package org.anima.activities;
import org.anima.animacite.R;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.anima.easyimage.DefaultCallback;
import org.anima.easyimage.EasyImage;
import org.anima.utils.PrefManager;
import org.anima.utils.ProgressDialogFragment;
import org.anima.utils.UploadFileSuccessEvent;
import org.anima.utils.ConfigurationVille;

import java.io.File;
import java.lang.ref.WeakReference;

import de.greenrobot.event.EventBus;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class FirebasePixActivity extends AppCompatActivity {

    private static final String TAG_DIALOG_FRAGMENT = "tagDialogFragment";
    private static final int RC_CAMERA_PERMISSIONS = 102;
    private static final String[] cameraPerms = new String[]{
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public ImageButton photo;
    public ImageView imagephoto;
    private Uri fileUri;
    private  Uri fullSizeUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signalement);
        photo = (ImageButton) findViewById(R.id.takephoto);
        imagephoto = (ImageView) findViewById(R.id.image_photo);


       // EventBus.getDefault().register(this);



       // showEasyImagePicker();
    }





    @AfterPermissionGranted(RC_CAMERA_PERMISSIONS)
    public void showEasyImagePicker() {
        EasyImage.openCamera(FirebasePixActivity.this,0);

        //EasyImage.openChooserWithGallery(this, "Choisir", 0);
        return;
    }

    public void showEasyImagePicker2(){
        if (!EasyPermissions.hasPermissions(this, cameraPerms)) {
            //EasyImage.openChooserWithGallery(this, "Choisir", 0);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                //ActivityCompat.requestPermissions(FirebaseCCImage.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUESTS_CAMERA);
            }
            else {

            }

        }

        EasyImage.openCamera(this, 0);
    }




    public void showProgressDialog(String message) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getExistingDialogFragment();
        if (prev == null) {
            ProgressDialogFragment fragment = ProgressDialogFragment.newInstance(message);
            fragment.show(ft, TAG_DIALOG_FRAGMENT);
        }
    }

    public void dismissProgressDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getExistingDialogFragment();
        if (prev != null) {
            ft.remove(prev).commitAllowingStateLoss();
        }
    }

    private Fragment getExistingDialogFragment() {
        return getSupportFragmentManager().findFragmentByTag(TAG_DIALOG_FRAGMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(FirebasePixActivity.this, "OOps la récupération de l'image à échouer.", Toast.LENGTH_SHORT).show();
            }

            //quand il prend la photo
            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {

                PrefManager.setImageUrl(getApplicationContext(), null);
                fileUri = Uri.fromFile(imageFile);
                PrefManager.setImageUrl(getApplicationContext(), fileUri.toString());
                //  UploadPostTask uploadTask = new FirebasePixActivity.UploadPostTask(FirebasePixActivity.this, fileUri, fileUri.getLastPathSegment());
                // uploadTask.execute();
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(FirebasePixActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    public void enregistrementBdd() {
                // Insert network call here!
                UploadPostTask uploadTask = new FirebasePixActivity.UploadPostTask(FirebasePixActivity.this, fileUri, fileUri.getLastPathSegment());
                uploadTask.execute();
    }



    public class UploadPostTask extends AsyncTask<Void, Void, Void> {
        public static final String TAG = "UploadPostTask";
        private final FirebasePixActivity activity;
        private WeakReference<Uri> uriReference;
        private String fileName;

        public UploadPostTask(FirebasePixActivity activity, Uri uri, String inFileName) {
            this.activity = activity;
            this.uriReference = new WeakReference<>(uri);
            this.fileName = inFileName;
        }

        @Override
        protected void onPreExecute() {

        }


        @Override
        protected Void doInBackground(Void... params) {
            Uri uriToPut = uriReference.get();
            if (uriToPut == null) {
                return null;
            }



            FirebaseStorage storageRef = FirebaseStorage.getInstance();
            StorageReference photoRef = storageRef.getReferenceFromUrl(ConfigurationVille.FIREBASE_STORAGE_URL);


            Long timestamp = System.currentTimeMillis();
            final StorageReference fullSizeRef = photoRef.child(ConfigurationVille.FOLDER_NAME).child("signalements_photos").child(ConfigurationVille.FOLDER_NAME+"-Signalements-Android-" +timestamp.toString()+ ".jpg");
            Log.d(TAG, fullSizeRef.toString());

            fullSizeRef.putFile(uriToPut).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     fullSizeUrl = taskSnapshot.getDownloadUrl();
                    if (fullSizeUrl != null) {

                        PrefManager.setImageUrl(getApplicationContext(), null);
                        Log.d(TAG, "fullSizeUrl: " + fullSizeUrl.toString());
                        PrefManager.setImageUrl(getApplicationContext(), fullSizeUrl.toString());
//                       Picasso.with(getApplicationContext()).load(PrefManager.getImageUrl(getApplicationContext())).into(imagephoto);
                        Log.d(TAG, PrefManager.getImageUrl(getApplicationContext()));
                        EventBus.getDefault().post(new UploadFileSuccessEvent(taskSnapshot));


                    } else {
                        Toast.makeText(FirebasePixActivity.this, "OOps l'image n'a pu être enregistré.", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(FirebasePixActivity.this, "OOps l'envoi de l'image à échouer.", Toast.LENGTH_SHORT).show();
                }
            });
            // TODO: Refactor these insanely nested callbacks.
            return null;
        }

        String getimageurl(){

            return fullSizeUrl.toString();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            File photoFile = EasyImage.lastlyTakenButCanceledPhoto(FirebasePixActivity.this);
            if (photoFile != null) photoFile.delete();



        }
    }
}
