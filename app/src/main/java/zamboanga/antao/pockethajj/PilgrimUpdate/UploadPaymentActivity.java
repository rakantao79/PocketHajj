package zamboanga.antao.pockethajj.PilgrimUpdate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import zamboanga.antao.pockethajj.R;
import zamboanga.antao.pockethajj.StartActivity;

import static java.util.Calendar.YEAR;

public class UploadPaymentActivity extends AppCompatActivity {

    private ImageView imgScreenshot;

    private EditText etInputAmount;
    private Button btnSubmit;
    private Button btnSubmitCancel;
    private Uri imageUri = null;

    private StorageReference mStorage;
    private DatabaseReference mPendingPaymentDatabase;
    private DatabaseReference mApprovedPaymentDatabase;

    private static final int GALLERY_REQUEST = 1;

    private FirebaseAuth mAuth;

    private ProgressDialog mProgressDialog;

    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_payment);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = current_user.getUid();

        mProgressDialog = new ProgressDialog(UploadPaymentActivity.this);
        mProgressDialog.setTitle("Uploading Image");
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.setCanceledOnTouchOutside(false);

        imgScreenshot = (ImageView)findViewById(R.id.img_screenshot);
        etInputAmount = (EditText) findViewById(R.id.etInputAmount);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmitCancel = (Button) findViewById(R.id.btnSubmitCancel);

        mStorage = FirebaseStorage.getInstance().getReference();
        mPendingPaymentDatabase = FirebaseDatabase.getInstance().getReference().child("PendingPayments").child(uid);
        mApprovedPaymentDatabase = FirebaseDatabase.getInstance().getReference().child("ApprovedPayments").child(uid);

        imgScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });

        btnSubmitCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSubmit();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            imageUri = data.getData();

            imgScreenshot.setImageURI(imageUri);

        }
    }

    private void startSubmit() {

        mProgressDialog.show();

        final Double amountprice = Double.parseDouble(etInputAmount.getText().toString().trim());

        if (amountprice > 0 && imageUri != null){

            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
            final String uid = current_user.getUid();

            //get date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            final SimpleDateFormat month_date = new SimpleDateFormat("MMMM dd yyyy");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");

            String date = dateFormat.format(c.getTime());

            final String month_name = month_date.format(c.getTime());

            //start uploading..

            StorageReference filepath = mStorage.child("Screenshot_Images").child(date + imageUri.getLastPathSegment());


            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests")
                    Uri imageUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference payment = mPendingPaymentDatabase.push();

                    payment.child("amount").setValue(amountprice);
                    payment.child("screenshoturl").setValue(imageUrl.toString());
                    payment.child("dateupload").setValue(month_name);
                    payment.child("uid").setValue(uid);

                    mProgressDialog.dismiss();
                    finish();


                }
            });

        } else if (amountprice <= 0.0){

            Toast.makeText(UploadPaymentActivity.this, "Enter Amount", Toast.LENGTH_SHORT).show();

        } else {
            mProgressDialog.dismiss();
            Toast.makeText(UploadPaymentActivity.this, "Select Image and Input the desired amount", Toast.LENGTH_SHORT).show();
        }
    }
}
