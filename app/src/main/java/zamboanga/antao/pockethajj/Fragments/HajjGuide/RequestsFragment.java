package zamboanga.antao.pockethajj.Fragments.HajjGuide;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import zamboanga.antao.pockethajj.DataUsers.DataRequest;
import zamboanga.antao.pockethajj.ProfileView.SelectHajjGuideActivity;
import zamboanga.antao.pockethajj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    private RecyclerView pilgrims_request_recyclerview;

    private DatabaseReference mRequestDatabase;

    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;



    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        pilgrims_request_recyclerview = (RecyclerView) view.findViewById(R.id.pilgrims_request_recyclerview);

        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req").child(mCurrent_user_id);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mRequestDatabase.keepSynced(true);
        mUsersDatabase.keepSynced(true);

        pilgrims_request_recyclerview.setHasFixedSize(true);
        pilgrims_request_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter <DataRequest, RequestsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataRequest, RequestsViewHolder>(

                        DataRequest.class,
                        R.layout.users_completedata_layout,
                        RequestsViewHolder.class,
                        mRequestDatabase

                ) {
                    @Override
                    protected void populateViewHolder(final RequestsViewHolder viewHolder, DataRequest model, final int position) {

                        viewHolder.setRequest_type(model.getRequest_type());

                        String list_user_id = getRef(position).getKey();

                        mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String lastname = dataSnapshot.child("lastname").getValue().toString();
                                String firstname = dataSnapshot.child("firstname").getValue().toString();
                                String thumbimage = dataSnapshot.child("thumbimage").getValue().toString();

                                viewHolder.setLastName(lastname);
                                viewHolder.setFirstName(firstname);
                                viewHolder.setThumbimage(thumbimage, getContext());

                                final String user_id = getRef(position).getKey();

                                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent profileIntent = new Intent(getContext(), SelectHajjGuideActivity.class);
                                        profileIntent.putExtra("user_id", user_id);
                                        startActivity(profileIntent);
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                };

            pilgrims_request_recyclerview.setAdapter(firebaseRecyclerAdapter);

    }

    public static class RequestsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public RequestsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setRequest_type (String request_type){
            TextView mAccountType = (TextView) mView.findViewById(R.id.user_accounttype);
            //mAccountType.setText(request_type);
            mAccountType.setText("Wants to Join You");
        }

        public void setLastName(String lastName){

            TextView mLastName = (TextView) mView.findViewById(R.id.user_lastname);
            mLastName.setText(lastName);

        }

        public void setFirstName (String firstName){

            TextView mFirstName = (TextView) mView.findViewById(R.id.user_firstname);
            mFirstName.setText(firstName);

        }

        public void setThumbimage (String thumbimage, Context context){
            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            Picasso.with(context).load(thumbimage).placeholder(R.drawable.img_profile_img).into(userImageView);
        }

    }
}
