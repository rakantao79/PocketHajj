package zamboanga.antao.pockethajj;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
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
import android.widget.Switch;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar mToolbar;
    private EditText etEmail;
    private EditText etPassword;

    private TextView tvForgotPassword;
    private TextView tvCreateAccount;

    private Button btnLogin;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstateListener;
    private DatabaseReference mUserDatabase;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //toolbar
        mToolbar = (Toolbar)findViewById(R.id.toolbar_login);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //firebase
        mAuth = FirebaseAuth.getInstance();

        //progress dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Verifying Your Account");
        mProgressDialog.setMessage("Please Wait...");

        //fields
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        tvCreateAccount = (TextView) findViewById(R.id.tvCreateAccount);

        btnLogin = (Button) findViewById(R.id.btnLogin);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        tvForgotPassword.setOnClickListener(this);
        tvCreateAccount.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        //change status background color
        changeStatusBarColor();

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tvForgotPassword:
                startActivity(new Intent(getApplicationContext(), ForgotPassActivity.class));
                finish();
                break;
            case R.id.btnLogin:
                //login account

                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                mProgressDialog.show();

                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){

                    loginUser(email, password);
                    //mProgressDialog.dismiss();

                } else {

                    Toast.makeText(LoginActivity.this, "Please Check the Fields", Toast.LENGTH_SHORT).show();
                    mProgressDialog.hide();
                }

                break;
            case R.id.tvCreateAccount:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
                break;
        }
    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    mProgressDialog.dismiss();

                    FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference mDatabase;
                    String current_uid = mCurrentUser.getUid();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String accounttype = dataSnapshot.child("accounttype").getValue().toString();

                            Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);

                            intent.putExtra("accounttype", accounttype);

                            //clears previous intent
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                            finish();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {



                        }
                    });
//                    Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
//
//                    //clears previous intent
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                    startActivity(intent);
//                    Toast.makeText(LoginActivity.this, "Successfully Login", Toast.LENGTH_SHORT).show();
//                    finish();

                } else {

                    mProgressDialog.hide();
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
}
