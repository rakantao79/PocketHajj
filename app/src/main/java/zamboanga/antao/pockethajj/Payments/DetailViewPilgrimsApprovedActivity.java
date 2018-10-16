package zamboanga.antao.pockethajj.Payments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import zamboanga.antao.pockethajj.PilgrimUpdate.ViewPendingPaymentActivity;
import zamboanga.antao.pockethajj.R;

public class DetailViewPilgrimsApprovedActivity extends AppCompatActivity {

    private TextView tvAmount;
    private TextView tvDateUpload;
    private ImageView imgScreenShot;
    private Button btnDetailOK;

    private DatabaseReference mDetailApprovedPayments;

    private String user_id = null;
    private String pushkey = null;

    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view_pilgrims_approved);

        final DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");

        user_id = getIntent().getStringExtra("user_id");
        pushkey = getIntent().getStringExtra("pushkey");

        tvAmount = (TextView) findViewById(R.id.tvDetailPilgrimAmount);
        tvDateUpload = (TextView) findViewById(R.id.tvDetailPilgrimDateUpload);
        imgScreenShot = (ImageView) findViewById(R.id.imgDetailPilgrimScreenShot);
        btnDetailOK = (Button) findViewById(R.id.btnDetailPilgrimOk);

        btnDetailOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDetailApprovedPayments = FirebaseDatabase.getInstance().getReference().child("ApprovedPayments").child(user_id).child(pushkey);

        mDetailApprovedPayments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    final String screenshoturl = dataSnapshot.child("screenshoturl").getValue().toString();
                    Double amount = Double.valueOf(dataSnapshot.child("amount").getValue().toString());
                    String dateupload = dataSnapshot.child("dateupload").getValue().toString();

                    tvAmount.setText(decimalFormat.format(amount));
                    tvDateUpload.setText(dateupload);

                    Picasso.with(DetailViewPilgrimsApprovedActivity.this)
                            .load(screenshoturl)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.ic_center_focus_weak_black_24dp)
                            .into(imgScreenShot, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(DetailViewPilgrimsApprovedActivity.this)
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