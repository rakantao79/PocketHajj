package zamboanga.antao.pockethajj;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar mToolbar;

    private Button btnHaveAccount;
    private Button btnCreateAccount;
    private Button btnAsGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        changeStatusBarColor();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_start);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Pocket Hajj");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnHaveAccount = (Button) findViewById(R.id.btnHaveAccount);
        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        btnAsGuest = (Button) findViewById(R.id.btnAsGuest);


        btnHaveAccount.setOnClickListener(this);
        btnCreateAccount.setOnClickListener(this);
        btnAsGuest.setOnClickListener(this);


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnHaveAccount:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
            case R.id.btnCreateAccount:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                break;
            case R.id.btnAsGuest:

                String accounttype = "guest";
                Intent intent = new Intent(StartActivity.this, NavigationActivity.class);
                //startActivity(new Intent(getApplicationContext(), NavigationActivity.class));
                intent.putExtra("accounttype", accounttype);

                startActivity(intent);
                break;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_start, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);


        if (item.getItemId() == R.id.menu_admin) {

            Toast.makeText(StartActivity.this, "Login As Admin", Toast.LENGTH_SHORT).show();

        }

        return true;
    }

}
