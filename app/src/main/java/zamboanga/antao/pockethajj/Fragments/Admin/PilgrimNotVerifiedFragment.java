package zamboanga.antao.pockethajj.Fragments.Admin;


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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import zamboanga.antao.pockethajj.AdminGUIActivity;
import zamboanga.antao.pockethajj.DataUsers.DataUsers;
import zamboanga.antao.pockethajj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PilgrimNotVerifiedFragment extends Fragment {

    private RecyclerView recycler_not_verified;
    private DatabaseReference DatabaseNotVerified;
    private FirebaseAuth mAuth;
    private String mCurrent_user_id;

    public PilgrimNotVerifiedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pilgrim_not_verified, container, false);

        recycler_not_verified = (RecyclerView) view.findViewById(R.id.recycler_Not_verified);

        recycler_not_verified.setHasFixedSize(true);
        recycler_not_verified.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();
        DatabaseNotVerified = FirebaseDatabase.getInstance().getReference().child("Users");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<DataUsers, notVerifiedViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataUsers, notVerifiedViewHolder>(

                        DataUsers.class,
                        R.layout.users_completedata_layout,
                        notVerifiedViewHolder.class,
                        DatabaseNotVerified.orderByChild("accountstatus").equalTo("Not Verified")

                ) {
            @Override
            protected void populateViewHolder(notVerifiedViewHolder viewHolder, DataUsers model, int position) {

                viewHolder.setLastName(model.getLastname());
                viewHolder.setFirstName(model.getFirstname());
                viewHolder.setAccountType(model.getAccounttype());
                viewHolder.setThumbimage(model.getThumbimage(), getContext());

                final String list_user_id = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent verifyPilgrimIntent = new Intent(getContext(), VerifyProfileActivity.class);
                        verifyPilgrimIntent.putExtra("user_id", list_user_id);
                        startActivity(verifyPilgrimIntent);

                        DatabaseNotVerified.orderByChild("accountstatus").equalTo("Not Verified").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()){

                                    long countchildren = dataSnapshot.getChildrenCount();

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
                });
            }
        };
        recycler_not_verified.setAdapter(firebaseRecyclerAdapter);
    }

    public static class notVerifiedViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public notVerifiedViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setLastName(String lastName){

            TextView mLastName = (TextView) mView.findViewById(R.id.user_lastname);
            mLastName.setText(lastName);

        }

        public void setFirstName (String firstName){

            TextView mFirstName = (TextView) mView.findViewById(R.id.user_firstname);
            mFirstName.setText(firstName);

        }

        public void setAccountType (String accountType){

            TextView mAccountType = (TextView) mView.findViewById(R.id.user_accounttype);
            mAccountType.setText(accountType);

        }

        public void setThumbimage (String thumbimage, Context context){
            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            Picasso.with(context).load(thumbimage).placeholder(R.drawable.img_profile_img).into(userImageView);
        }

    }

}
