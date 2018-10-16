package zamboanga.antao.pockethajj.ProfileView;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import zamboanga.antao.pockethajj.R;

public class ProfileHajjGuideActivity extends AppCompatActivity {


    private Toolbar mToolbar;

    private CircleImageView mProfileImage;
    private TextView mProfileLastName;
    private TextView mProfileFirstName;
    private RatingBar mRatingBar;

    //database reference for loading the profile data
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private DatabaseReference mRootRef;

    private FirebaseUser mCurrent_user;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_hajj_guide);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_hajjguiprofile);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //put extra USER ID From AdminHajjGuideFragment
        final String user_id = getIntent().getStringExtra("user_id");

        mAuth = FirebaseAuth.getInstance();

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();


        //field
        mProfileImage = (CircleImageView) findViewById(R.id.imgHajjGuideProfileImage);
        mProfileLastName = (TextView) findViewById(R.id.tvHajjLastName);
        mProfileFirstName = (TextView)findViewById(R.id.tvHajjFirstName);

        //progress Bar
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading Profile Data");
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String lastname = dataSnapshot.child("lastname").getValue().toString();
                String firstname = dataSnapshot.child("firstname").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                mProfileLastName.setText(lastname);
                mProfileFirstName.setText(firstname);

                Picasso.with(ProfileHajjGuideActivity.this).load(image).placeholder(R.drawable.img_profile_img).into(mProfileImage);

                mProgressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(ProfileHajjGuideActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
