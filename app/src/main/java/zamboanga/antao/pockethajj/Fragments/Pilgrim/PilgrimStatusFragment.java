package zamboanga.antao.pockethajj.Fragments.Pilgrim;


import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import zamboanga.antao.pockethajj.AllHajjGuideActivity;
import zamboanga.antao.pockethajj.PilgrimUpdate.PilgrimProfileSettings;
import zamboanga.antao.pockethajj.PilgrimUpdate.PilgrimUpdateProfileActivity;
import zamboanga.antao.pockethajj.PilgrimUpdate.UpdatePaymentActivity;
import zamboanga.antao.pockethajj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PilgrimStatusFragment extends Fragment{


    //firebase database
    private DatabaseReference mUserDatabase;
    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mApprovedPayments;

    //firebase user id
    private FirebaseUser mCurrentUser;

    //fields
    private TextView tvPilgrimDisplayName;
    private TextView tvPilgrimContactNumber;
    private TextView tvPilgrimLastName;
    private TextView tvPilgrimFirstName;
    private TextView tvPilgrimBiometric;
    private TextView tvPilgrimPassport;
    private TextView tvPilgrimYelloCard;

    private TextView tvPilgrimMiddleName;
    private TextView tvPilgrimGender;
    private TextView tvPilgrimAddress;
    private TextView tvPilgrimCivilStatus;
    private TextView tvPilgrimCitizenship;
    private TextView tvPilgrimAccountStatus;

    private TextView tvSelectedHajjGuide;

    private TextView tvPilgrimPassportExpiry;
    private TextView tvPilgrimYellowCardExpiry;

    private TextView tvRequirementsUpdate;
    private TextView tvPilgrimPaid;
    private TextView tvPilgrimBalance;

    private CircleImageView mPilgrimProfileImage;

    //buttons
    private Button btnUpdatePayment;
    private Button btnPilgrimProfileSettings;
    private Button btnSearchHajjGuide;


    public PilgrimStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pilgrim_status, container, false);


        //database get info of the current user id
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mApprovedPayments = FirebaseDatabase.getInstance().getReference().child("ApprovedPayments").child(current_uid);
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(current_uid);

        //fields
        tvPilgrimDisplayName = (TextView) view.findViewById(R.id.tvPilgrimDisplayName);
        tvPilgrimContactNumber = (TextView) view.findViewById(R.id.tvPilgrimContactNumber);
        tvPilgrimLastName = (TextView) view.findViewById(R.id.tvPIlgrimLastName);
        tvPilgrimFirstName = (TextView) view.findViewById(R.id.tvPilgrimFirstName);

        tvPilgrimBiometric = (TextView) view.findViewById(R.id.tvPilgrimBiometric);
        tvPilgrimPassport = (TextView) view.findViewById(R.id.tvPilgrimPassport);
        tvPilgrimYelloCard = (TextView) view.findViewById(R.id.tvPilgrimYellowCard);

        tvPilgrimMiddleName = (TextView) view.findViewById(R.id.tvPilgrimMiddleName);
        tvPilgrimGender = (TextView) view.findViewById(R.id.tvPilgrimGender);
        tvPilgrimAddress = (TextView) view.findViewById(R.id.tvPIlgrimAddress);
        tvPilgrimCivilStatus = (TextView) view.findViewById(R.id.tvPIlgrimCivilStatus);
        tvPilgrimCitizenship = (TextView) view.findViewById(R.id.tvPilgrimCitizenship);
        tvPilgrimAccountStatus = (TextView) view.findViewById(R.id.tvPilgrimAccountStatus);
        tvSelectedHajjGuide = (TextView) view.findViewById(R.id.tvSelectedHajjGuide);

//        tvPilgrimPassportExpiry = (TextView) view.findViewById(R.id.tvPilgrimPassportExpiry);
//        tvPilgrimYellowCardExpiry = (TextView) view.findViewById(R.id.tvYellowCardExpiry);

