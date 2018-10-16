package zamboanga.antao.pockethajj.Fragments;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import zamboanga.antao.pockethajj.AdminGUIActivity;
import zamboanga.antao.pockethajj.DataUsers.DataScheduleOfActivities;
import zamboanga.antao.pockethajj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleOfActivitiesFragment extends Fragment {

    private DatabaseReference mSchedDatabase;

    private RecyclerView mSchedRecyclerView;



    public ScheduleOfActivitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule_of_activities, container, false);

        mSchedRecyclerView = (RecyclerView)view.findViewById(R.id.schedRecyclerView);
        mSchedDatabase = FirebaseDatabase.getInstance().getReference().child("ScheduleOfActivities");
        mSchedDatabase.keepSynced(true);

        mSchedRecyclerView.setHasFixedSize(true);
        mSchedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<DataScheduleOfActivities, schedOfActivitiesViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataScheduleOfActivities, schedOfActivitiesViewHolder>(

                        DataScheduleOfActivities.class,
                        R.layout.user_sched_layout,
                        schedOfActivitiesViewHolder.class,
                        mSchedDatabase

                ) {
            @Override
            protected void populateViewHolder(schedOfActivitiesViewHolder viewHolder,
                                              DataScheduleOfActivities model, int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setContent(model.getContent());
                viewHolder.setDate(model.getDate());
                viewHolder.setTime(model.getTime());

                mSchedDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
                            Intent intent = new Intent(getActivity(), AdminGUIActivity.class);
                            intent.putExtra("notification", "unread");
                            PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

                            mBuilder.setContentIntent(pendingIntent);

                            mBuilder.setSmallIcon(R.drawable.ic_launcher_pockethajj);
                            mBuilder.setContentTitle("Pocket Hajj Notification");
                            mBuilder.setContentText("Pilgrim Registered");
                            mBuilder.setPriority(Notification.PRIORITY_MAX);

                            long[] vibrate = {0, 100, 200, 300};
                            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            mBuilder.setSound(alarmSound);
                            mBuilder.setVibrate(vibrate);
                            NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

                            mNotificationManager.notify(001, mBuilder.build());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        mSchedRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class schedOfActivitiesViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public schedOfActivitiesViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setTitle (String title){
            TextView usertitle = (TextView)mView.findViewById(R.id.user_SchedTitle);
            usertitle.setText(title);
        }
        public void setContent (String content){
            TextView usercontent = (TextView)mView.findViewById(R.id.user_SchedContent);
            usercontent.setText(content);
        }
        public void setDate (String date) {
            TextView userdate = (TextView)mView.findViewById(R.id.user_SchedDate);
            userdate.setText(date);
        }
        public void setTime (String time) {
            TextView userTime = (TextView)mView.findViewById(R.id.user_SchedTime);
            userTime.setText(time);
        }
    }
}
