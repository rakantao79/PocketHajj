package zamboanga.antao.pockethajj.PilgrimUpdate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import zamboanga.antao.pockethajj.Payments.PaymentsActivity;
import zamboanga.antao.pockethajj.ProfileView.SelectHajjGuideActivity;
import zamboanga.antao.pockethajj.R;

import static java.util.Calendar.YEAR;

public class UpdateRequirementsPilgrim extends AppCompatActivity {

    private DatabaseReference mUsersDatabase;

    private TextView mFirstName;
    private TextView mLastName;

    private TextView mYelloCard;
    private TextView mBiometrics;
    private TextView mPassport;

    private Button mYellowCardExpiry;
    private Button mPassportExpiry;
    private Button mViewPayment;

    private Switch mSwitchYellowCard;
    private Switch mSwitchBiometrics;
    private Switch mSwitchPassport;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private CircleImageView mCircularImageProf;

    String switchOn = "Completed";
    String switchOff = "On Process";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_requirements_pilgrim);

        final String user_id = getIntent().getStringExtra("user_id");

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        mLastName = (TextView) findViewById(R.id.tvRqrmntsLastName);
        mFirstName = (TextView) findViewById(R.id.tvRqrmntsFirstName);

        mYelloCard = (TextView) findViewById(R.id.tvYellowCardUpdate);
        mPassport = (TextView) findViewById(R.id.tvPassportUpdate);
        mBiometrics = (TextView) findViewById(R.id.tvBiometricsUpdate);

//        mYellowCardExpiry = (Button) findViewById(R.id.btnYellowCardExpiry);
//        mPassportExpiry = (Button) findViewById(R.id.btnPassportExpiry);
        //mViewPayment = (Button) findViewById(R.id.btnViewPilgrimPayments);

