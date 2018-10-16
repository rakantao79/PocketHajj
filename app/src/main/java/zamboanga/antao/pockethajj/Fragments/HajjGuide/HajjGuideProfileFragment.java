package zamboanga.antao.pockethajj.Fragments.HajjGuide;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import zamboanga.antao.pockethajj.HajjGuideGUIActivity;
import zamboanga.antao.pockethajj.HajjGuideUpdate.HajjGuideProfileSettings;
import zamboanga.antao.pockethajj.HajjGuideUpdate.HajjGuideUpdateProfile;
import zamboanga.antao.pockethajj.PilgrimUpdate.PilgrimProfileSettings;
import zamboanga.antao.pockethajj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HajjGuideProfileFragment extends Fragment {


    //firebase database
    private DatabaseReference mUserDatabase;

    //firebase user id
    private FirebaseUser mCurrentUser;

    private static final int GALLERY_PICK = 1;

    //firebaseStorage
    private StorageReference mImageStorage;

    //progress bar
    private ProgressDialog mProgressDialog;


    private TextView tvHGProfileLastName;
    private TextView tvHGProfileFirstName;
    private TextView tvHGProfileContactNum;
    private TextView tvHGProfileNumOfPilgrims;

    private CircleImageView cimgvHGProfile;

    private Button btnHGProfileSettings;
    private Button btnHGProfileManageUsers;

    public HajjGuideProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hajj_guide_profile, container, false);


        //database get info of the current user id
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        tvHGProfileLastName = (TextView) view.findViewById(R.id.tvHGProfileLastName);
        tvHGProfileFirstName = (TextView) view.findViewById(R.id.tvHGProfileFirstName);
        tvHGProfileContactNum = (TextView) view.findViewById(R.id.tvHGProfileContactNum);
        tvHGProfileNumOfPilgrims = (TextView) view.findViewById(R.id.tvHGNumOfPilgrims);

        cimgvHGProfile = (CircleImageView) view.findViewById(R.id.imgHGPRofile);


        btnHGProfileSettings = (Button) view.findViewById(R.id.btnHGProfileCallProfileSettings);
        btnHGProfileManageUsers = (Button) view.findViewById(R.id.btnHGProfileCallManageUsers);

        btnHGProfileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HajjGuideProfileSettings.class);
                startActivity(intent);
            }
        });

        btnHGProfileManageUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Intent intent = new Intent(getContext(), HajjGuideGUIActivity.class);
                startActivity(intent);
            }
        });

        //load current information
        loadCurrentInfo();

        return view;
    }

    private void loadCurrentInfo() {

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String displayname = dataSnapshot.child("displayname").getValue().toString();
                String contactnumber = dataSnapshot.child("mobilenumber").getValue().toString();
                String lastname = dataSnapshot.child("lastname").getValue().toString();
                String firstname = dataSnapshot.child("firstname").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();

                tvHGProfileLastName.setText(lastname);
                tvHGProfileFirstName.setText(firstname);
                tvHGProfileContactNum.setText(contactnumber);
                tvHGProfileNumOfPilgrims.setText("Number of pilgrims: ");

                if (!image.equals("default")){
                    Picasso.with(getContext())
                            .load(image)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.img_profile_img)
                            .into(cimgvHGProfile, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(getContext())
                                            .load(image)
                                            .placeholder(R.drawable.img_profile_img)
                                            .into(cimgvHGProfile);
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
