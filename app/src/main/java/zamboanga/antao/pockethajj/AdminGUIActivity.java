package zamboanga.antao.pockethajj;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import zamboanga.antao.pockethajj.AnnualReport.AnnualReportActivity;
import zamboanga.antao.pockethajj.AnnualReport.AnnualReportSpinnerTabbedActivity;
import zamboanga.antao.pockethajj.AnnualReport.Year2016Fragment;
import zamboanga.antao.pockethajj.Fragments.Admin.AdminHajjGuideFragment;
import zamboanga.antao.pockethajj.Fragments.Admin.AdminPilgrimFragment;
import zamboanga.antao.pockethajj.Fragments.Admin.AdminSOAFragment;
import zamboanga.antao.pockethajj.Fragments.Admin.AdminScheduleOfActivitesFragment;
import zamboanga.antao.pockethajj.Fragments.Admin.PilgrimNotVerifiedFragment;
import zamboanga.antao.pockethajj.Fragments.Admin.ScheduleOfActivities.AdminSchedOfActivitiesFragment;
import zamboanga.antao.pockethajj.Fragments.Admin.StatisticsFragment;

public class AdminGUIActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */



    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_gui);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Admin");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_gui, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_allusers) {
            startActivity(new Intent(AdminGUIActivity.this, AllUsersActivity.class));
        }

        if (id == R.id.action_annual_report){
            startActivity(new Intent(AdminGUIActivity.this, AnnualReportSpinnerTabbedActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_admin_gui, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position){
                case 0:
                    PilgrimNotVerifiedFragment pilgrimNotVerifiedFragment = new PilgrimNotVerifiedFragment();
                    return pilgrimNotVerifiedFragment;
                case 1:
                    AdminHajjGuideFragment adminHajjGuideFragment = new AdminHajjGuideFragment();
                    return adminHajjGuideFragment;
                case 2:
                    AdminPilgrimFragment adminPilgrimFragment = new AdminPilgrimFragment();
                    return adminPilgrimFragment;
                case 3:

                    AdminScheduleOfActivitesFragment adminScheduleOfActivitesFragment = new AdminScheduleOfActivitesFragment();
                    return  adminScheduleOfActivitesFragment;

                case 4:
                    StatisticsFragment statisticsFragment = new StatisticsFragment();
                    return statisticsFragment;
                case 5:
                    Year2016Fragment year2016Fragment = new Year2016Fragment();
                    return year2016Fragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 6 total pages.
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "To be Verified";
                case 1:
                    return "Hajj Guide Directory";
                case 2:
                    return "Pilgrim Directory";
                case 3:
                    return "Schedule of Activities";
                case 4:
                    return "Annual Report 2017";
                case 5:
                    return  "Annual Report 2016";
            }
            return null;
        }
    }
}
