package zamboanga.antao.pockethajj.Fragments.Admin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import zamboanga.antao.pockethajj.R;

public class VerifyProfileActivity extends AppCompatActivity {

    private DatabaseReference mUsersDatabase;

    private TextView tvVerifyLastName;
    private TextView tvVerifyMiddleName;
    private TextView tvVerifyFirstName;
    private TextView tvVerifyGender;
    private TextView tvVerifyAddress;
    private TextView tvVerifyCivilStatus;
    private TextView tvVerifyAccountStatus;
    private TextView tvVerifyMobileNumber;
    private TextView tvVerifyCitizenship;

    private CircleImageView imgVerify;

    private String switchOn = "Verified";
    private String switchOff = "Not Verified";

    private Switch switchVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_profile);

        final String user_id = getIntent().getStringExtra("user_id");

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        tvVerifyLastName = (TextView) findViewById(R.id.tvVerifyLastName);
        tvVerifyMiddleName= (TextView) findViewById(R.id.tvVerifyMiddleName);
        tvVerifyFirstName= (TextView) findViewById(R.id.tvVerifyFirstName);
        tvVerifyGender= (TextView) findViewById(R.id.tvVerifyGender);
        tvVerifyAddress= (TextView) findViewById(R.id.tvVerifyAddress);
        tvVerifyCivilStatus= (TextView) findViewById(R.id.tvVerifyCivilStatus);
        tvVerifyAccountStatus= (TextView) findViewById(R.id.tvVerifyAccountStatus);
        tvVerifyMobileNumber= (TextView) findViewById(R.id.tvVerifyPhoneNumber);
        tvVerifyCitizenship = (TextView) findViewById(R.id.tvVerifyCitizenship);

        switchVerify = (Switch) findViewById(R.id.switchVerify);

        imgVerify = (CircleImageView) findViewById(R.id.imgVerify);

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String displayname = dataSnapshot.child("displayname").getValue().toString();
                String contactnumber = dataSnapshot.child("mobilenumber").getValue().toString();
                String lastname = dataSnapshot.child("lastname").getValue().toString();
                String firstname = dataSnapshot.child("firstname").getValue().toString();
                String middlename = dataSnapshot.child("middlename").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();

                String accountstatus = dataSnapshot.child("accountstatus").getValue().toString();
                String citizenship = dataSnapshot.child("citizenship").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                String gender = dataSnapshot.child("gender").getValue().toString();
                String civilstatus = dataSnapshot.child("civilstatus").getValue().toString();

                tvVerifyLastName.setText(lastname);
                tvVerifyMiddleName.setText(middlename);
                tvVerifyFirstName.setText(firstname);
                tvVerifyGender.setText(gender);
                tvVerifyAddress.setText(address);
                tvVerifyCivilStatus.setText(civilstatus);
                tvVerifyAccountStatus.setText(accountstatus);
                tvVerifyMobileNumber.setText(contactnumber);
                tvVerifyCitizenship.setText(citizenship);

                if (!image.equals("default")){
                    Picasso.with(VerifyProfileActivity.this)
                            .load(image)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.img_profile_img)
                            .into(imgVerify, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(VerifyProfileActivity.this)
                                            .load(image)
                                            .placeholder(R.drawable.img_profile_img)
                                            .into(imgVerify);
                                }
                            });
                } // image closer

                if (accountstatus.equals("Not Verified")){
                    switchVerify.setChecked(false);
                    tvVerifyAccountStatus.setText(switchOff);
                } else {
                    switchVerify.setChecked(true);
                    tvVerifyAccountStatus.setText(switchOn);
                }

                switchVerify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked){
                            mUsersDatabase.child("accountstatus").setValue(switchOn).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        switchVerify.setChecked(true);
                                        tvVerifyAccountStatus.setText(switchOn);
                                    }

                                }
                            });
                        } else {

                            mUsersDatabase.child("accountstatus").setValue(switchOff).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        switchVerify.setChecked(false);
                                        tvVerifyAccountStatus.setText(switchOff);
                                    }
                                }
                            });
                        }

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
