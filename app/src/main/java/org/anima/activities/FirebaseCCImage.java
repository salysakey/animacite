package org.anima.activities;

import org.anima.animacite.R;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.anima.easyimage.DefaultCallback;
import org.anima.easyimage.EasyImage;
import org.anima.helper.Permissions;
import org.anima.utils.PrefManager;
import org.anima.utils.UploadFileSuccessEvent;
import org.anima.utils.ConfigurationVille;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Calendar;

import de.greenrobot.event.EventBus;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class FirebaseCCImage extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FirebaseCCImage";
    private static final String TAG_DIALOG_FRAGMENT = "tagDialogFragment";
    private static final int RC_CAMERA_PERMISSIONS = 102;
    private static final String[] cameraPerms = new String[]{
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE=19;
    private static final int MY_PERMISSIONS_GRANTED = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final int MY_PERMISSIONS_REQUESTS_POSITION = MY_PERMISSIONS_REQUEST_READ_CONTACTS + 1;
    private static final int MY_PERMISSIONS_REQUESTS_CAMERA = MY_PERMISSIONS_REQUEST_READ_CONTACTS + 2;
    private static final int MY_PERMISSIONS_CONCOURS = MY_PERMISSIONS_REQUEST_READ_CONTACTS + 2;

    public ImageButton photo;
    public ImageView imagephoto;
    public Uri fileUri;
    String imageURL;

    private Location location;
    private double longitude, latitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    ProgressDialog prgDialog;
    public static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    Bitmap bmp;

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute
    @SuppressWarnings("VisibleForTests")
    public Uri fullSizeUrl;
    private int concoursId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        concoursId = intent.getIntExtra("concoursId", 0);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        Permissions.checkStoragePermission(FirebaseCCImage.this, REQUEST_WRITE_EXTERNAL_STORAGE);
        Permissions.checkLocationPermission(FirebaseCCImage.this, MY_PERMISSIONS_CONCOURS);
        showEasyImagePicker();
    }


    public void displayImage() throws IOException {
        setContentView(R.layout.cc_uploadcontour);

        Button btn_close = (Button) findViewById(R.id.btnClose);
        photo = (ImageButton) findViewById(R.id.takephoto);
        imagephoto = (ImageView) findViewById(R.id.image_photo);
        btn_close.setOnClickListener(this);
        ImageView display_upload = (ImageView) findViewById(R.id.display_upload);
        InputStream is = (InputStream) new URL(imageURL).getContent();
        Drawable d = Drawable.createFromStream(is, "src name");
        display_upload.setImageDrawable(d);
    }



    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, " - onRequestPermissionsResult");
        Permissions.storagePermissionResult(requestCode, permissions, grantResults, FirebaseCCImage.this, REQUEST_WRITE_EXTERNAL_STORAGE);

        //Demande d'autorisations pour le concours LOCALISATION + CAMERA
        if (grantResults.length > 0 && requestCode == MY_PERMISSIONS_CONCOURS && grantResults[0] == MY_PERMISSIONS_GRANTED) {
            showEasyImagePicker();
        } else if(requestCode == MY_PERMISSIONS_CONCOURS && grantResults[0] != MY_PERMISSIONS_GRANTED){
            Permissions.getLocation(FirebaseCCImage.this, MY_PERMISSIONS_CONCOURS, location,
                    latitude, longitude,MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES);
        }
    }

    public void addNewItemToDB(int concoursId, double latitude, double longitude, String imageURL){
        setContentView(R.layout.loading);
        TextView txt_loading = (TextView) findViewById(R.id.txt_loading);
        txt_loading.setText("Enregistrement en cours...");
        // Set Progress Dialog Text
        //http://146.185.161.5:8080/Anima/rest/concours/send?concoursId=AAA&userId=BBB&latitude=CCC&longitude=DDD&date=EEE&parcoursPhoto=FFF
        AsyncHttpClient client = new AsyncHttpClient();
        Calendar cl = Calendar.getInstance();
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(System.currentTimeMillis());
        long dateInSeconds = current.getTimeInMillis()/1000;
        long userId = PrefManager.getUserId(getApplicationContext());

        RequestParams params = new RequestParams();
        /*
        params.put("concoursId",concoursId);
        params.put("userId",userId);
        params.put("latitude",latitude);
        params.put("longitude",longitude);
        params.put("date",dateInSeconds);
        params.put("parcoursPhoto",imageURL);
        */
        String str = "?concoursId="+concoursId+"&&";
        str += "userId="+userId+"&&";
        str += "latitude="+latitude+"&&";
        str += "longitude="+longitude+"&&";
        str += "date="+dateInSeconds+"&&";
        imageURL = imageURL.replace("&","%26");
        imageURL = imageURL.replace("signalements_photos/","signalements_photos%2f");
        str += "parcoursPhoto="+imageURL;

        client.get(ConfigurationVille.addItemConcours+str,params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                try {
                    displayImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }// end override function
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {

                //Log.d("error add item", error.getMessage());
            }// end method onFailure
        });//


    }// end function

    @AfterPermissionGranted(RC_CAMERA_PERMISSIONS)
    public void showEasyImagePicker() {
        if (!EasyPermissions.hasPermissions(this, cameraPerms)) {
            //EasyImage.openChooserWithGallery(this, "Choisir", 0);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                //ActivityCompat.requestPermissions(FirebaseCCImage.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUESTS_CAMERA);
            }
            else {

            }

        }

        EasyImage.openCamera(this, 0);

        //EasyImage.openChooserWithGallery(this, "Choisir", 0);
    }


    public  void finishActivity(){
        FirebaseCCImage.this.finish();
    }// end function


    private Fragment getExistingDialogFragment() {
        return getSupportFragmentManager().findFragmentByTag(TAG_DIALOG_FRAGMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.configuration(this)
                .saveInAppExternalFilesDir() //if you want to use root internal memory for storying images
                .saveInRootPicturesDirectory()
                .setCopyExistingPicturesToPublicLocation(true);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(FirebaseCCImage.this, "Oops la récupération de l'image à échouer.", Toast.LENGTH_SHORT).show();
            }

            //quand il prend la photo
            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                try {
                    compressAndUpload(imageFile, source, type);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(FirebaseCCImage.this);
                    if (photoFile != null) photoFile.delete();
                }
                finishActivity();
            }
        });

    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnClose){
            FirebaseCCImage.this.finish();
            Intent concourse = new Intent(FirebaseCCImage.this, ConcoursActivity.class);
            startActivity(concourse);
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void compressAndUpload(File imageFile, EasyImage.ImageSource source, int type) throws IOException {
        PrefManager.setImageUrl(getApplicationContext(), null);
        fileUri = Uri.fromFile(imageFile);

                int new_width = 900;
                int new_height = 0;
                Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                try{
                    int width = bmp.getWidth();
                    int height = bmp.getHeight();
                    new_height = new_width* height / width ;
                    bmp = Bitmap.createScaledBitmap(bmp, new_width, new_height, true);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(new File(getRealPathFromURI(fileUri)));
                        bmp.compress(Bitmap.CompressFormat.JPEG, 70, fos);
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }

        PrefManager.setImageUrl(getApplicationContext(), fileUri.toString());

        UploadPostTask uploadTask = new UploadPostTask(FirebaseCCImage.this, fileUri, "");
        uploadTask.execute();
    }

    public class UploadPostTask extends AsyncTask<Void, Void, Void> {
        public static final String TAG = "UploadPostTask";
        private final FirebaseCCImage activity;
        private WeakReference<Uri> uriReference;
        private String fileName;


        public UploadPostTask(FirebaseCCImage activity, Uri uri, String inFileName) {
            this.activity = activity;
            this.uriReference = new WeakReference<>(uri);
            this.fileName = inFileName;
        }

        @Override
        protected void onPreExecute() {
            setContentView(R.layout.loading);
            TextView txt_loading = (TextView) findViewById(R.id.txt_loading);
            txt_loading.setText("Chargement de la photo...");
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
            final StorageReference fullSizeRef = photoRef.child(ConfigurationVille.FOLDER_NAME).child("concours_photos").child(ConfigurationVille.FOLDER_NAME +"-Concours-Android-" +timestamp.toString()+ ".jpg");

            fullSizeRef.putFile(uriToPut).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    fullSizeUrl = downloadUrl;
                    if (fullSizeUrl != null) {
                            imageURL = getimageurl();
                            PrefManager.setImageUrl(getApplicationContext(), null);
                            PrefManager.setImageUrl(getApplicationContext(), fullSizeUrl.toString());
//                       Picasso.with(getApplicationContext()).load(PrefManager.getImageUrl(getApplicationContext())).into(imagephoto);
                            EventBus.getDefault().post(new UploadFileSuccessEvent(taskSnapshot));
                            //prgDialog.hide();
                            addNewItemToDB(concoursId, latitude, longitude, imageURL);

                    } else {
                        Toast.makeText(FirebaseCCImage.this, "Oops l'image n'a pu être enregistré.", Toast.LENGTH_SHORT).show();
                        finishActivity();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(FirebaseCCImage.this, "Oops l'envoi de l'image à échouer.", Toast.LENGTH_SHORT).show();
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
            //File photoFile = EasyImage.lastlyTakenButCanceledPhoto(FirebaseCCImage.this);
            //if (photoFile != null) photoFile.delete();ß

        }
    }// end class UploadPostTask

}// end class
