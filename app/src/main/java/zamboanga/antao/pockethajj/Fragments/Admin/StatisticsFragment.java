package zamboanga.antao.pockethajj.Fragments.Admin;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import zamboanga.antao.pockethajj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment {

    private TextView tvTotalNumOfUsers;
    private TextView tvTotalNumOfPilgrims;
    private TextView tvTotalNumOfHajjGuides;

    private DatabaseReference mUsersDatabase;

    PieChart pieChart ;
    ArrayList<Entry> entries ;
    ArrayList<String> PieEntryLabels ;
    PieDataSet pieDataSet ;
    PieData pieData ;
    private int count = 1;

    private long numberOfPilgrims;
    private long numberOfHajjGuides;

    private ProgressDialog mProgressDialog;


    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        //progress Bar
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setTitle("Loading Profile Data");
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        tvTotalNumOfUsers = (TextView) view.findViewById(R.id.tvTotalNumberOfUsers);
        tvTotalNumOfPilgrims = (TextView) view.findViewById(R.id.tvTotalNumberOfPilgrims);
        tvTotalNumOfHajjGuides = (TextView) view.findViewById(R.id.tvTotalNumberOfHajjGuides);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);


        mUsersDatabase.orderByChild("regyear").equalTo("pilgrim2017").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    numberOfPilgrims = dataSnapshot.getChildrenCount();

                    tvTotalNumOfPilgrims.setText("Total Number of Pilgrims : " + numberOfPilgrims);
                } else {
                    Toast.makeText(getContext(), dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mUsersDatabase.orderByChild("regyear").equalTo("hajjguide2017").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    numberOfHajjGuides = dataSnapshot.getChildrenCount();

                    tvTotalNumOfHajjGuides.setText("Total Number of HajjGuide : " + numberOfHajjGuides);
                } else {
                    Toast.makeText(getContext(), dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /**
         * load pie chart
         */
        long[] value = {numberOfHajjGuides, numberOfPilgrims};
        String label [] = {"HajjGuide", "Pilgrims"};

        List<PieEntry> pieEntries = new ArrayList<>();

        for (int i = 0; i < label.length; i++){
            pieEntries.add(new PieEntry(value[i], label[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Number of Users");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        PieChart chart = (PieChart)view.findViewById(R.id.chart1);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();

        mProgressDialog.dismiss();

        return view;
    }
}
