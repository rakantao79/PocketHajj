package zamboanga.antao.pockethajj;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import zamboanga.antao.pockethajj.DataUsers.DataUsers;
import zamboanga.antao.pockethajj.ProfileView.ProfileHajjGuideActivity;
import zamboanga.antao.pockethajj.ProfileView.SelectHajjGuideActivity;

public class AllHajjGuideActivity extends AppCompatActivity {

    private RecyclerView mAllHajjGuideRecyclerView;

    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    //toolbar
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_hajj_guide);

        //toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar_allHajjGuid);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Hajj Guide Directory");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mAllHajjGuideRecyclerView = (RecyclerView) findViewById(R.id.allHajjGuideRecyclerview);
        mAllHajjGuideRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
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
                viewHolder.setThumbimage(model.getThumbimage(), getApplicationContext());

                final String user_id = getRef(position).getKey();


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent = new Intent(AllHajjGuideActivity.this, SelectHajjGuideActivity.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);
                    }
                });

            }
        };

        mAllHajjGuideRecyclerView.setAdapter(firebaseRecyclerAdapter);

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
