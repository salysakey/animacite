package org.anima.activities;
import org.anima.helper.Navigation;
import org.anima.helper.Permissions;
import org.anima.utils.ConfigurationVille;
import org.anima.utils.OnSwipeTouchListener;
import org.json.JSONArray;
import org.anima.animacite.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.anima.utils.PrefManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Calendar;

/**
 * Created by thiam on 05/11/2015.
 */
//http://146.185.161.5/phpmyadmin : root/mortalxy
public class ConcoursActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ConcoursActivity";
    ProgressDialog prgDialog;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private Menu menudeux;
    private  JSONArray photo_list;
    private int current_index = 0;
    private JSONObject concourObj;
    private JSONObject infosConcours;
    int CAMERA_PIC_REQUEST = 2;
    private ConstraintLayout layout_body;
    private WeakReference<Object> uriReference;
    public static  final int CONCOURS_NOTYET_START = 0;
    public static final int CONCOURS_INPROGRESS = 1;
    public static final int CONCOURS_ISOVER = -1;
    public int concours_progress_status;
    private JSONObject currentObj;
    private long nb_likes = 0;
    private boolean is_liked = false;
    String username, photo_url;
    ImageView display_image;
    TextView txt_nb_like, txt_user;
    InputStream is;
    Drawable d;
    ImageView btn_camera, btn_back, btn_next, btn_prev, btn_like;
    private File mFile;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE = 19;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in concours");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        prgDialog = new ProgressDialog(this);
        RequestParams params = new RequestParams();
        // Put Http parameter username with value of Email Edit View control
        params.put("userId", ""+PrefManager.getUserId(getApplicationContext()));
        //Permissions.checkStoragePermission(ConcoursActivity.this, REQUEST_WRITE_EXTERNAL_STORAGE);
        invokeWS(params);

    }// end function


    public JSONArray reversJsonArray(JSONArray jsonArray){
        JSONArray newJsonArray = new JSONArray();
        for (int i = jsonArray.length()-1; i>=0; i--) {
            try {
                newJsonArray.put(jsonArray.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return newJsonArray;
    };// end function

    public void runActivity(JSONObject obj) throws JSONException {
        Calendar cl = Calendar.getInstance();
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(System.currentTimeMillis());
        long current_time_seconds = current.getTimeInMillis()/1000;
        infosConcours = (JSONObject) obj.getJSONArray("infosConcours").get(0);
        photo_list = obj.getJSONArray("photoNewsfeed");
        photo_list = reversJsonArray(photo_list);
        long date_debut = infosConcours.getLong("date_debut");
        long date_fin = infosConcours.getLong("date_fin");
        long date_diff_debut = date_debut - current_time_seconds;
        long date_diff_fin = date_fin - current_time_seconds ;
        if(date_diff_debut>0){
            concours_progress_status = CONCOURS_NOTYET_START;
            applyNotyetConcours(infosConcours, date_diff_debut);
        }else if(date_diff_debut<=0 && date_diff_fin>=0){
            concours_progress_status = CONCOURS_INPROGRESS;
            applyProgressConcours(infosConcours, photo_list);
        }else if(date_diff_debut<=0 && date_diff_fin<0){
            concours_progress_status = CONCOURS_ISOVER;
            applyOverConcours(infosConcours, photo_list);
        }// end if
        initToolbar();
        setupDrawerLayout();
    }// end method

    /**
     *
     * @param info
     */
    public void applyOverConcours(JSONObject info, JSONArray image_list) throws JSONException {
        JSONObject first_obj = (JSONObject) image_list.get(current_index);
        super.setContentView(R.layout.activity_concours_over);
        // set click listener to neccessary button
        btn_next = (ImageView) findViewById(R.id.btn_next);
        btn_prev = (ImageView) findViewById(R.id.btn_prev);
        layout_body = (ConstraintLayout) findViewById(R.id.layout_body);
        TextView txt_desc = (TextView) findViewById(R.id.txt_desc);
        btn_next.setOnClickListener(this);
        btn_prev.setOnClickListener(this);
        layout_body.setOnTouchListener(new OnSwipeTouchListener(ConcoursActivity.this) {
            public void onSwipeTop() {}
            public void onSwipeRight() {navigateItem(findViewById(R.id.btn_prev));}
            public void onSwipeLeft() {navigateItem(findViewById(R.id.btn_next));}
            public void onSwipeBottom() {}

        });
        displayObject(first_obj);
        txt_desc.setText(info.getString("concours_msg_final"));
    }// end method

    /**
     *
     * @param info
     * @param time_seconds_until_event
     * @throws JSONException
     */
    public void applyNotyetConcours(JSONObject info, long time_seconds_until_event) throws JSONException {
        //time_seconds_until_event = 86400 * 2; // 2 days (for testing)
        this.setContentView(R.layout.activity_concours_notyet);

        long n_days = time_seconds_until_event / (60 * 60 * 24);
        String description = info.getString("concours_description");
        TextView txt_desc = (TextView) findViewById(R.id.txt_desc);
        TextView txt_next_event = (TextView) findViewById(R.id.txt_nextevent_remain);
        txt_next_event.setText("Ce concours commence dans "+n_days+" jours");
        txt_desc.setText(description);

    }// end method

    /**
     *
     * @param info
     * @param image_list
     * @throws JSONException
     */
    public void applyProgressConcours(JSONObject info, JSONArray image_list) throws JSONException {
        this.setContentView(R.layout.activity_concours_progress);
        // set click listener to neccessary button
        btn_camera = (ImageView) findViewById(R.id.btn_camera);
        btn_next = (ImageView) findViewById(R.id.btn_next);
        btn_prev = (ImageView) findViewById(R.id.btn_prev);
        btn_like = (ImageView) findViewById(R.id.btn_like);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        display_image = (ImageView) findViewById(R.id.image_display);
        //layout_body = (ConstraintLayout) findViewById(R.id.layout_body);
        btn_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_prev.setOnClickListener(this);
        btn_like.setOnClickListener(this);
        btn_camera.setOnClickListener(this);
        btn_camera.setImageResource(R.drawable.cc_camera_active);
        TextView txt_desc = (TextView) findViewById(R.id.txt_desc);

        display_image.setOnTouchListener(new OnSwipeTouchListener(ConcoursActivity.this) {
            public void onSwipeTop() {}
            public void onSwipeRight() {navigateItem(findViewById(R.id.btn_prev));}
            public void onSwipeLeft() {navigateItem(findViewById(R.id.btn_next));}
            public void onSwipeBottom() {}
        });

        txt_desc.setText(info.getString("concours_description"));
        if(image_list.length()>0){
            JSONObject first_obj = (JSONObject) image_list.get(current_index);
            displayObject(first_obj);
        }else{
            btn_next.setVisibility(View.INVISIBLE);
            btn_prev.setVisibility(View.INVISIBLE);
        }
    }// end method

    /**
     *
     * @param jObject
     */
    protected void displayObject(JSONObject jObject){
        try {
            nb_likes = jObject.getLong("nb_likes");
            is_liked = jObject.getBoolean("like");
            username = jObject.getString("utilisateur_pseudo");
            photo_url = jObject.getString("photo_parcours");
            display_image = (ImageView) findViewById(R.id.image_display);
            btn_like = (ImageView) findViewById(R.id.btn_like);
            txt_nb_like = (TextView) findViewById(R.id.txt_nb_like);
            txt_user = (TextView) findViewById(R.id.txt_username);
            txt_user.setText("by: "+username);
            txt_nb_like.setText(""+nb_likes);
            if(concours_progress_status== CONCOURS_INPROGRESS && is_liked==true){
                btn_like.setImageResource(R.drawable.cc_heart_active);
            }else if(concours_progress_status == CONCOURS_INPROGRESS && is_liked==false){
                btn_like.setImageResource(R.drawable.cc_heart_inactive);
            }
            photo_url = photo_url.replace("signalements_photos/","signalements_photos%2f");
            prgDialog.setMessage("Chargement...");
            prgDialog.show();
            is = (InputStream) new URL(photo_url).getContent();
            d = Drawable.createFromStream(is, "src name");
            prgDialog.hide();
            display_image.setImageDrawable(d);

            // disable navigation when out of item
            autoDisableNavigation();

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }// end method

    public void autoDisableNavigation(){
        if(current_index==0){
            btn_prev.setVisibility(View.INVISIBLE);
        }else{
            btn_prev.setVisibility(View.VISIBLE);
        }
        if(current_index==photo_list.length()-1){
            btn_next.setVisibility(View.INVISIBLE);
        }else{
            btn_next.setVisibility(View.VISIBLE);
        }
    }

    public void invokeWS(RequestParams params) {
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ConfigurationVille.Retrieve_Concours,params, new AsyncHttpResponseHandler() {
            //client.get("http://192.168.0.10:8080/Anima/rest/showfollow/doshow",params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                try {
                    // JSON Object
                    concourObj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (concourObj.getBoolean("status")) {
                        runActivity(concourObj);
                    }
                    // Else display error message
                    else {
                        //errorMsg.setText(obj.getString("error_msg"));
                        TextView loading_text = (TextView) findViewById(R.id.txt_loading);
                        loading_text.setText("Il n'y a pas de concours pour le moment, n'hésitez pas a contacter votre ville pour proposer vos idées de concours!");
                        //Toast.makeText(getApplicationContext(), "Il n'y a pas de concours pour le moment, n'hésitez pas a contacter votre ville pour proposer vos idées de concours!", Toast.LENGTH_LONG).show();
                    }// end if
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Une erreur réseau s'est produite lors du chargement du concours", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }// end try
            }// end override function

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                errorResponse(statusCode);
            }// end method onFailure
        });//
    }// end function


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppCompatActivity dodo = new AppCompatActivity();
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Concours");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }// end if
    }// end method



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }// end method

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }// end method


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_next:
                btn_next.setImageResource(R.drawable.cc_red_heart);
                navigateItem(view);
                btn_next.setImageResource(R.drawable.cc_arr_right);
                break;
            case R.id.btn_prev:
                navigateItem(view);
                break;
            case R.id.btn_like:
                try {
                    doLikePhoto(current_index);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_camera:
                startCamera();
                break;
            case R.id.btn_back:
                navigateItem(findViewById(R.id.btn_next));
                break;
            default:
                break;
        }
    }// end method

    /**
     * @author Saly Sakey
     * @param index
     * @throws JSONException
     */
    public void doLikePhoto(int index) throws JSONException {
        //URL: http://146.185.161.5:8080/Anima/rest/concours/dolike?userId=XXX&ConcoursId=YYY&photoId=ZZZ
        final JSONObject currentObj = (JSONObject) photo_list.get(index);
        if(currentObj.getBoolean("like")){
            currentObj.put("like", false);
        }else{
            currentObj.put("like", true);
        }
        displayObject(currentObj);
        RequestParams params = new RequestParams();
        //params.put("userId", PrefManager.getUserId(getApplicationContext()));
        //params.put("concoursId",infosConcours.getInt("concours_id"));
        //params.put("photoId",currentObj.getInt("concours_photo_id"));

        String str = "?userId="+PrefManager.getUserId(getApplicationContext());
        str += "&&concoursId="+infosConcours.getInt("concours_id");
        str += "&&photoId="+currentObj.getInt("concours_photo_id");

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ConfigurationVille.Do_Like+str,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("status")) {
                        refreshList();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }// end try
            }// end override function
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
               errorResponse(statusCode);
            }// end method onFailure
        });//
    }// end function

    /**
     * @author Saly Sakey
     */
    public void refreshList(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", ""+PrefManager.getUserId(getApplicationContext()));
        client.get(ConfigurationVille.Retrieve_Concours, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("status")) {
                        concourObj = new JSONObject(response);
                        photo_list = concourObj.getJSONArray("photoNewsfeed");
                        photo_list = reversJsonArray(photo_list);
                        displayObject((JSONObject) photo_list.get(current_index));
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }// end try
            }// end override function
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                errorResponse(statusCode);
            }// end method onFailure
        });//
    }
    /**
     * @author Saly Sakey
     * @param statusCode
     */
    public void errorResponse(int statusCode){
        // Hide Progress Dialog
        // When Http response code is '404'
        if (statusCode == 404) {
            Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
        }
        // When Http response code is '500'
        else if (statusCode == 500) {
            Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
        }
        // When Http response code other than 404, 500
        else {
            Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
        }// end if
    }
    /**
     * @author Saly Sakey
     * @param view
     */
    public void navigateItem(View view) {

        //JSONObject currentObj = null;
        //ImageView btnIterator = (ImageView) view.findViewById(view.getId());
        try {
            if(view.getId()==R.id.btn_next && current_index<photo_list.length()-1){
                current_index +=1;
            }else if(view.getId()==R.id.btn_prev && current_index>0){
                current_index -=1;
            }else{
                Toast.makeText(getApplicationContext(),"Vous avez balayé toutes les photos.", Toast.LENGTH_SHORT).show();
            }// end if

            currentObj = (JSONObject) photo_list.get(current_index);
            displayObject(currentObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }catch(IndexOutOfBoundsException ind){

        }// end try
    }// end function

    /**
     *
     */
    public void startCamera(){
        try {
            int concourId = infosConcours.getInt("concours_id");
            Intent intent = new Intent(this, FirebaseCCImage.class);
            intent.putExtra("concoursId", concourId);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }// end function

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && null!=data.getExtras()) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            ImageView imageview = (ImageView) findViewById(R.id.image_display); //sets imageview as the bitmap
            imageview.setImageBitmap(image);
            Intent intent = new Intent(this,FirebaseCCImage.class);
            intent.putExtra("image_taken",image);
            startActivity(intent);
        }
    }

    /**
     * Refactored by Saly Sakey November-07-2017
     */
    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        new Navigation(ConcoursActivity.this, drawerLayout, view);
    }// end function

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Permissions.storagePermissionResult(requestCode, permissions, grantResults, ConcoursActivity.this, REQUEST_WRITE_EXTERNAL_STORAGE);
    }
}// end class
