package zamboanga.antao.pockethajj.Fragments.Admin;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import zamboanga.antao.pockethajj.DataUsers.DataUsers;
import zamboanga.antao.pockethajj.ProfileView.ProfileHajjGuideActivity;
import zamboanga.antao.pockethajj.R;
import zamboanga.antao.pockethajj.RegisterHajjGuideActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminHajjGuideFragment extends Fragment {

    private FloatingActionButton fabAddHajjGuide;

    private RecyclerView mHajjGuideRecyclerView;

    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;



    public AdminHajjGuideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_hajj_guide, container, false);

        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();


        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mHajjGuideRecyclerView = (RecyclerView) view.findViewById(R.id.hajjGuideRecyclerView) ;
        mHajjGuideRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fabAddHajjGuide = (FloatingActionButton) view.findViewById(R.id.fab_AddHajjGuide);

        fabAddHajjGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RegisterHajjGuideActivity.class));
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<DataUsers, hajjGuideViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataUsers, hajjGuideViewHolder>(

                        DataUsers.class,
                        R.layout.users_completedata_layout,
                        hajjGuideViewHolder.class,
                        mUsersDatabase.orderByChild("accounttype").equalTo("hajjguide")

                ) {
            @Override
            protected void populateViewHolder(hajjGuideViewHolder viewHolder, DataUsers model, int position) {

                viewHolder.setLastName(model.getLastname());
                viewHolder.setFirstName(model.getFirstname());
                viewHolder.setAccountType(model.getAccounttype());
                viewHolder.setThumbimage(model.getThumbimage(), getContext());


                //to detaile view of Hajj Profile
                final String user_id = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent = new Intent(getContext(), ProfileHajjGuideActivity.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);


                    }
                });
            }
        };

        mHajjGuideRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }
    public static class hajjGuideViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public hajjGuideViewHolder(View itemView) {
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
