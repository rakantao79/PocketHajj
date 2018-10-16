package zamboanga.antao.pockethajj.HajjGuideUpdate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import zamboanga.antao.pockethajj.PilgrimUpdate.PilgrimProfileSettings;
import zamboanga.antao.pockethajj.R;

public class HajjGuideProfileSettings extends AppCompatActivity {

    //toolbar
    private Toolbar mToolbar;

    //firebase database
    private DatabaseReference mUserDatabase;

    //firebase user id
    private FirebaseUser mCurrentUser;

    private TextView tvHGProfSettingsFirstName;
    private TextView tvHGProfSettingsLastName;
    private TextView tvHGProfSettingsContactNum;

    private CircleImageView imgHGProfileSettings;
    private Button btnHGProfChangeImage;


    private static final int GALLERY_PICK = 1;

    //firebaseStorage
    private StorageReference mImageStorage;

    //progress bar
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hajj_guide_profile_settings);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_hajj_guide_profile_settings);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Update Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvHGProfSettingsLastName = (TextView) findViewById(R.id.tvHGProfSettingsLastName);
        tvHGProfSettingsFirstName = (TextView) findViewById(R.id.tvHGProfSettingsFirstName);
        tvHGProfSettingsContactNum = (TextView) findViewById(R.id.tvHGProfSettingsContactNum);

        imgHGProfileSettings = (CircleImageView) findViewById(R.id.imgHGProfSettings);
        btnHGProfChangeImage = (Button) findViewById(R.id.btnHGProfChangeImage);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        //connecting firebase storage
        mImageStorage = FirebaseStorage.getInstance().getReference();

        loadHajjGuideInformation();

        btnHGProfChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), GALLERY_PICK);
            }
        });
    }

    private void loadHajjGuideInformation() {

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String displayname = dataSnapshot.child("displayname").getValue().toString();
                String contactnumber = dataSnapshot.child("mobilenumber").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();
                String lastname = dataSnapshot.child("lastname").getValue().toString();
                String firstname = dataSnapshot.child("firstname").getValue().toString();


                tvHGProfSettingsLastName.setText(lastname);
                tvHGProfSettingsFirstName.setText(firstname);
                tvHGProfSettingsContactNum.setText(contactnumber);

                if (!image.equals("default")){
                    Picasso.with(HajjGuideProfileSettings.this)
                            .load(image)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.img_profile_img)
                            .into(imgHGProfileSettings, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(HajjGuideProfileSettings.this)
                                            .load(image)
                                            .placeholder(R.drawable.img_profile_img)
                                            .into(imgHGProfileSettings);
                                }
                            });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageURI = data.getData();

            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(imageURI)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                //progress bar
                mProgressDialog = new ProgressDialog(HajjGuideProfileSettings.this);
                mProgressDialog.setTitle("Uploading Image");
                mProgressDialog.setMessage("Please wait");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri resultUri = result.getUri();

                //optional
                //compressor of image
                File thumb_filepath = new File(resultUri.getPath());

                String current_user_id = mCurrentUser.getUid();

                //optional
                //compressor
                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_filepath);

                //optional
                ByteArrayOutputStream baos  = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                final byte[] thumb_byte = baos.toByteArray();

                StorageReference filepath = mImageStorage.child("profile_images").child(current_user_id + ".jpg");

                //compress image to make thumbnaile
                final StorageReference thumb_file = mImageStorage.child("profile_images").child("thumbs").child(current_user_id + ".jpg");


                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){

                            //Toast.makeText(SettingsActivity.this, "Working", Toast.LENGTH_SHORT).show();

                            //final String download_url = task.getResult().getDownloadUrl().toString();
                            @SuppressWarnings("VisibleForTests")
                            final String download_url = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumb_file.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    @SuppressWarnings("VisibleForTests")
                                    String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();

                                    if (thumb_task.isSuccessful()){

                                        Map updateHashMap = new HashMap<String, String>();
                                        updateHashMap.put("image", thumb_downloadUrl);
                                        updateHashMap.put("thumbimage", thumb_downloadUrl);

                                        mUserDatabase.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){
                                                    mProgressDialog.hide();
                                                    Toast.makeText(getApplicationContext(), "Upload Successful", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                                    } else {

                                        Toast.makeText(getApplicationContext(), "Error uploading on Thumbnail", Toast.LENGTH_SHORT).show();
                                        mProgressDialog.dismiss();

                                    }
                                }
                            });

                        } else {

                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();

                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }
}