//        tvRequirementsUpdate = (TextView) view.findViewById(R.id.tvRequirementsUpdate);
//        tvPilgrimPaid = (TextView) view.findViewById(R.id.tvPilgrimPaid);
//        tvPilgrimBalance = (TextView) view.findViewById(R.id.tvPilgrimBalance);

        //circuleimage
        mPilgrimProfileImage = (CircleImageView) view.findViewById(R.id.circleImageViewPilgrim) ;


        //fields start here
        btnUpdatePayment = (Button) view.findViewById(R.id.btnUpdatePayment);
        btnPilgrimProfileSettings = (Button) view.findViewById(R.id.btnPilgrimProfileSettings);
        btnSearchHajjGuide = (Button) view.findViewById(R.id.btnSearchHajjGuide);

        btnUpdatePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UpdatePaymentActivity.class));
            }
        });

        btnPilgrimProfileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PilgrimProfileSettings.class);
                startActivity(intent);
            }
        });

        btnSearchHajjGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AllHajjGuideActivity.class);
                startActivity(intent);
            }
        });


        //load current user information
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    String displayname = dataSnapshot.child("displayname").getValue().toString();
                    String contactnumber = dataSnapshot.child("mobilenumber").getValue().toString();
                    String lastname = dataSnapshot.child("lastname").getValue().toString();
                    String middlename = dataSnapshot.child("middlename").getValue().toString();
                    String firstname = dataSnapshot.child("firstname").getValue().toString();
                    final String image = dataSnapshot.child("image").getValue().toString();
                    String passport = dataSnapshot.child("passport").getValue().toString();
                    String biometrics = dataSnapshot.child("biometric").getValue().toString();
                    final String yellowcard = dataSnapshot.child("yellowcard").getValue().toString();

                    String accountstatus = dataSnapshot.child("accountstatus").getValue().toString();
                    String citizenship = dataSnapshot.child("citizenship").getValue().toString();
                    String address = dataSnapshot.child("address").getValue().toString();
                    String gender = dataSnapshot.child("gender").getValue().toString();
                    String civilstatus = dataSnapshot.child("civilstatus").getValue().toString();

                    String passportexpiry = dataSnapshot.child("passportexpiry").getValue().toString();
                    String yellowcardexpiry = dataSnapshot.child("yellowcardexpiry").getValue().toString();


                    tvPilgrimDisplayName.setText(displayname);
                    tvPilgrimContactNumber.setText(contactnumber);
                    tvPilgrimLastName.setText("Last Name : " + lastname);
                    tvPilgrimFirstName.setText("First Name : " + firstname);
                    tvPilgrimPassport.setText("Passport :" + passport);
                    tvPilgrimBiometric.setText("Biometric :" + biometrics);
                    tvPilgrimYelloCard.setText("Yellow Card : " + yellowcard);
                    tvPilgrimMiddleName.setText("Middle Name : " + middlename);

                    tvPilgrimAccountStatus.setText("Account Status : " + accountstatus);
                    tvPilgrimCivilStatus.setText("Civil Status : " + civilstatus);
                    tvPilgrimCitizenship.setText("Citizenship : " + citizenship);
                    tvPilgrimAddress.setText("Address : " + address);
                    tvPilgrimGender.setText("Gender : " + gender);

                    //tvPilgrimPassportExpiry.setText("Passport Expiry : " + passportexpiry);
                    //tvPilgrimYellowCardExpiry.setText("Yellow Card Expiry : "+ yellowcardexpiry);


                    if (accountstatus.equals("Not Verified")){
                        //hide that is needed to be hide

//                    tvRequirementsUpdate.setVisibility(View.INVISIBLE);
//                    tvPilgrimPaid.setVisibility(View.INVISIBLE);
//                    tvPilgrimBalance.setVisibility(View.INVISIBLE);
//                    tvPilgrimYelloCard.setVisibility(View.INVISIBLE);
//                    tvPilgrimYellowCardExpiry.setVisibility(View.INVISIBLE);
//                    tvPilgrimBiometric.setVisibility(View.INVISIBLE);
//                    tvPilgrimPassport.setVisibility(View.INVISIBLE);
//                    tvPilgrimPassportExpiry.setVisibility(View.INVISIBLE);

//                        tvRequirementsUpdate.setText("Requirements Update");
//                    tvPilgrimPaid.setText("Paid : ");
//                    tvPilgrimBalance.setText("Balance : ");
                        tvPilgrimYelloCard.setText("Yellow Card : ");
//                    tvPilgrimYellowCardExpiry.setText("Expiry : ");
                        tvPilgrimBiometric.setText("Biometric : ");
                        tvPilgrimPassport.setText("Passport : ");
//                    tvPilgrimPassportExpiry.setText("Expiry : ");

                        btnUpdatePayment.setEnabled(false);
                        //btnUpdatePayment.setVisibility(View.INVISIBLE);

                        btnSearchHajjGuide.setEnabled(false);
                        //btnSearchHajjGuide.setVisibility(View.INVISIBLE);

                    }

                    if (!image.equals("default")){
                        Picasso.with(getContext())
                                .load(image)
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.img_profile_img)
                                .into(mPilgrimProfileImage, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Picasso.with(getContext())
                                                .load(image)
                                                .placeholder(R.drawable.img_profile_img)
                                                .into(mPilgrimProfileImage);
                                    }
                                });
                    }
                } else {
                    Toast.makeText(getActivity(), dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                //Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        updateApprovedPayments();
        selectedHajjGuide();

        return view;
    }

    private void selectedHajjGuide() {

        mFriendsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    String hajjkey = dataSnapshot.getKey();

                    String current_uid = mCurrentUser.getUid();

                    for (final DataSnapshot hajjSnapshot : dataSnapshot.getChildren()){

                        Log.d("select : ", hajjSnapshot.toString());

                        String selectedHajjGuide = hajjSnapshot.getKey();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

                        databaseReference.child(selectedHajjGuide).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()){

                                    String firstname = dataSnapshot.child("firstname").getValue().toString();
                                    String LastName = dataSnapshot.child("lastname").getValue().toString();

                                    tvSelectedHajjGuide.setText("Selected Hajj Guide : " + LastName + ", " + firstname);

                                    if (hajjSnapshot == null){
                                        Toast.makeText(getContext(), hajjSnapshot.toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        btnSearchHajjGuide.setEnabled(false);
                                        btnSearchHajjGuide.setVisibility(View.INVISIBLE);
                                    }

                                } else {
                                    Toast.makeText(getContext(), dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    //tvSelectedHajjGuide.setText("Selected Hajj Guide : " + selectedHajj);

                    //String date = dataSnapshot.child("date").getValue().toString();
//                    for (DataSnapshot selectedHajjGuideSnapShot : dataSnapshot.getChildren()){
//                        tvSelectedHajjGuide.setText("Selected Hajj Guide : ");
//                    }

//                    if (dataSnapshot.hasChild(current_uid)){
//                        String selectedHajjGuideString = dataSnapshot.child(current_uid).child("date").getValue().toString();
//                        tvSelectedHajjGuide.setText("Selected Hajj Guide : " + selectedHajjGuideString);
//                    }

                } else {
                    Toast.makeText(getContext(), dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateApprovedPayments() {

        mApprovedPayments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
