package zamboanga.antao.pockethajj.Fragments.HajjGuide;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import zamboanga.antao.pockethajj.DataUsers.DataFriends;
import zamboanga.antao.pockethajj.DataUsers.DataUsers;
import zamboanga.antao.pockethajj.PilgrimUpdate.UpdateRequirementsPilgrim;
import zamboanga.antao.pockethajj.ProfileView.SelectHajjGuideActivity;
import zamboanga.antao.pockethajj.R;

/**
 * This fragments shows the accepted pilgrims
 * A simple {@link Fragment} subclass.
 */
public class MyPIlgrimsFragment extends Fragment {

    private RecyclerView my_pilgrims_recyclerview;


    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mDatabaseUser;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;


    public MyPIlgrimsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_pilgrims, container, false);

        //
        //start your fields here..
        //and functions

        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");

        my_pilgrims_recyclerview = (RecyclerView) view.findViewById(R.id.my_pilgrims_recyclerview);
        my_pilgrims_recyclerview.setHasFixedSize(true);
        my_pilgrims_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<DataFriends, myPilgrimsViewholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataFriends, myPilgrimsViewholder>(

                        DataFriends.class,
                        R.layout.users_completedata_layout,
                        myPilgrimsViewholder.class,
                        mFriendsDatabase

                ) {
                    @Override
                    protected void populateViewHolder(final myPilgrimsViewholder viewHolder, final DataFriends model, int position) {

                        //will get the value of the date in friends database
                        viewHolder.setDate(model.getDate());

                        //retrieve the data of the cardview assigned id
                        final String list_user_id = getRef(position).getKey();

                        mDatabaseUser.child(list_user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String lastname = dataSnapshot.child("lastname").getValue().toString();
                                String firstname = dataSnapshot.child("firstname").getValue().toString();
                                String thumbimage = dataSnapshot.child("thumbimage").getValue().toString();

                                viewHolder.setLastName(lastname);
                                viewHolder.setFirstName(firstname);
                                viewHolder.setThumbimage(thumbimage, getContext());

                                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CharSequence options[] = new CharSequence[]{"Open Profile","Update Pilgrim Requirements"};
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Select Options");

                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                if (which == 0){
                                                    Intent profileIntent = new Intent(getContext(), SelectHajjGuideActivity.class);
                                                    profileIntent.putExtra("user_id", list_user_id);
                                                    startActivity(profileIntent);
                                                }

                                                if (which == 1){
                                                    Intent updateRequirementIntent = new Intent(getContext(), UpdateRequirementsPilgrim.class);
                                                    updateRequirementIntent.putExtra("user_id", list_user_id);
                                                    startActivity(updateRequirementIntent);

                                                }
                                            }
                                        });
                                        builder.show();
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                };
                my_pilgrims_recyclerview.setAdapter(firebaseRecyclerAdapter);
    }

    public static class myPilgrimsViewholder extends RecyclerView.ViewHolder {

        View mView;

        public myPilgrimsViewholder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDate (String date){
            TextView mAccountType = (TextView) mView.findViewById(R.id.user_accounttype);
            mAccountType.setText(date);
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
