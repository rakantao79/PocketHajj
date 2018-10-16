package zamboanga.antao.pockethajj;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import zamboanga.antao.pockethajj.Fragments.AboutUsFragment;
import zamboanga.antao.pockethajj.Fragments.Admin.AdminPilgrimFragment;
import zamboanga.antao.pockethajj.Fragments.Admin.AdminProfileFragment;
import zamboanga.antao.pockethajj.Fragments.ExtrasFragment;
import zamboanga.antao.pockethajj.Fragments.FAQFragment;
import zamboanga.antao.pockethajj.Fragments.HajjGuide.HajjGuideProfileFragment;
import zamboanga.antao.pockethajj.Fragments.HajjInfoFragment;
import zamboanga.antao.pockethajj.Fragments.Pilgrim.PilgrimStatusFragment;
import zamboanga.antao.pockethajj.Fragments.ScheduleOfActivitiesFragment;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //database
    private DatabaseReference mDatabase;

    //firebase
    private FirebaseUser mCurrentUser;

    //auth
    private FirebaseAuth mAuth;

    //progress dialog
    private ProgressDialog mProgressDialog;

    private String accounttype;

    private TextView tvAccountType;

    NavigationView navigationView;

    public void getAccountType(){

        //Hide menu items
        Menu menunav = navigationView.getMenu();

        MenuItem nav_AdminProfile = menunav.findItem(R.id.nav_Admin_profile);
        MenuItem nav_PilgrimProfile = menunav.findItem(R.id.nav_Pilgrim_profile);
        MenuItem nav_HajjGuideProfile = menunav.findItem(R.id.nav_HajjGuideProfile);
        MenuItem nav_Schedule = menunav.findItem(R.id.nav_Schedule);
        MenuItem nav_HajjList = menunav.findItem(R.id.nav_hajjguid_list);

        //hide what is needed to hide
        nav_AdminProfile.setVisible(false);
        nav_PilgrimProfile.setVisible(false);
        nav_HajjGuideProfile.setVisible(false);
        nav_HajjList.setVisible(false);
        nav_Schedule.setVisible(false);

        //filter account type

        if (accounttype.equals("pilgrim")){

            Toast.makeText(NavigationActivity.this, "Logged is As Pilgrim", Toast.LENGTH_SHORT).show();

            // unhide menu item
            nav_AdminProfile.setVisible(false);
            nav_PilgrimProfile.setVisible(true);
            nav_HajjList.setVisible(false);
            nav_Schedule.setVisible(true);
            nav_HajjGuideProfile.setVisible(false);


            //load fragment status
            getSupportFragmentManager().beginTransaction().replace(R.id.content_nav, new PilgrimStatusFragment()).commit();
            getSupportActionBar().setTitle("My Status");

            mProgressDialog.dismiss();

        } else if (accounttype.equals("hajjguide")){


            getSupportFragmentManager().beginTransaction().replace(R.id.content_nav, new HajjGuideProfileFragment()).commit();
            getSupportActionBar().setTitle("Hajj Guide");


            nav_AdminProfile.setVisible(false);
            nav_PilgrimProfile.setVisible(false);
            nav_HajjList.setVisible(false);
            nav_Schedule.setVisible(true);
            nav_HajjGuideProfile.setVisible(true);

            Toast.makeText(NavigationActivity.this, "Logged is As HajjGuide", Toast.LENGTH_SHORT).show();

            mProgressDialog.dismiss();

        } else if (accounttype.equals("admin")){

            Toast.makeText(NavigationActivity.this, "Logged is As Admin", Toast.LENGTH_SHORT).show();

            nav_AdminProfile.setVisible(true);
            nav_PilgrimProfile.setVisible(false);
            nav_HajjList.setVisible(false);
            nav_Schedule.setVisible(true);
            nav_HajjGuideProfile.setVisible(false);

            getSupportFragmentManager().beginTransaction().replace(R.id.content_nav, new AdminProfileFragment()).commit();
            getSupportActionBar().setTitle("Admin");

            mProgressDialog.dismiss();

        } else {
            Toast.makeText(NavigationActivity.this, "Welcome Guest", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_nav, new HajjInfoFragment()).commit();
            getSupportActionBar().setTitle("Hajj Info");
            mProgressDialog.dismiss();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading App");
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        //String current_uid = mCurrentUser.getUid();

        tvAccountType = (TextView) findViewById(R.id.tvAccountType);

        accounttype = getIntent().getStringExtra("accounttype");

        getAccountType();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);

        MenuItem menu_Admin = menu.findItem(R.id.menu_NavAdmin);
        MenuItem menu_HajjGuide = menu.findItem(R.id.menu_NavHajjGuide);

        menu_Admin.setVisible(false);
        menu_HajjGuide.setVisible(false);

        if (accounttype.equals("admin")){

            menu_Admin.setVisible(true);
            menu_HajjGuide.setVisible(false);

        } else if (accounttype.equals("hajjguide")){

            menu_Admin.setVisible(false);
            menu_HajjGuide.setVisible(true);

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_NavAdmin) {

            //Continue As Admin
            startActivity(new Intent(getApplicationContext(), AdminGUIActivity.class));

            return true;
        } else if (id == R.id.menu_NavHajjGuide){

            //continue as pilgrim
            startActivity(new Intent(getApplicationContext(), HajjGuideGUIActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_Admin_profile){

            getSupportFragmentManager().beginTransaction().replace(R.id.content_nav, new AdminProfileFragment()).commit();
            getSupportActionBar().setTitle("Admin");

        } else if (id == R.id.nav_Pilgrim_profile){
            //getSupportFragmentManager().beginTransaction().replace(R.id.content_nav, new AdminPilgrimFragment()).commit();
            //getSupportActionBar().setTitle("Pilgrim Directory");
            getSupportFragmentManager().beginTransaction().replace(R.id.content_nav, new PilgrimStatusFragment()).commit();
            getSupportActionBar().setTitle("My Status");

        } else if (id == R.id.nav_hajjguid_list){



        } else if (id == R.id.nav_Schedule) {

            getSupportFragmentManager().beginTransaction().replace(R.id.content_nav, new ScheduleOfActivitiesFragment()).commit();
            getSupportActionBar().setTitle("Schedule of Activities");

        } else if (id == R.id.nav_HajjGuideProfile){



        } else if (id == R.id.nav_HajjInfo) {

            getSupportFragmentManager().beginTransaction().replace(R.id.content_nav, new HajjInfoFragment()).commit();
            getSupportActionBar().setTitle("Hajj Info");

        } else if (id == R.id.nav_Extras) {

            getSupportFragmentManager().beginTransaction().replace(R.id.content_nav, new ExtrasFragment()).commit();
            getSupportActionBar().setTitle("Extras");

        } else if (id == R.id.nav_AboutUS) {

            getSupportFragmentManager().beginTransaction().replace(R.id.content_nav, new AboutUsFragment()).commit();
            getSupportActionBar().setTitle("About Us");

        } else if (id == R.id.nav_FAQ) {

            getSupportFragmentManager().beginTransaction().replace(R.id.content_nav, new FAQFragment()).commit();
            getSupportActionBar().setTitle("FAQ");

        } else if (id == R.id.nav_LogOut){

            if (mCurrentUser != null){
                mAuth.signOut();
            }
            Intent intent = new Intent(NavigationActivity.this, StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
