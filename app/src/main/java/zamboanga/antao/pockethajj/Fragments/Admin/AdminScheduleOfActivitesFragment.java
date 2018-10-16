package zamboanga.antao.pockethajj.Fragments.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import zamboanga.antao.pockethajj.DataUsers.DataScheduleOfActivities;
import zamboanga.antao.pockethajj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminScheduleOfActivitesFragment extends Fragment {

    private FloatingActionButton fabSOA;

    private RecyclerView mSOARecyclerview;

    private DatabaseReference mSOADatabasel;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    public AdminScheduleOfActivitesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_schedule_of_activites, container, false);

        fabSOA = (FloatingActionButton) view.findViewById(R.id.fabSOA);

        fabSOA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Intent intent = new Intent(getContext(), AddScheduleActivity.class);
                startActivity(intent);

            }
        });

        mSOARecyclerview = (RecyclerView) view.findViewById(R.id.SOArecyclerview);

        mSOADatabasel = FirebaseDatabase.getInstance().getReference().child("ScheduleOfActivities");
        mSOADatabasel.keepSynced(true);

        mSOARecyclerview.setHasFixedSize(true);
        mSOARecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter <DataScheduleOfActivities, soaViewholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataScheduleOfActivities, soaViewholder>(

                        DataScheduleOfActivities.class,
                        R.layout.user_sched_layout,
                        soaViewholder.class,
                        mSOADatabasel

        ) {
            @Override
            protected void populateViewHolder(final soaViewholder viewHolder, final DataScheduleOfActivities model, int position) {

                final String post_key = getRef(position).toString();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setContent(model.getContent());
                viewHolder.setDate(model.getDate());
                viewHolder.setTime(model.getTime());
            }
        };

        mSOARecyclerview.setAdapter(firebaseRecyclerAdapter);
    }

    public static class soaViewholder extends RecyclerView.ViewHolder {

        View mView;

        public soaViewholder(View itemView) {
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
