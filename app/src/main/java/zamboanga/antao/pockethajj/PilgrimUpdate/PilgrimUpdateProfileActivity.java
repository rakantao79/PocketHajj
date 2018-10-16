package zamboanga.antao.pockethajj.PilgrimUpdate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import zamboanga.antao.pockethajj.R;

public class PilgrimUpdateProfileActivity extends AppCompatActivity {

    //toolbar
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilgrim_update_profile);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_pilgrim_update_profile);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Update Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
