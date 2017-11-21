package org.anima.activities;

import org.anima.helper.Navigation;
import org.anima.utils.ConfigurationVille;
import org.anima.animacite.R;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.squareup.otto.Bus;
//import com.squareup.otto.Subscribe;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import android.location.LocationListener;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.anima.adapters.CustomOnItemSelectedListener;
import org.anima.utils.GMailSender;
import org.anima.utils.PrefManager;
import org.anima.utils.UploadFileSuccessEvent;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by momo on 16/10/16.
 */
public class SignalementActivity extends FirebasePixActivity implements Chronometer.OnChronometerTickListener,
        AdapterView.OnItemClickListener {
    private final String TAG  = "SignalementActivity";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    private ListView listView;
    private ListAdapter adapter;
    private EditText emailET;
    private DrawerLayout drawerLayout;
    private Menu menudeux;
    private String description;
    private EditText descriptionET;
    private Spinner type;
    private Toolbar toolbar;
    private Location location;
    private double longitude, latitude;
    private List<String> list = new ArrayList<String>();
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    private static final int MY_PERMISSIONS_GRANTED = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE=19;
    private static final int MY_PERMISSIONS_REQUESTS_POSITION = MY_PERMISSIONS_REQUEST_READ_CONTACTS + 1;
    private static final int MY_PERMISSIONS_REQUESTS_CAMERA = MY_PERMISSIONS_REQUEST_READ_CONTACTS + 2;
    private static final int MY_PERMISSIONS_SIGNALEMENT = MY_PERMISSIONS_REQUEST_READ_CONTACTS + 2;


    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute

    private String mail;
    TextView errorMsg;

    ProgressDialog prgDialog;
    //private Evaluation evaluation = new Evaluation();
    private Chronometer chronometer;
    private static final int THRESHOLD_EXERCISE = 30 * 60 * 1000;
    private GMailSender lolo;
    //DBAdapter db;
    //Users users;
    int evaluationResult = 0;
    //private Bus bus;

    class SenderMail extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            sendMail(emailET);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            //  bus.post(new MailSendingEvent(aBoolean));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        setupDrawerLayout();
        initToolbar();
        // db = new DBAdapter(this);
        //users = getIntent().getParcelableExtra(getString(R.string.bundle_key_users));
        //bus = new Bus();
        //bus.register(this);
        emailET = (EditText) findViewById(R.id.editTextTo);
        descriptionET = (EditText) findViewById(R.id.description);
        errorMsg = (TextView) findViewById(R.id.error);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        checkPermission();
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationChangeListener = new LocationListener() {
            public void onLocationChanged(Location l) {
                if (l != null) {
                    Log.i("SuperMap", "Location changed : Lat: " + l.getLatitude() + " Lng: " +
                            l.getLongitude());
                }
            }

            public void onProviderEnabled(String p) {
            }

            public void onProviderDisabled(String p) {
            }

            public void onStatusChanged(String p, int status, Bundle extras) {
            }

        };


        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false && locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == false) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignalementActivity.this);
            // Setting Dialog Title
            alertDialog.setTitle("Veuillez activer votre localisation");
            alertDialog.setIcon(R.drawable.petite_image);
            alertDialog.setNegativeButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancel Dialog

                            Intent intent4 = new Intent(SignalementActivity.this, ImagePickActivity.class);
                            startActivity(intent4);
                            dialog.cancel();
                        }
                    });
            // Showing Alert Message
            AlertDialog alert = alertDialog.create();
            alert.show();

        } else {

            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(SignalementActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
                        MY_PERMISSIONS_SIGNALEMENT);
                //return;
            }
            else{

                locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationChangeListener);
                location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (location == null) {
                    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationChangeListener);
                    location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                }

                if (location != null) {

                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                } else {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignalementActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Veuillez activer votre localisation");
                    alertDialog.setIcon(R.drawable.petite_image);
                    // Setting Dialog Message
                    // Setting Positive "Yes" Button
                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Cancel Dialog
                                    Intent intent4 = new Intent(SignalementActivity.this, ImagePickActivity.class);
                                    startActivity(intent4);
                                    dialog.cancel();
                                }
                            });
                    // Showing Alert Message
                    AlertDialog alert = alertDialog.create();
                    alert.show();
                }
            }
        }


        type = (Spinner) findViewById(R.id.type);
        type.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        type.setBackgroundColor(Color.WHITE);

        addItemsOnSpinner();

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEasyImagePicker();
            }
        });

        //initializeEvaluation();

        //initializeControls();
    }

    private static void requestPermission(final Context context){
        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.

            new AlertDialog.Builder(context)
                    .setMessage("Please allow access to storage")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    }).show();

        }else {
            // permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }
    public void checkPermission(){
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //RUNTIME PERMISSION Android M
            if(PackageManager.PERMISSION_GRANTED==ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "");
            }else{
                requestPermission(SignalementActivity.this);
            }

        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, " - onRequestPermissionsResult");

        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),"Storage Permission is granted.",
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(),"No permission on storage",
                        Toast.LENGTH_SHORT).show();
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
            return;
        }
        //Demande d'autorisations pour le Signalement LOCALISATION + CAMERA
        if (requestCode == MY_PERMISSIONS_SIGNALEMENT && grantResults[0] == MY_PERMISSIONS_GRANTED && grantResults[1] == MY_PERMISSIONS_GRANTED) {
            Log.d(TAG, " - onRequestPermissionsResult.LOCALISATION");
            LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationChangeListener = new LocationListener() {
                public void onLocationChanged(Location l) {
                    if (l != null) {
                        Log.i("SuperMap", "Location changed : Lat: " + l.getLatitude() + " Lng: " +
                                l.getLongitude());
                    }
                }

                public void onProviderEnabled(String p) {
                }

                public void onProviderDisabled(String p) {
                }

                public void onStatusChanged(String p, int status, Bundle extras) {
                }

            };

            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationChangeListener);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (location == null) {
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationChangeListener);
                location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if(location!=null){


                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignalementActivity.this);
                // Setting Dialog Title
                alertDialog.setTitle("Nous ne pouvons vous localiser, réessayer plus tard.");
                alertDialog.setIcon(R.drawable.petite_image);
                alertDialog.setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent4 = new Intent(SignalementActivity.this, ImagePickActivity.class);
                                startActivity(intent4);
                                dialog.cancel();
                            }
                        });
                // Showing Alert Message
                AlertDialog alert = alertDialog.create();
                alert.show();
            }// end if location!=null

            return;
        }
        else if(requestCode == MY_PERMISSIONS_SIGNALEMENT && (grantResults[0] != MY_PERMISSIONS_GRANTED || grantResults[1] != MY_PERMISSIONS_GRANTED)){
            Intent intent4 = new Intent(SignalementActivity.this, ImagePickActivity.class);
            startActivity(intent4);
            Toast.makeText(getApplicationContext(), "Veuillez autoriser les permissions.", Toast.LENGTH_LONG).show();
            return;
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!PrefManager.getImageUrl(getApplicationContext()).isEmpty()) {
            imagephoto.setVisibility(View.VISIBLE);
            Picasso.with(getApplicationContext()).load(PrefManager.getImageUrl(getApplicationContext())).into(imagephoto);
        }
    }

    public void addItemsOnSpinner() {


        //list.add("Bris");
        //list.add("Nid-de-poule");

        list.add("TravauxPublics");
        list.add("Autres");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(dataAdapter);
    }

    public void signaler(View view) {

        description = descriptionET.getText().toString();

        if(description=="" || PrefManager.getImageUrl(getApplicationContext())=="" || description.isEmpty() || PrefManager.getImageUrl(getApplicationContext()).isEmpty()){
            //if(description==""){

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignalementActivity.this);
            // Setting Dialog Title
            alertDialog.setTitle("Attention");
            alertDialog.setMessage("Veuillez indiquer au moins une photo et une description");
            alertDialog.setIcon(R.drawable.petite_image);
            // Setting Dialog Message
            // Setting Positive "Yes" Button
            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancel Dialog
                            dialog.cancel();
                        }
                    });
            // Showing Alert Message
            AlertDialog alert = alertDialog.create();
            alert.show();

        }else{

            // Get NAme ET control value

            try {
                description = URLEncoder.encode(description, "utf-8");

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }
            showProgressDialog("Merci de votre participation citoyenne, veuillez patienter une petite minute.");


            enregistrementBdd();

            //  sendWS();

            // envoyer mail

        }

    }

    @SuppressWarnings("unused")
    public void onEvent(UploadFileSuccessEvent event) {
        Uri uri= event.getTaskSnapshot().getDownloadUrl();
        if(uri!=null){
            String url = uri.toString();
            PrefManager.setImageUrl(getApplicationContext(), url);
            sendWS();
            //Toast.makeText(getApplicationContext(), "Merci, votre signalement a été pris en compte !", Toast.LENGTH_LONG).show();


        }

    }

    public void sendWS(){

        mail = "mouha_thiam@hotmail.fr";

        sendMail(emailET);
        //invokeWS(params);

        RequestParams params = new RequestParams();

        // Put Http parameter username with value of Email Edit View control
        params.put("userId", "" + PrefManager.getUserId(getApplicationContext()));
        params.put("date", "" + System.currentTimeMillis());
        // Put Http parameter password with value of Password Edit View control
        //params.put("password", password);
        // Put Http parameter password with value of Password Edit View control
        params.put("description", description);
        params.put("type",  type.getSelectedItem().toString() );
        //params.put("type", "TravauxPublics");
        params.put("latitude", "" + latitude);
        params.put("longitude", "" + longitude);
        params.put("parcoursphoto", PrefManager.getImageUrl(getApplicationContext()));

        invokeWSsignalement(params);

    }

    private void sendMail(EditText emailET) {
        lolo = new GMailSender("noreply@animacite.com","mortalXY2016");

        try {

            String contenu="(Ce message vous a été envoyé automatiquement, merci de ne pas y répondre)\n" +
                    "\n" +
                    "Bonjour, \n" +
                    "\n" +
                    "La description du signalement est : "+description+".\n" +
                    "\n" +
                    "Bien cordialement,\n" +
                    "L’équipe de Feuch'App";

            lolo.sendSignalement("Signalement d'un utilisateur", contenu, "noreply@animacite.com", mail, PrefManager.getImageUrl(getApplicationContext()));

        } catch (Exception e) {

            e.printStackTrace();
        }



    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        chronometerBehaviour();
    }

    public void invokeWSsignalement(RequestParams params) {
        // Show Progress Dialog
        // prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.14:8080/Anima/rest/login/dologin",params ,new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS + "/signalement/dosignalement", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {

                        PrefManager.setImageUrl(getApplicationContext(), null);
                        dismissProgressDialog();

                        Intent intent4 = new Intent(SignalementActivity.this, ImagePickActivity.class);
                        startActivity(intent4);

                    }
                    // Else display error message
                    else {
                        errorMsg.setText("Oups.... Erreur de frappe ?");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Oups, une erreur s'est produite, veuillez réessayer ultérieurement.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Veuillez activer votre localisation.", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Signalement");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    /*
    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        View headerLayout = view.getHeaderView(0);
        ImageView avatarhomme = (ImageView) headerLayout.findViewById(R.id.avatarhomme);
        ImageView avatarfemme = (ImageView) headerLayout.findViewById(R.id.avatarfemme);
        if(PrefManager.getUserSexe(getApplicationContext()).equals("F")){
            avatarfemme.setVisibility(View.VISIBLE);
        }else{
            avatarhomme.setVisibility(View.VISIBLE);
        }
        String phone=PrefManager.getUserMail(getApplicationContext());
        String name = PrefManager.getUserName(getApplicationContext());

        TextView txtUserName = (TextView)headerLayout.findViewById(R.id.user_name);
        TextView txtUserMail = (TextView)headerLayout.findViewById(R.id.user_email);

        if (!TextUtils.isEmpty(phone)) {
            txtUserMail.setText(phone);
        }

        if (!TextUtils.isEmpty(name)) {
            txtUserName.setText(name);
        }

        menudeux = view.getMenu();
        if (PrefManager.getRatingStatus(getApplicationContext())==1) {
            menudeux.findItem(R.id.logout).setVisible(true);
            menudeux.findItem(R.id.my_account).setVisible(true);
            menudeux.findItem(R.id.maps).setVisible(true);
            menudeux.findItem(R.id.evenements).setVisible(true);
            //menudeux.findItem(R.id.stat).setVisible(true);
            menudeux.findItem(R.id.kiosque).setVisible(true);
            //menudeux.findItem(R.id.parametre).setVisible(true);
            //menudeux.findItem(R.id.infopratique).setVisible(true);
            menudeux.findItem(R.id.actu).setVisible(true);
            menudeux.findItem(R.id.panier).setVisible(true);
            menudeux.findItem(R.id.concours).setVisible(true);
        }else{
            menudeux.findItem(R.id.login).setVisible(true);

        }




        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                //fab.setVisibility(View.GONE);

                switch (menuItem.getItemId()) {
                    case R.id.concours:
                        startActivity(new Intent(SignalementActivity.this, ConcoursActivity.class));
                        break;
                    case R.id.panier:
                        PrefManager.setImageUrl(getApplicationContext(),null);
                        Intent intent4 = new Intent(SignalementActivity.this, FollowActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.my_account:
                        PrefManager.setImageUrl(getApplicationContext(),null);
                        Intent intent3 = new Intent(SignalementActivity.this, MyAccountActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.actu:
                        PrefManager.setImageUrl(getApplicationContext(),null);
                        Intent intent5 = new Intent(SignalementActivity.this, ImagePickActivity.class);
                        startActivity(intent5);
                        break;
                    case R.id.evenements:

                        if (PrefManager.getRatingStatus(getApplicationContext()) == 1) {
                            PrefManager.setImageUrl(getApplicationContext(),null);

                            Intent intent8 = new Intent(SignalementActivity.this, CalendarActivity.class);
                            startActivity(intent8);
                            break;

                        } else {
                            break;
                        }

                    case R.id.maps:

                        Intent intent9 = new Intent(SignalementActivity.this, MapActivity.class);
                        PrefManager.setImageUrl(getApplicationContext(),null);
                        startActivity(intent9);
                        break;
                    case R.id.infopratique:
                        if (PrefManager.getRatingStatus(getApplicationContext()) == 1) {
                            PrefManager.setImageUrl(getApplicationContext(),null);
                            Intent pratique = new Intent(SignalementActivity.this, PratiqueActivity.class);
                            startActivity(pratique);
                            break;

                        } else {
                            break;

                        }
                    case R.id.kiosque:

                        if (PrefManager.getRatingStatus(getApplicationContext()) == 1) {
                            PrefManager.setImageUrl(getApplicationContext(),null);
                            Intent intent6 = new Intent(SignalementActivity.this, KiosqueActivity.class);
                            startActivity(intent6);

                            break;

                        } else {
                            break;
                        }
                    case R.id.logout:
                        FacebookSdk.sdkInitialize(getApplicationContext());
                        LoginManager loginManager = LoginManager.getInstance();
                        loginManager.logOut();
                        LoginManager.getInstance().logOut();

                        Intent intent2 = new Intent(SignalementActivity.this, LoginActivity.class);
                        PrefManager.setRatingStatus(getApplicationContext(), 0);
                        PrefManager.setUserId(getApplicationContext(), 0);
                        PrefManager.setUserName(getApplicationContext(), null);
                        PrefManager.setUserMail(getApplicationContext(), null);
                        PrefManager.setUserProfession(getApplicationContext(), null);
                        PrefManager.setUserAdresse(getApplicationContext(), null);
                        PrefManager.setLatitude(getApplicationContext(), null);
                        PrefManager.setLongitude(getApplicationContext(), null);
                        PrefManager.setImageUrl(getApplicationContext(),null);
                        startActivity(intent2);

                        break;
                }
                return true;
            }
        });
    }*/
    /**
     * Refactored by Saly Sakey November-07-2017
     */
    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        new Navigation(SignalementActivity.this, drawerLayout, view);
    }// end function

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void chronometerBehaviour(){
        long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
        if(elapsedMillis>THRESHOLD_EXERCISE / 3){
            chronometer.setTextColor(Color.YELLOW);
        }
        if(elapsedMillis>THRESHOLD_EXERCISE / 2){
            chronometer.setTextColor(Color.RED);
        }
        if(elapsedMillis>THRESHOLD_EXERCISE){
            chronometer.stop();
            // collectEvaluationResult();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            PrefManager.setImageUrl(getApplicationContext(),null);
            Intent intent5 = new Intent(SignalementActivity.this, ImagePickActivity.class);
            startActivity(intent5);
        }
        return true;


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // @TODO Display a dialog to ask a confirmation to the user
    }
}
