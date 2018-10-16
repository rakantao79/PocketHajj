package zamboanga.antao.pockethajj;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.preference.DialogPreference;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Toolbar mToolbar;

        /*
        addiotinal fields
        middlename editext
        gender - radiogroup
        citizenship - editext
        civil status- radio group
        */

    private EditText etDisplayName;
    private EditText etLastName;
    private EditText etFirstName;
    private EditText etMobileNumber;
    private EditText etEmail;
    private EditText etPassword;

    private EditText etMiddleName;
    private EditText etCitizenship;
    private EditText etAddress;
    private RadioGroup rgGender;
    private RadioGroup rgCivilStatus;

    private RadioButton rbMale;
    private RadioButton rbFemale;
    private RadioButton rbSingle;
    private RadioButton rbMarried;

    private TextView tvHaveAccountLoginHere;
    private TextView tvAgree;

    private Button btnRegister;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private ProgressDialog mProgressDialog;

    private String mGender = null;
    private String mCiviStatus = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //firebase
        mAuth = FirebaseAuth.getInstance();

        //progress dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Registering Your Account");
        mProgressDialog.setMessage("Please Wait...");

        //toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar_register);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //fields
        etDisplayName = (EditText) findViewById(R.id.et_DisplayName);
        etLastName = (EditText)findViewById(R.id.et_lastName);
        etFirstName = (EditText)findViewById(R.id.et_firstName);
        etMobileNumber = (EditText)findViewById(R.id.et_MobileNum);
        etEmail = (EditText)findViewById(R.id.et_email);
        etPassword = (EditText)findViewById(R.id.et_password);

        /*
        addiotinal fields
        middlename editext
        gender - radiogroup
        address
        citizenship - editext
        civil status- radio group
        */

        etMiddleName = (EditText) findViewById(R.id.et_middleName);
        etAddress = (EditText) findViewById(R.id.et_pilgrimAddress);
        etCitizenship = (EditText) findViewById(R.id.et_PilgrimCitizenship);

        rgGender = (RadioGroup) findViewById(R.id.radiogroup_Gender) ;
        rgCivilStatus = (RadioGroup) findViewById(R.id.radiogroup_CivilStatus) ;

        rbMale = (RadioButton)findViewById(R.id.radioMale);
        rbFemale = (RadioButton)findViewById(R.id.radioFemale);
        rbSingle = (RadioButton) findViewById(R.id.radioSingle);
        rbMarried = (RadioButton) findViewById(R.id.radioMarried);

        selectGender();
        selectCivilStatus();


        tvHaveAccountLoginHere = (TextView) findViewById(R.id.tvHaveAccount) ;
        tvAgree = (TextView) findViewById(R.id.tvTOA) ;

        btnRegister = (Button) findViewById(R.id.btn_register) ;


        tvHaveAccountLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Terms of Agreement")
                        .setMessage("Pocket Hajj terms of Agreement")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start register

                mProgressDialog.show();

                final String displayName = etDisplayName.getText().toString().trim();
                final String firstName = etFirstName.getText().toString().trim();
                final String lastName = etLastName.getText().toString().trim();
                final String email = etEmail.getText().toString().trim();
                final String password = etPassword.getText().toString().trim();
                final String mobilenumber = etMobileNumber.getText().toString().trim();

                String middlename = etMiddleName.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                String citizenship = etCitizenship.getText().toString().trim();

                if (TextUtils.isEmpty(displayName) ||
                        TextUtils.isEmpty(firstName) ||
                        TextUtils.isEmpty(lastName) ||
                        TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(password) ||
                        TextUtils.isEmpty(mobilenumber) ||
                        TextUtils.isEmpty(middlename) ||
                        TextUtils.isEmpty(address) ||
                        TextUtils.isEmpty(citizenship) ||
                        TextUtils.isEmpty(mGender) ||
                        TextUtils.isEmpty(mCiviStatus)) {

                    mProgressDialog.hide();
                    Toast.makeText(getApplicationContext(), "All Fields Are Required", Toast.LENGTH_SHORT).show();

                } else if (mobilenumber.length() < 11){

                   mProgressDialog.hide();
//                    Toast.makeText(RegisterActivity.this, "Please check the fields", Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegisterActivity.this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(RegisterActivity.this, "Sucessfully Registered", Toast.LENGTH_SHORT).show();
                    registerUser();
                }
            }
        });

        //change color
        changeStatusBarColor();

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void selectCivilStatus() {

        rgCivilStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId){
                    case R.id.radioSingle:
                        //
                        mCiviStatus = "Single";
                        break;
                    case R.id.radioMarried:
                        //
                        mCiviStatus = "Married";
                        break;
                }
            }
        });
    }

    private void selectGender() {

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId){
                    case R.id.radioMale:
                        //
                        mGender = "Male";
                        break;
                    case R.id.radioFemale:
                        //
                        mGender = "Female";
                        break;
                }
            }
        });

    }

    private void registerUser() {

        final String displayName = etDisplayName.getText().toString().trim();
        final String firstName = etFirstName.getText().toString().trim();
        final String lastName = etLastName.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String mobilenumber = etMobileNumber.getText().toString().trim();

        final String middlename = etMiddleName.getText().toString().trim();
        final String address = etAddress.getText().toString().trim();
        final String citizenship = etCitizenship.getText().toString().trim();

        final int currentyear = Calendar.getInstance().get(Calendar.YEAR);
        final String yearInString = String.valueOf(currentyear);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressDialog.dismiss();

                if (task.isSuccessful()){
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    final String uid = current_user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    //Log.d("Lat", String.valueOf(lat));

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("displayname", displayName);
                    userMap.put("firstname", firstName);
                    userMap.put("lastname", lastName);
                    userMap.put("email", email);
                    userMap.put("password", password);
                    userMap.put("mobilenumber", mobilenumber);
                    userMap.put("IDNum", "IDNum");
                    userMap.put("image", "default");
                    userMap.put("thumbimage", "default");

                    userMap.put("accounttype", "pilgrim");
                    userMap.put("yellowcard","On Process");
                    userMap.put("biometric","On Process");
                    userMap.put("passport","On Process");
                    userMap.put("regyear", "pilgrim"+yearInString);

                    userMap.put("accountstatus","Not Verified");

                    userMap.put("middlename",middlename);
                    userMap.put("gender",mGender);
                    userMap.put("address",address);
                    userMap.put("citizenship",citizenship);
                    userMap.put("civilstatus",mCiviStatus);

                    userMap.put("passportexpiry","");
                    userMap.put("yellowcardexpiry","");

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                //create another table for accounttype
                                DatabaseReference mPilgrimDatabase = FirebaseDatabase.getInstance().getReference().child("Pilgrim").child(uid);
                                HashMap<String, String> userPilgrimMap = new HashMap<String, String>();
                                userPilgrimMap.put("accounttype", "pilgrim");

                                mPilgrimDatabase.setValue(userPilgrimMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        DatabaseReference mPilgrimPhoneNumberDatabase = FirebaseDatabase.getInstance().getReference().child("PilgrimPhoneNumber").child(uid);


                                        mProgressDialog.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        Toast.makeText(RegisterActivity.this, "Successfully Registered / You may now Login", Toast.LENGTH_SHORT).show();
                                        finish();
                                        mAuth.signOut();
                                    }
                                });
//                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//                                Toast.makeText(RegisterActivity.this, "Successfully Registered / You may now Login", Toast.LENGTH_SHORT).show();
//                                finish();
//                                mAuth.signOut();
                            }
                        }
                    });

                } else {
                    mProgressDialog.hide();
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        //mAuth.addAuthStateListener(mAuthListener);

        if (currentUser != null){
            Toast.makeText(RegisterActivity.this, "Start Main Activity", Toast.LENGTH_SHORT).show();
        }
    }
}