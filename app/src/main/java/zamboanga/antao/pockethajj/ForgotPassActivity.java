package zamboanga.antao.pockethajj;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private EditText etEnterEmail;
    private Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        changeStatusBarColor();

        //toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar_forgotpass);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Forot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //fields
        etEnterEmail = (EditText) findViewById(R.id.etEnterEmail);
        btnResetPassword = (Button) findViewById(R.id.btnResetPass);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEnterEmail.getText().toString().trim();

                if (!TextUtils.isEmpty(email)) {

                    Toast.makeText(ForgotPassActivity.this, "Please enter Email", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(ForgotPassActivity.this, "Please Check you email to reset the Password", Toast.LENGTH_LONG).show();
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

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }
}
