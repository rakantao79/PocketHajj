package zamboanga.antao.pockethajj.Fragments.Admin;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import zamboanga.antao.pockethajj.DataPilgrim.DataPilgrim;
import zamboanga.antao.pockethajj.DataUsers.DataUsers;
import zamboanga.antao.pockethajj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminPilgrimFragment extends Fragment {


    private RecyclerView mPilgrimRecyclerView;

    private DatabaseReference mPilgrimDatabase;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;


    public AdminPilgrimFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_pilgrim, container, false);

        //create model
        mPilgrimRecyclerView  = (RecyclerView) view.findViewById(R.id.admin_pilgrim_recyclerview);

        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mPilgrimDatabase = FirebaseDatabase.getInstance().getReference().child("Pilgrim").child(mCurrent_user_id);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mPilgrimRecyclerView.setHasFixedSize(true);
        mPilgrimRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter <DataUsers, pilgrimViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataUsers, pilgrimViewHolder>(

                        DataUsers.class,
                        R.layout.users_completedata_layout,
                        pilgrimViewHolder.class,
                        mUsersDatabase.orderByChild("accounttype").equalTo("pilgrim")

                ) {
            @Override
            protected void populateViewHolder(pilgrimViewHolder viewHolder, DataUsers model, int position) {

                viewHolder.setLastName(model.getLastname());
                viewHolder.setFirstName(model.getFirstname());
                viewHolder.setAccountType(model.getAccounttype());
                viewHolder.setThumbimage(model.getThumbimage(), getContext());
            }
        };

        mPilgrimRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    public static class pilgrimViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public pilgrimViewHolder(View itemView) {
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
