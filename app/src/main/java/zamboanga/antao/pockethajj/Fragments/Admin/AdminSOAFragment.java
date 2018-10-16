package zamboanga.antao.pockethajj.Fragments.Admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zamboanga.antao.pockethajj.DataSchedule.AdapterSched;
import zamboanga.antao.pockethajj.DataUsers.DataScheduleOfActivities;
import zamboanga.antao.pockethajj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminSOAFragment extends Fragment {

    private DatabaseReference mSOADatabase;

    private RecyclerView mSOARecyclerview;

    private AdapterSched adapterSched;

    public AdminSOAFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_soa, container, false);

        mSOADatabase = FirebaseDatabase.getInstance().getReference();

        final List<DataScheduleOfActivities> dataScheduleOfActivities = new ArrayList<>();

        mSOARecyclerview = (RecyclerView) view.findViewById(R.id.SOArecyclerview);

        adapterSched = new AdapterSched(dataScheduleOfActivities, getActivity());

        //mSOARecyclerview.setAdapter(adapterSched);

        mSOARecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        fill_with_data();


        return view;
    }

    private void fill_with_data() {

        mSOADatabase.child("ScheduleOfActivities").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    List<DataScheduleOfActivities> list = new ArrayList<DataScheduleOfActivities>();
                    for (DataSnapshot dataSOA : dataSnapshot.getChildren()){
                        DataScheduleOfActivities data = dataSOA.getValue(DataScheduleOfActivities.class);
                        list.add(data);
                    }
                    Collections.reverse(list);
                    for (DataScheduleOfActivities data : list){
                        adapterSched.insert(new DataScheduleOfActivities( data.title, data.content, data.date, data.time));
                    }

                } else {
                    Toast.makeText(getContext(), "NO Data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSOARecyclerview.setAdapter(adapterSched);

    }

}
