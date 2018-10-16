package zamboanga.antao.pockethajj;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class RegisterHajjGuideActivity extends AppCompatActivity {


    private Toolbar mToolbar;

    private EditText etLastName;
    private EditText etFirstName;
    private EditText etContactNum;
    private EditText etIDNum;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnRegisterHajjGuide;
    private Button btnCancel;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;


    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_hajj_guide);


        //firebase
        mAuth = FirebaseAuth.getInstance();

        //progress dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Adding Hajj Guide to System");
        mProgressDialog.setMessage("Please Wait...");

        //toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar_register);
        setSupportActionBar(mToolbar);
        //getSupportActionBar().setTitle("Register Hajj Guide");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        etLastName = (EditText)findViewById(R.id.etLastName);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etContactNum = (EditText) findViewById(R.id.etMobileNum);
        etIDNum = (EditText) findViewById(R.id.etIDNumber);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnRegisterHajjGuide = (Button) findViewById(R.id.btnRegisterHajjGuide);

        btnRegisterHajjGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //register hajj guide
                final String hajjLastName = etLastName.getText().toString().trim();
                String hajjFirstName = etFirstName.getText().toString().trim();
                String hajjContactNum = etContactNum.getText().toString().trim();
                String hajjIDNum = etIDNum.getText().toString().trim();
                String hajjEmail = etEmail.getText().toString().trim();
                String hajjPassword = etPassword.getText().toString().trim();

                if (!TextUtils.isEmpty(hajjLastName) ||
                        !TextUtils.isEmpty(hajjFirstName) ||
                        hajjContactNum.length() == 12 ||
                        !TextUtils.isEmpty(hajjIDNum) ||
                        !TextUtils.isEmpty(hajjEmail) ||
                        !TextUtils.isEmpty(hajjPassword)) {

                    mProgressDialog.show();
                    registerHajjGuide();

                } else {
                    mProgressDialog.hide();
                    Toast.makeText(RegisterHajjGuideActivity.this, "Please check the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void registerHajjGuide() {
        final String hajjLastName = etLastName.getText().toString().trim();
        final String hajjFirstName = etFirstName.getText().toString().trim();
        final String hajjContactNum = etContactNum.getText().toString().trim();
        final String hajjIDNum = etIDNum.getText().toString().trim();
        final String hajjEmail = etEmail.getText().toString().trim();
        final String hajjPassword = etPassword.getText().toString().trim();

        int currentyear = Calendar.getInstance().get(Calendar.YEAR);
        final String yearInString = String.valueOf(currentyear);


        mAuth.createUserWithEmailAndPassword(hajjEmail, hajjPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    final String uid = current_user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String, String> hajjMap = new HashMap<String, String>();
                    hajjMap.put("displayname", "displayname");
                    hajjMap.put("firstname", hajjFirstName);
                    hajjMap.put("lastname", hajjLastName);
                    hajjMap.put("email", hajjEmail);
                    hajjMap.put("password", hajjPassword);
                    hajjMap.put("mobilenumber", hajjContactNum);
                    hajjMap.put("IDNum", hajjIDNum);
                    hajjMap.put("image", "default");
                    hajjMap.put("thumbimage", "default");

                    hajjMap.put("accounttype", "hajjguide");
                    hajjMap.put("yellowcard","On Process");
                    hajjMap.put("biometric","On Process");
                    hajjMap.put("passport","On Process");
                    hajjMap.put("regyear", "hajjguide"+yearInString);

                    hajjMap.put("accountstatus","Verified");

                    hajjMap.put("middlename","");
                    //userMap.put("","");
                    hajjMap.put("gender","");
                    hajjMap.put("address","");
                    hajjMap.put("citizenship","");
                    hajjMap.put("civilstatus","");

                    hajjMap.put("passportexpiry","");
                    hajjMap.put("yellowcardexpiry","");

                    mDatabase.setValue(hajjMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                DatabaseReference mHajjDatabase = FirebaseDatabase.getInstance().getReference().child("HajjGuide").child(uid);
                                HashMap<String, String> userHajjGuideMap = new HashMap<String, String>();
                                userHajjGuideMap.put("accounttype", "hajjguide");

                                mHajjDatabase.setValue(userHajjGuideMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        mProgressDialog.dismiss();
                                        Intent intent = new Intent(RegisterHajjGuideActivity.this, AdminGUIActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        Toast.makeText(RegisterHajjGuideActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}
