package zamboanga.antao.pockethajj.Payments;


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
import android.widget.ImageView;
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
import zamboanga.antao.pockethajj.DataUsers.DataPayment;
import zamboanga.antao.pockethajj.DataUsers.DataUsers;
import zamboanga.antao.pockethajj.PilgrimUpdate.UpdateRequirementsPilgrim;
import zamboanga.antao.pockethajj.PilgrimUpdate.ViewPendingPaymentActivity;
import zamboanga.antao.pockethajj.ProfileView.SelectHajjGuideActivity;
import zamboanga.antao.pockethajj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingPaymentsFragment extends Fragment {

    private RecyclerView mViewPilgrimPendingPayments;

    private DatabaseReference mViewPendingDatabase;

    private DatabaseReference mDatabaseUser;


    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    public PendingPaymentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pending_payments, container, false);

        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();


        mViewPilgrimPendingPayments = (RecyclerView) view.findViewById(R.id.pendingPaymentsRecyclerView);

        mViewPendingDatabase = FirebaseDatabase.getInstance().getReference().child("PendingPayments");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");

        mViewPilgrimPendingPayments.setHasFixedSize(true);
        mViewPilgrimPendingPayments.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<DataUsers, pendingPaymentsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataUsers, pendingPaymentsViewHolder>(

                        DataUsers.class,
                        R.layout.users_completedata_layout,
                        pendingPaymentsViewHolder.class,
                        mViewPendingDatabase

                ) {
            @Override
            protected void populateViewHolder(final pendingPaymentsViewHolder viewHolder, DataUsers model, int position) {


                final String list_user_id = getRef(position).getKey();

                mDatabaseUser.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String lastname = dataSnapshot.child("lastname").getValue().toString();
                        String firstname = dataSnapshot.child("firstname").getValue().toString();
                        String thumbimage = dataSnapshot.child("thumbimage").getValue().toString();
                        String accounttype = dataSnapshot.child("accounttype").getValue().toString();

//                        viewHolder.setLastName(lastname);
//                        viewHolder.setFirstName(firstname);
//                        viewHolder.setThumbimage(thumbimage, getContext());

                        viewHolder.setLastName(lastname);
                        viewHolder.setFirstName(firstname);
                        viewHolder.setAccountType(accounttype);
                        viewHolder.setThumbimage(thumbimage, getContext());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //view List of pending payments
                                Intent intentViewListOfPendingPayments = new Intent(getContext(), ViewListOfPendingPaymentsActivity.class);
                                intentViewListOfPendingPayments.putExtra("user_id", list_user_id);
                                startActivity(intentViewListOfPendingPayments);
                                getActivity().finish();

//                                Intent intentViewListOfPendingPayments = new Intent(getContext(), ViewPendingPaymentActivity.class);
//                                intentViewListOfPendingPayments.putExtra("user_id", list_user_id);
//                                startActivity(intentViewListOfPendingPayments);

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        mViewPilgrimPendingPayments.setAdapter(firebaseRecyclerAdapter);

    }

    public static class pendingPaymentsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public pendingPaymentsViewHolder(View itemView) {

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
