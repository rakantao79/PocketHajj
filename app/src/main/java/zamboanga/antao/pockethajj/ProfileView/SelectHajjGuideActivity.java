package zamboanga.antao.pockethajj.ProfileView;

import android.app.ProgressDialog;
import android.icu.text.DateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.security.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import zamboanga.antao.pockethajj.PilgrimUpdate.PilgrimProfileSettings;
import zamboanga.antao.pockethajj.R;

public class SelectHajjGuideActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private CircleImageView mSelectHajjGuideImage;
    private TextView mSelectHajjGuideLastName;
    private TextView mSelectHajjGuideFirstName;
    private Button btnSelectHajjGuide;
    private Button btnDeclineRequest;
    private RatingBar mRatingBar;

    //database reference for loading the profile data
    private DatabaseReference mUsersDatabase;

    //database for friend request
    private DatabaseReference mFriendReqDatabase;

    //database for accepted friends
    private DatabaseReference mFriendDatabase;

    //current state of hajjguide and pilgrim
    private String mCurrent_state;

    private FirebaseAuth mAuth;

    private DatabaseReference mRootRef;

    private FirebaseUser mCurrent_user;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_hajj_guide);

        //toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar_select_hajj_guide);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String user_id = getIntent().getStringExtra("user_id");

        //root database
        mRootRef = FirebaseDatabase.getInstance().getReference();

        //main table reference users
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        //database for request
        //when request is accepted database will remove the queried data
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");

        mUsersDatabase.keepSynced(true);
        mFriendDatabase.keepSynced(true);
        mFriendReqDatabase.keepSynced(true);


        //current user id
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();

        //current state
        mCurrent_state = "not_friends";

        //fields
        mSelectHajjGuideImage = (CircleImageView)findViewById(R.id.imgSelectHajjGuide);
        mSelectHajjGuideLastName = (TextView)findViewById(R.id.tvSelectHajjLastName);
        mSelectHajjGuideFirstName = (TextView)findViewById(R.id.tvSelectHajjFirstName);
        btnSelectHajjGuide = (Button) findViewById(R.id.btnSelectHajjGuide) ;
        btnDeclineRequest = (Button) findViewById(R.id.btnDeclineRequest);


        //progress Bar
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading Profile Data");
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        //disable and hide some buttons
        btnDeclineRequest.setVisibility(View.INVISIBLE);
        btnDeclineRequest.setEnabled(false);

        //load hajj guide information
        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String lastname = dataSnapshot.child("lastname").getValue().toString();
                String firstname = dataSnapshot.child("firstname").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();

                mSelectHajjGuideFirstName.setText(firstname);
                mSelectHajjGuideLastName.setText(lastname);


                if (!image.equals("default")){
                    Picasso.with(SelectHajjGuideActivity.this)
                            .load(image)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.img_profile_img)
                            .into(mSelectHajjGuideImage, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(SelectHajjGuideActivity.this)
                                            .load(image)
                                            .placeholder(R.drawable.img_profile_img)
                                            .into(mSelectHajjGuideImage);
                                }
                            });
                }

                // ======================== Friends List =========================//

                mFriendReqDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (dataSnapshot.hasChild(user_id)){

                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();

                            if (req_type.equals("received")){


                                mCurrent_state = "req_received";
                                btnSelectHajjGuide.setText("Accept Join Request");

                                btnDeclineRequest.setVisibility(View.VISIBLE);
                                btnDeclineRequest.setEnabled(true);

                                btnDeclineRequest.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mFriendReqDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        mFriendReqDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                        btnSelectHajjGuide.setEnabled(true);
                                                                        mCurrent_state = "not_friends";
                                                                        btnSelectHajjGuide.setText("Send Join Request");

                                                                        btnDeclineRequest.setVisibility(View.INVISIBLE);
                                                                        btnDeclineRequest.setEnabled(false);

                                                                    }
                                                                });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                                    }
                                });

                            } else if (req_type.equals("sent")){

                                mCurrent_state = "req_sent";
                                btnSelectHajjGuide.setText("Cancel Friend Request");

                                btnDeclineRequest.setVisibility(View.INVISIBLE);
                                btnDeclineRequest.setEnabled(false);

                            }

                            mProgressDialog.dismiss();

                        } else {
                            mFriendDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.hasChild(user_id)){

                                        mCurrent_state = "friends";
                                        btnSelectHajjGuide.setText("Unfriend this Person");

                                        btnDeclineRequest.setVisibility(View.INVISIBLE);
                                        btnDeclineRequest.setEnabled(false);

                                    }
                                    mProgressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    mProgressDialog.dismiss();
                                }
                            });
                        }

                        //mProgressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                // ======================== Friends List =========================//

                //mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnSelectHajjGuide.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                btnSelectHajjGuide.setEnabled(false);

                // ======================== not friends =========================//

                if (mCurrent_state.equals("not_friends")){

                    mFriendReqDatabase.child(mCurrent_user.getUid()).child(user_id).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                //Toast.makeText(SelectHajjGuideActivity.this, "", Toast.LENGTH_SHORT).show();

                                mFriendReqDatabase.child(user_id).child(mCurrent_user.getUid()).child("request_type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        //btnSelectHajjGuide.setEnabled(true);
                                        mCurrent_state = "req_sent";
                                        btnSelectHajjGuide.setText("Cancel Join Request");

                                        btnDeclineRequest.setVisibility(View.INVISIBLE);
                                        btnDeclineRequest.setEnabled(false);

                                        //Toast.makeText(SelectHajjGuideActivity.this, "Request Successful", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SelectHajjGuideActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                Toast.makeText(SelectHajjGuideActivity.this, "Failed Sending Request", Toast.LENGTH_SHORT).show();
                            }

                            btnSelectHajjGuide.setEnabled(true);
                            mCurrent_state = "req_sent";
                            btnSelectHajjGuide.setText("Cancel Join Request");
                        }
                    });
                }
                // ======================== not friends =========================//

                // ======================== Cancel Request State ================//
                if (mCurrent_state.equals("req_sent")){
                    mFriendReqDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendReqDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    btnSelectHajjGuide.setEnabled(true);
                                    mCurrent_state = "not_friends";
                                    btnSelectHajjGuide.setText("Send Join Request");

                                    btnDeclineRequest.setVisibility(View.INVISIBLE);
                                    btnDeclineRequest.setEnabled(false);

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
                // ======================== Cancel Request State ==================//

                // ======================== Request Recieved state ================//

                if (mCurrent_state.equals("req_received")){

                    //you can also user server.timestamp to get time from firebase
                    //
                    //final String currentDate;
                    //currentDate = DateFormat.getDateTimeInstance().format(new Date());

                    mFriendDatabase.child(mCurrent_user.getUid()).child(user_id).child("date").setValue("friends")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mFriendDatabase.child(user_id).child(mCurrent_user.getUid()).child("date").setValue("friends")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    //----------- remove value from the friend req table -----------------//

                                    mFriendReqDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    mFriendReqDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    btnSelectHajjGuide.setEnabled(true);
                                                                    mCurrent_state = "friends";
                                                                    btnSelectHajjGuide.setText("Remove from Group");

                                                                    btnDeclineRequest.setVisibility(View.INVISIBLE);
                                                                    btnDeclineRequest.setEnabled(false);

                                                                }
                                                            });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });

                                    //----------- remove value from the friend req table -----------------//
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SelectHajjGuideActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                // ======================== Request Recieved state ================//

                // ======================== Unfriend============== ================//

                if (mCurrent_state.equals("friends")){
                    Map unfriendMap = new HashMap();
                    unfriendMap.put("Friends/" + mCurrent_user.getUid() + "/" + user_id, null);
                    unfriendMap.put("Friends/" + user_id + "/" + mCurrent_user.getUid(), null);


                    mRootRef.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError == null){

                                mCurrent_state = "not_friends";
                                btnSelectHajjGuide.setText("Send Join Request");
                            } else {
                                String error = databaseError.getMessage();
                                Toast.makeText(SelectHajjGuideActivity.this, error, Toast.LENGTH_SHORT).show();

                            }

                            btnSelectHajjGuide.setEnabled(true);

                        }
                    });
                }

                // ======================== Unfriend============== ================//


                // ======================== Decline Join Request===================//



                // ======================== Decline Join Request===================//
            }
        });
    }
}