//        mYellowCardExpiry.setVisibility(View.INVISIBLE);
//        mPassportExpiry.setVisibility(View.INVISIBLE);
//        mYellowCardExpiry.setEnabled(false);
//        mPassportExpiry.setEnabled(false);

        mSwitchYellowCard = (Switch) findViewById(R.id.switchYelloCard);
        mSwitchBiometrics = (Switch) findViewById(R.id.switchBiometrics);
        mSwitchPassport = (Switch) findViewById(R.id.switchPassport);

        mCircularImageProf = (CircleImageView) findViewById(R.id.imgCircuProfPic) ;


        //mSwitchYellowCard.setChecked(false);
        //mSwitchBiometrics.setChecked(false);
        //mSwitchPassport.setChecked(false);

        viewPilgrimPayments();

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    String lastname = dataSnapshot.child("lastname").getValue().toString();
                    String firstname = dataSnapshot.child("firstname").getValue().toString();
                    final String yellowcard = dataSnapshot.child("yellowcard").getValue().toString();
                    String passport = dataSnapshot.child("passport").getValue().toString();
                    String biometrics = dataSnapshot.child("biometric").getValue().toString();
                    final String image = dataSnapshot.child("image").getValue().toString();

                    final String passportexpiry = dataSnapshot.child("passportexpiry").getValue().toString();
                    String yellowcardexpiry = dataSnapshot.child("yellowcardexpiry").getValue().toString();

                    if (!image.equals("default")){
                        Picasso.with(UpdateRequirementsPilgrim.this)
                                .load(image)
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.img_profile_img)
                                .into(mCircularImageProf, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Picasso.with(UpdateRequirementsPilgrim.this)
                                                .load(image)
                                                .placeholder(R.drawable.img_profile_img)
                                                .into(mCircularImageProf);
                                    }
                                });
                    }

                    mLastName.setText(lastname);
                    mFirstName.setText(firstname);
                    //mPassportExpiry.setText("Passport Date Expiry : " + passportexpiry);
                    //mYellowCardExpiry.setText("Yellow Card Expiry : " + yellowcardexpiry);

                    if (yellowcard.equals("On Process")){
                        mSwitchYellowCard.setChecked(false);
                        mYelloCard.setText(switchOff);
                    } else {
                        mSwitchYellowCard.setChecked(true);
                        mYelloCard.setText(switchOn);
                    }

                    if (passport.equals("On Process")){
                        mSwitchPassport.setChecked(false);
                        mPassport.setText(switchOff);
                    } else {
                        mSwitchPassport.setChecked(true);
                        mPassport.setText(switchOn);
                    }

                    if (biometrics.equals("On Process")){
                        mSwitchBiometrics.setChecked(false);
                        mBiometrics.setText(switchOff);
                    } else {
                        mSwitchBiometrics.setChecked(true);
                        mBiometrics.setText(switchOn);
                    }

                    mSwitchYellowCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked){
                                //update yellowcard to complete
                                mUsersDatabase.child("yellowcard").setValue(switchOn).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            //mYelloCard.setText(switchOn);

                                            mSwitchYellowCard.setChecked(true);
                                            mYelloCard.setText(switchOn);
                                            Toast.makeText(UpdateRequirementsPilgrim.this, "Yellow Card Completed", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            } else {
                                mUsersDatabase.child("yellowcard").setValue(switchOff).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            mSwitchYellowCard.setChecked(false);
                                            Toast.makeText(UpdateRequirementsPilgrim.this, "Yellow Card On Process", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });

                    mSwitchPassport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked){
                                // switch on
                                mUsersDatabase.child("passport").setValue(switchOn).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            mSwitchPassport.setChecked(true);
                                            mPassport.setText(switchOn);
                                            Toast.makeText(UpdateRequirementsPilgrim.this, "Passport Completed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                //switch off
                                mUsersDatabase.child("passport").setValue(switchOff).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            mSwitchPassport.setChecked(false);
                                            mPassport.setText(switchOff);
                                            Toast.makeText(UpdateRequirementsPilgrim.this, "Passport On Process", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });

                    mSwitchBiometrics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked){
                                mUsersDatabase.child("biometric").setValue(switchOn).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            mSwitchBiometrics.setChecked(true);
                                            mBiometrics.setText(switchOn);
                                            Toast.makeText(UpdateRequirementsPilgrim.this, "Biometrics Completed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else {
                                mUsersDatabase.child("biometric").setValue(switchOff).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            mSwitchBiometrics.setChecked(false);
                                            mBiometrics.setText(switchOff);
                                            Toast.makeText(UpdateRequirementsPilgrim.this, "Biometrics On Process", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });

//                mPassportExpiry.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        final Calendar c = Calendar.getInstance();
//                        mYear = c.get(YEAR);
//                        mMonth = c.get(Calendar.MONTH);
//                        mDay = c.get(Calendar.DAY_OF_MONTH);
//
//                        final SimpleDateFormat month_date = new SimpleDateFormat("MMMM dd yyyy");
//
//                        final String month_name = month_date.format(c.getTime());
//
//                        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateRequirementsPilgrim.this, new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//                                mPassportExpiry.setText(month_name);
//
//                            }
//                        }, mYear, mMonth, mDay);
//                        datePickerDialog.show();
//
//                    }
//                });

//                mYellowCardExpiry.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        final Calendar c = Calendar.getInstance();
//                        mYear = c.get(YEAR);
//                        mMonth = c.get(Calendar.MONTH);
//                        mDay = c.get(Calendar.DAY_OF_MONTH);
//
//                        final SimpleDateFormat month_date = new SimpleDateFormat("MMMM dd yyyy");
//
//                        final String month_name = month_date.format(c.getTime());
//
//                        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateRequirementsPilgrim.this, new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//                                mYellowCardExpiry.setText(month_name);
//
//                            }
//                        }, mYear, mMonth, mDay);
//                        datePickerDialog.show();
//
//                    }
//                });

                } else {
                    Toast.makeText(UpdateRequirementsPilgrim.this, dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UpdateRequirementsPilgrim.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); // add value evetlistener closer

    }

    private void viewPilgrimPayments() {

//        mViewPayment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                Intent intent = new Intent(UpdateRequirementsPilgrim.this, PaymentsActivity.class);
////                startActivity(intent);
//
//            }
//        });
    }
}