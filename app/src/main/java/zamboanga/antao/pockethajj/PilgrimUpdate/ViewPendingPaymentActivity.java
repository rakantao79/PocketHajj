package zamboanga.antao.pockethajj.PilgrimUpdate;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

public class ViewPendingPaymentActivity extends AppCompatActivity {

    private DatabaseReference mUsersDatabase;

    private DatabaseReference mApprovedPayments;
    private DatabaseReference mPendingPayments;

    private TextView tvAmountPayment;
    private TextView tvDateUpload;
    private ImageView imgScreenshot;

    private Button btnApprovedPayment;
    private Button btnDeclinePayment;

    private String user_id = null;
    private String pushkey = null;

    private String screenshoturl = null;
    private Double amount = null;
    private String dateupload = null;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pending_payment);

        final DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");

        mAuth = FirebaseAuth.getInstance();

        user_id = getIntent().getExtras().getString("user_id");
        pushkey = getIntent().getExtras().getString("pushkey");

        tvAmountPayment = (TextView) findViewById(R.id.tvViewPendingAmount);
        tvDateUpload = (TextView) findViewById(R.id.tvViewDateUpload);
        imgScreenshot = (ImageView) findViewById(R.id.imgViewPendingPayment) ;
        btnApprovedPayment = (Button) findViewById(R.id.btnApprovePendingAmount);
        btnDeclinePayment = (Button) findViewById(R.id.btnDeclinePendingAmount);

        //database reference of detail view of pending payments
         mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("PendingPayments").child(user_id).child(pushkey);

        mPendingPayments = FirebaseDatabase.getInstance().getReference().child("PendingPayments").child(user_id);

        mApprovedPayments = FirebaseDatabase.getInstance().getReference().child("ApprovedPayments");

        mUsersDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    screenshoturl = dataSnapshot.child("screenshoturl").getValue().toString();
                    Double amount = Double.valueOf(dataSnapshot.child("amount").getValue().toString());
                    String dateupload = dataSnapshot.child("dateupload").getValue().toString();

                    //tvAmountPayment.setText(String.format("%.2f", amount));
                    tvAmountPayment.setText(decimalFormat.format(amount));
                    tvDateUpload.setText(dateupload);

                    //Toast.makeText(ViewPendingPaymentActivity.this, dataSnapshot.hasChild(user_id).to, Toast.LENGTH_SHORT);

                    Picasso.with(ViewPendingPaymentActivity.this)
                            .load(screenshoturl)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.ic_center_focus_weak_black_24dp)
                            .into(imgScreenshot, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(ViewPendingPaymentActivity.this)
                                            .load(screenshoturl)
                                            .placeholder(R.drawable.ic_center_focus_weak_black_24dp)
                                            .into(imgScreenshot);
                                }
                            });
                } else {
                    Toast.makeText(ViewPendingPaymentActivity.this, dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ViewPendingPaymentActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT);
            }
        });

        approvedPayment();
        declinePayment();

    }

    private void declinePayment() {
        btnDeclinePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                mUsersDatabase.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            nullEverything();
                            finish();
                        }
                    }
                });
            }
        });

    }

    private void approvedPayment() {
        btnApprovedPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //

                mUsersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mApprovedPayments.child(user_id).push().setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                if (databaseError != null){
                                    Toast.makeText(ViewPendingPaymentActivity.this, "Copy Failed", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ViewPendingPaymentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    finish();

                                    nullEverything();

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mUsersDatabase.removeValue();
                                        }
                                    }, 3000);
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(ViewPendingPaymentActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    private void nullEverything(){
        screenshoturl = null;
        dateupload = null;
        amount = null;
    }
}
