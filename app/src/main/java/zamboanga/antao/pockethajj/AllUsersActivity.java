package zamboanga.antao.pockethajj;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import zamboanga.antao.pockethajj.DataUsers.DataUsers;

public class AllUsersActivity extends AppCompatActivity {


    private RecyclerView mAllUserRecyclerView;

    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private CircleImageView imgUsersProfImage;

    private Toolbar mToolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_all_users);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        imgUsersProfImage = (CircleImageView) findViewById(R.id.user_single_image);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);

        mAllUserRecyclerView = (RecyclerView) findViewById(R.id.allUserRecyclerview);
        mAllUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<DataUsers, UsersViewholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataUsers, UsersViewholder>(

                        DataUsers.class,
                        R.layout.users_completedata_layout,
                        UsersViewholder.class,
                        mUsersDatabase//.orderByChild("accounttype").equalTo("pilgrim")

                ) {
            @Override
            protected void populateViewHolder(final UsersViewholder viewHolder, DataUsers model, int position) {

                viewHolder.setLastName(model.getLastname());
                viewHolder.setFirstName(model.getFirstname());
                viewHolder.setAccountType(model.getAccounttype());
                viewHolder.setThumbimage(model.getThumbimage(), getApplicationContext());

                final String list_user_id = getRef(position).getKey();

                mUsersDatabase.orderByChild("accounttype").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //System.out.println(dataSnapshot.getChildrenCount());
                        getSupportActionBar().setTitle("Total Number of Users : " + String.valueOf(dataSnapshot.getChildrenCount()));
                        Log.d("Count", String.valueOf(dataSnapshot.getChildrenCount()));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        mAllUserRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class UsersViewholder extends RecyclerView.ViewHolder{

        View mView;

        public UsersViewholder(View itemView) {
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
