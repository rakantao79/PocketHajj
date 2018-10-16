package zamboanga.antao.pockethajj.AnnualReport;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import zamboanga.antao.pockethajj.NavigationActivity;
import zamboanga.antao.pockethajj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Year2016Fragment extends Fragment {

    private long numberOfPilgrims2016;
    private long numberOfHajjGuides2016;
    private DatabaseReference mUsersDatabase2016;

    private TextView mHajjGuide2016;
    private TextView mPilgrim2016;

    private String numPil2016;
    private String numHajjGuide2016;

    private ProgressDialog mProgressDialog;

    public Year2016Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_year2016, container, false);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setTitle("Loading Profile Data");
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        mHajjGuide2016 = (TextView) view.findViewById(R.id.tvHajjGuide2016);
        mPilgrim2016 = (TextView) view.findViewById(R.id.tvPilgrim2016);

        mUsersDatabase2016 = FirebaseDatabase.getInstance().getReference().child("Users");
        //mUsersDatabase2016.keepSynced(true);

        mUsersDatabase2016.orderByChild("regyear").equalTo("pilgrim2016").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    numberOfPilgrims2016 = dataSnapshot.getChildrenCount();
                    mPilgrim2016.setText("Number of Pilgrims 2016 : " + numberOfPilgrims2016);

//                    long countchildren = dataSnapshot.getChildrenCount();
//
//                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
//                    Intent intent = new Intent(getActivity(), NavigationActivity.class);
//                    intent.putExtra("notification", "unread");
//                    PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);
//
//                    mBuilder.setContentIntent(pendingIntent);
//
//                    mBuilder.setSmallIcon(R.drawable.ic_launcher_pockethajj);
//                    mBuilder.setContentTitle("You have received a notification");
//                    mBuilder.setContentText("You have "+(int) countchildren+" unread report(s)");
//                    mBuilder.setPriority(Notification.PRIORITY_MAX);
//
//                    long[] vibrate = {0, 100, 200, 300};
//                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                    mBuilder.setSound(alarmSound);
//                    mBuilder.setVibrate(vibrate);
//                    NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//
//                    mNotificationManager.notify(001, mBuilder.build());

                } else {
                    Toast.makeText(getContext(), dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mUsersDatabase2016.orderByChild("regyear").equalTo("hajjguide2016").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numberOfHajjGuides2016 = dataSnapshot.getChildrenCount();
                mHajjGuide2016.setText("Number of Hajj Guide 2016 : "+ numberOfHajjGuides2016);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        long value [] = {numberOfHajjGuides2016, numberOfPilgrims2016};
        String label [] = {"HajjGuide", "Pilgrim"};

        List<PieEntry> pieEntries = new ArrayList<>();

        for (int i = 0; i < label.length; i++){
            pieEntries.add(new PieEntry(value[i], label[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Number of Users");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        PieChart chart = (PieChart)view.findViewById(R.id.chart2016);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();

        mProgressDialog.dismiss();
        return view;
    }

    private void notifydatachanged() {



    }
}

