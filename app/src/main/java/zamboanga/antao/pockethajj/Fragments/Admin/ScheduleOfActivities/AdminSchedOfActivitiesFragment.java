package zamboanga.antao.pockethajj.Fragments.Admin.ScheduleOfActivities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import zamboanga.antao.pockethajj.DataUsers.DataScheduleOfActivities;
import zamboanga.antao.pockethajj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminSchedOfActivitiesFragment extends Fragment {

    private RecyclerView mSchedRecyclerview;
    private DatabaseReference mSchedDatabase;
    private FirebaseAuth mAuth;
    private ListView schedListview;

    private ArrayList<String> schedListArray = new ArrayList<>();



    public AdminSchedOfActivitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_sched_of_activities, container, false);

        mSchedDatabase = FirebaseDatabase.getInstance().getReference().child("ScheduleOfActivities").push();

//        mSchedRecyclerview = (RecyclerView) view.findViewById(R.id.sched_list);
//        mSchedRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        schedListview = (ListView)view.findViewById(R.id.listview_sched);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.sched_list_layout, schedListArray);

        schedListview.setAdapter(arrayAdapter);

        mSchedDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String value = dataSnapshot.getValue(String.class);
                schedListArray.add(value);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    public static class schedListViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public schedListViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setTitle (String title){
            TextView schedTitle = (TextView) mView.findViewById(R.id.etSchedTitle);
            schedTitle.setText(title);
        }

        public void setContent (String content){
            TextView schedContent = (TextView) mView.findViewById(R.id.etSchedContent);
            schedContent.setText(content);
        }

        public void setDate (String date){
            TextView schedDate = (TextView) mView.findViewById(R.id.etSchedDate);
            schedDate.setText(date);
        }

        public void setTime (String time){
            TextView schedTime = (TextView) mView.findViewById(R.id.etSchedTime);
            schedTime.setText(time);
        }

    }

}
