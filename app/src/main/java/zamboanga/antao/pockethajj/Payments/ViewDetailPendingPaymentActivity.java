package zamboanga.antao.pockethajj.Payments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import zamboanga.antao.pockethajj.R;

public class ViewDetailPendingPaymentActivity extends AppCompatActivity {

    private TextView tvAmount;
    private TextView tvDateUpload;
    private ImageView imgScreenShot;
    private Button btnDetailOK;

    private DatabaseReference mPilgrimPendingPaymentDatabase;

    private FirebaseUser mCurrentUser;

    private String user_id = null;
    private String pushkey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail_pending_payment);

        user_id = getIntent().getStringExtra("user_id");
        pushkey = getIntent().getStringExtra("push_key");

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        tvAmount = (TextView) findViewById(R.id.tvViewPendingAmountForPilgrim);
        tvDateUpload = (TextView) findViewById(R.id.tvViewDateUploadForPilgrim);
        imgScreenShot = (ImageView) findViewById(R.id.imgPendingScreenshot);
        btnDetailOK = (Button) findViewById(R.id.btnViewPendingForPilgrimOk);

        mPilgrimPendingPaymentDatabase = FirebaseDatabase.getInstance().getReference().child("PendingPayments").child(current_uid).child(pushkey);

        btnDetailOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPilgrimPendingPaymentDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");

                    final String screenshoturl = dataSnapshot.child("screenshoturl").getValue().toString();
                    Double amount = Double.valueOf(dataSnapshot.child("amount").getValue().toString());
                    String dateupload = dataSnapshot.child("dateupload").getValue().toString();

                    tvAmount.setText(decimalFormat.format(amount));
                    tvDateUpload.setText(dateupload);

                    Picasso.with(getApplicationContext())
                            .load(screenshoturl)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.ic_center_focus_weak_black_24dp)
                            .into(imgScreenShot, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(getApplicationContext())
                                            .load(screenshoturl)
                                            .placeholder(R.drawable.ic_center_focus_weak_black_24dp)
                                            .into(imgScreenShot);
                                }
                            });

                } else {
                    Toast.makeText(getApplicationContext(), dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
