package zamboanga.antao.pockethajj.Payments;


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
import android.widget.Toast;

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
import zamboanga.antao.pockethajj.DataUsers.DataPayment;
import zamboanga.antao.pockethajj.DataUsers.DataUsers;
import zamboanga.antao.pockethajj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApprovedPaymentsFragment extends Fragment {

    private RecyclerView mViewListApprovedRecyclerview;

    private DatabaseReference mApprovedPaymentsDatabase;

    private DatabaseReference mDatabaseUser;

    public ApprovedPaymentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_approved_payments, container, false);

        mViewListApprovedRecyclerview = (RecyclerView) view.findViewById(R.id.viewApprovedRecyclerview);

        mApprovedPaymentsDatabase = FirebaseDatabase.getInstance().getReference().child("ApprovedPayments");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");

        mViewListApprovedRecyclerview.setHasFixedSize(true);
        mViewListApprovedRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<DataUsers, approvedPaymentsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataUsers, approvedPaymentsViewHolder>(

                        DataUsers.class,
                        R.layout.users_completedata_layout,
                        approvedPaymentsViewHolder.class,
                        mApprovedPaymentsDatabase

                ) {
            @Override
            protected void populateViewHolder(final approvedPaymentsViewHolder viewHolder, DataUsers model, int position) {

                final String list_user_id = getRef(position).getKey();

                mDatabaseUser.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){

                            String lastname = dataSnapshot.child("lastname").getValue().toString();
                            String firstname = dataSnapshot.child("firstname").getValue().toString();
                            String thumbimage = dataSnapshot.child("thumbimage").getValue().toString();
                            String accounttype = dataSnapshot.child("accounttype").getValue().toString();

                            viewHolder.setLastName(lastname);
                            viewHolder.setFirstName(firstname);
                            viewHolder.setAccountType(accounttype);
                            viewHolder.setThumbimage(thumbimage, getContext());

                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intentViewListApproved = new Intent(getContext(), ViewListOfApprovedPaymentsActivity.class);
                                    intentViewListApproved.putExtra("user_id", list_user_id);
                                    startActivity(intentViewListApproved);

                                }
                            });

                            notifyPilgrim();

                        } else {
                            Toast.makeText(getContext(), dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        mViewListApprovedRecyclerview.setAdapter(firebaseRecyclerAdapter);

    }

    private void notifyPilgrim() {

        //long countchildren = dataSnapshot.getChildrenCount();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
        Intent intent = new Intent(getActivity(), AdminGUIActivity.class);
        intent.putExtra("notification", "unread");
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.drawable.ic_launcher_pockethajj);
        mBuilder.setContentTitle("Pocket Hajj Notification");
        mBuilder.setContentText("Update on Payments");
        mBuilder.setPriority(Notification.PRIORITY_MAX);

        long[] vibrate = {0, 100, 200, 300};
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
        mBuilder.setVibrate(vibrate);
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());

    }

    public static class approvedPaymentsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public approvedPaymentsViewHolder(View itemView) {

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