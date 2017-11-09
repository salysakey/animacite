package org.anima.activities;
import org.anima.animacite.R;
/**
 * Created by thiam on 05/10/2015.
 */
import org.anima.adapters.CustomOnItemSelectedListener;
import org.anima.helper.Navigation;
import org.anima.utils.ConfigurationVille;
import org.anima.utils.GeocodingLocation;
import org.anima.utils.PrefManager;
import org.anima.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 *
 * Register Activity Class
 */
public class InfoContactActivity extends AppCompatActivity {
    private String TAG = "InfoContactActivity";
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Name Edit View Object
    EditText nameET,newmdpET,newmdpET2,adresse,ville;
    // Email Edit View Object
    EditText emailET;

    EditText prof;
    String name, profession,email,newmdp,newmdp2,oldmdp,address;

    private Toolbar toolbar;
    private Double latitude,longitude;
    private DrawerLayout drawerLayout;
    private Menu menudeux;
    private Spinner profess;
    private List<String> list = new ArrayList<String>();
    private Tracker mTracker;

    //private final Calendar cal2 = Calendar.getInstance();
    //RadioGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, " - OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_update);

        Analytics application = (Analytics) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("ModifierProfil");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        initToolbar();

        // Find Error Msg Text View control by ID
        errorMsg = (TextView)findViewById(R.id.register_error);
        // Find Name Edit View control by ID
        nameET = (EditText)findViewById(R.id.registerName);

        emailET = (EditText)findViewById(R.id.mail);

        adresse = (EditText) findViewById(R.id.adresse);
        ville = (EditText) findViewById(R.id.ville);

        newmdpET = (EditText)findViewById(R.id.Newpassword);

        newmdpET2 = (EditText)findViewById(R.id.Newpassword2);

        profess = (Spinner) findViewById(R.id.profession);
        profess.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        addItemsOnSpinner();

        /** Display the current date in the TextView */
        //pDisplayDate.setText(PrefManager.getUserNaiss(getApplicationContext()));

        //updateDisplay();
        // Find Password Edit View control by ID
        //sexe = (EditText)findViewById(R.id.sexe);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        setDefaultValues();


        setupDrawerLayout();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Fails")
                .setAction("wentBackWhileModifyingProfil")
                .setLabel("FailModifProfil")
                .build());

        return true;


    }

    public void addItemsOnSpinner() {


        list.add("Votre profession ?");
        list.add("Agriculteur");
        list.add("Artisan/Commerçant");
        list.add("Cadre");
        list.add("Employé");
        list.add("Ouvrier");
        list.add("Étudiant");
        list.add("Retraité");
        list.add("Sans emploi");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profess.setAdapter(dataAdapter);
    }
 /*
    public void navigatetoRegisterActivity(View view){
        showDialog(DATE_DIALOG_ID);

    }


    private DatePickerDialog.OnDateSetListener pDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    pYear = year;
                    pMonth = monthOfYear;
                    pDay = dayOfMonth;
                    updateDisplay();
                }
            };


    private void updateDisplay() {

        pDisplayDate.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(pDay).append("/")
                        .append(pMonth + 1).append("/")
                        .append(pYear).append(" "));
    }



    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, pDateSetListener, pYear, pMonth, pDay);
        }
        return null;
    }
     */
    /**
     * Method gets triggered when Register button is clicked
     *
     * @param view
     */
    public void registerUser(View view){

        // Get NAme ET control value
        name = nameET.getText().toString();
        // Get Email ET control value
        email = emailET.getText().toString();

        newmdp = newmdpET.getText().toString();

        newmdp2 = newmdpET2.getText().toString();

        if(profess.getSelectedItemPosition()==0){

            profession="Aucun";

        }else{
            profession = profess.getSelectedItem().toString();

        }

        address = adresse.getText().toString()+" "+ville.getText().toString();

        GeocodingLocation locationAddress = new GeocodingLocation();
        locationAddress.getAddressFromLocation(address, getApplicationContext(), new GeocoderHandler());



        //profession = profess.get
        // Get Password ET control value
        //String password = pwdET.getText().toString();
        // Get Password ET control value
        //profession = prof.getText().toString();
        // Get Password ET control value
        //adress = adresse.getText().toString();
        // Get Password ET control value

        //pMonth2 =pMonth +1;
        // Get Password ET control value
        // Instantiate Http Request Param Object

        // When Name Edit View, Email Edit View and Password Edit View have values other than Null
        if(Utility.isNotNull(name)&Utility.isNotNull(email)){

            if(!name.equals(PrefManager.getUserName(getApplicationContext()))){

                if(name.length()<16){

                    RequestParams params2 = new RequestParams();
                    // Put Http parameter password with value of Password Edit Value control
                    params2.put("psd", name);
                    // Invoke RESTful Web Service with Http parameters
                    invokeWSPseudo(params2);


                }else{

                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Fails")
                            .setAction("FailToModifProfil")
                            .setLabel("FailModifProfilPseudoTropLong")
                            .build());

                    errorMsg.setText("Le pseudo doit contenir moins de seize caractères.");


                }



            }else if(!email.equals(PrefManager.getUserMail(getApplicationContext()))){

                if(Utility.validate(email)){

                    RequestParams params2 = new RequestParams();
                    // Put Http parameter password with value of Password Edit Value control
                    params2.put("mail", email);
                    // Invoke RESTful Web Service with Http parameters
                    invokeWSmail(params2);


                }else{

                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Fails")
                            .setAction("FailToModifProfil")
                            .setLabel("FailModifProfilAdresseNonValide")
                            .build());

                    errorMsg.setText("Le format de l'adresse mail est incorrect");

                }


            }else{

                enregistrer();

            }




            // Put Http parameter name with value of Name Edit View control

            // Put Http parameter password with value of Password Edit View control


            //    cal2.set(pYear,pMonth,pDay);
            //  params.put("naiss", "" + cal2.getTimeInMillis());
        /*    if(pMonth2 < 10 && pDay >= 10){
                params.put("naiss", ""+pYear+"0"+pMonth2+pDay);
            }
            if(pDay < 10 && pMonth2 >= 10 ){
                params.put("naiss", ""+pYear+pMonth2+"0"+pDay);

            }
            if(pDay < 10 && pMonth2 < 10 ){

                params.put("naiss", ""+pYear+"0"+pMonth2+"0"+pDay);

            }
            if(pDay >= 10 && pMonth2 >= 10 ){

                params.put("naiss", ""+pYear+pMonth2+pDay);

            } */


            // Put Http parameter password with value of Password Edit View control
            // params.put("adresse", adress);
            // Put Http parameter password with value of Password Edit View control

            // Invoke RESTful Web Service with Http parameters


        }
        // When any of the Edit View control left blank
        else{

            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Fails")
                    .setAction("FailToModifProfil")
                    .setLabel("FailModifProfilChampsVide")
                    .build());

            Toast.makeText(getApplicationContext(), "Le pseudo ou l'email est vide", Toast.LENGTH_LONG).show();
        }

    }
    public void invokeWSPseudo(RequestParams params){

        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.10:8080/Anima/rest/register/doregister",params ,new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS + "/login/doveripsd", params, new AsyncHttpResponseHandler() {
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

                        if (!email.equals(PrefManager.getUserMail(getApplicationContext()))) {

                            if (Utility.validate(email)) {

                                RequestParams params2 = new RequestParams();
                                // Put Http parameter password with value of Password Edit Value control
                                params2.put("mail", email);
                                // Invoke RESTful Web Service with Http parameters
                                invokeWSmail(params2);


                            } else {

                                mTracker.send(new HitBuilders.EventBuilder()
                                        .setCategory("Fails")
                                        .setAction("FailToModifProfil")
                                        .setLabel("FailModifProfilAdresseNonValide")
                                        .build());

                                errorMsg.setText("Le format de l'adresse mail est incorrect");


                            }

                        } else {

                            enregistrer();

                        }


                    }
                    // Else display error message
                    else {
                        //pseudoexiste();

                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory("Fails")
                                .setAction("FailToModifProfil")
                                .setLabel("FailModifPseudoDejaPris")
                                .build());

                        errorMsg.setText("Trop tard, le pseudo " + name + " existe déjà");
                        //Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void invokeWSmail(RequestParams params){

        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.10:8080/Anima/rest/register/doregister",params ,new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS + "/register/doverimail", params, new AsyncHttpResponseHandler() {
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


                        enregistrer();

                    }
                    // Else display error message
                    else {
                        //pseudoexiste();

                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory("Fails")
                                .setAction("FailToModifProfil")
                                .setLabel("FailModifMailDejaPris")
                                .build());

                        errorMsg.setText("Le mail " + email + " existe déjà");
                        //Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void enregistrer(){


        if (!newmdp.equals("null") && !newmdp.isEmpty() && newmdp != "null") {

            if(!newmdp.equals(newmdp2)){

                errorMsg.setText("Les mots de passes ne correspondent pas");

            }else{

                enregistrer2();

            }


        }else{

            enregistrer2();


        }






    }

    public void enregistrer2(){


        // When Email entered is Valid
        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.alertdialogperso, null);

        //Création de l'AlertDialog
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
        adb.setView(alertDialogView);

        //On donne un titre à l'AlertDialog
        adb.setTitle("Entrez votre mot de passe");

        //On modifie l'icône de l'AlertDialog pour le fun ;)
        adb.setIcon(android.R.drawable.ic_dialog_alert);

        //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //Lorsque l'on cliquera sur le bouton "OK", on récupère l'EditText correspondant à notre vue personnalisée (cad à alertDialogView)
                EditText et = (EditText)alertDialogView.findViewById(R.id.EditText1);

                oldmdp = et.getText().toString();

                RequestParams params = new RequestParams();
                params.put("username", PrefManager.getUserMail(getApplicationContext()));
                // Put Http parameter password with value of Password Edit Value control
                params.put("password", oldmdp);
                // Invoke RESTful Web Service with Http parameters
                invokeWSValiation(params);



                //On affiche dans un Toast le texte contenu dans l'EditText de notre AlertDialog
                //Toast.makeText(Tutoriel18_Android.this, et.getText(), Toast.LENGTH_SHORT).show();
            } });

        //On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un évènement
        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Lorsque l'on cliquera sur annuler on quittera l'application
                finish();
            }
        });
        adb.show();



    }

    public void invokeWSValiation(RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.14:8080/Anima/rest/login/dologin",params ,new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS + "/login/dologin", params, new AsyncHttpResponseHandler() {
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

                        RequestParams params = new RequestParams();


                        params.put("psd", name);
                        // Put Http parameter username with value of Email Edit View control
                        params.put("userId", "" + PrefManager.getUserId(getApplicationContext()));
                        params.put("mail", email);
                        // Put Http parameter password with value of Password Edit View control
                        //params.put("password", password);
                        // Put Http parameter password with value of Password Edit View control
                        params.put("profession", profession);

                        params.put("latitude", "" + latitude);
                        params.put("longitude", "" + longitude);

                        if (!newmdp.equals("null") && !newmdp.isEmpty() && newmdp != "null") {

                            params.put("mdp", newmdp);
                        }

                        invokeWS(params);


                    }
                    // Else display error message
                    else {

                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory("Fails")
                                .setAction("FailToModifProfil")
                                .setLabel("FailModifProfilFauxMdp")
                                .build());

                        errorMsg.setText("Oups.... Erreur de frappe ? le mot de passe n'est pas correct");
                        // Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(RequestParams params){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ConfigurationVille.Debut_WS+"/update/doupdate", params, new AsyncHttpResponseHandler() {
            //client.get("http://192.168.0.10:8080/Anima/rest/update/doupdate", params, new AsyncHttpResponseHandler() {
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
                        // Set Default Values for Edit View controls
                        PrefManager.setUserName(getApplicationContext(), name);
                        PrefManager.setUserMail(getApplicationContext(), email);
                        // PrefManager.setUserAdresse(getApplicationContext(), adress);
                        PrefManager.setUserProfession(getApplicationContext(), profession);
                        PrefManager.setLongitude(getApplicationContext(), String.valueOf(longitude));
                        PrefManager.setLatitude(getApplicationContext(), String.valueOf(latitude));
                        //  PrefManager.setUserNaiss(getApplicationContext(), cal2.getTimeInMillis() );
                        //PrefManager.setUserSexe(getApplicationContext(),sexxe);

                        setDefaultValues();
                        // Display successfully registered message using Toast
                        Toast.makeText(getApplicationContext(), "Changements effectués", Toast.LENGTH_LONG).show();
                        Intent lolo = new Intent(getApplicationContext(), MyAccountActivity.class);
                        startActivity(lolo);

                    }
                    // Else display error message
                    else {
                        errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Method which navigates from Register Activity to Login Activity
     */
    public void navigatetoLoginActivity(View view){
        Intent loginIntent = new Intent(getApplicationContext(),MyAccountActivity.class);
        // Clears History of Activity
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    /**
     * Set degault values for Edit View controls
     */
    public void setDefaultValues(){
        nameET.setText(PrefManager.getUserName(getApplicationContext()));
        emailET.setText(PrefManager.getUserMail(getApplicationContext()));
        //
        if(PrefManager.getUserProfession(getApplicationContext())!="Aucun"&&PrefManager.getUserProfession(getApplicationContext())!="null"){
            int i = list.indexOf(PrefManager.getUserProfession(getApplicationContext()));
            profess.setSelection(i);


        }
        //Log.i("yoyo",PrefManager.getLatitude(getApplicationContext()));

        if(!PrefManager.getLatitude(getApplicationContext()).equals("0.0") && !PrefManager.getLongitude(getApplicationContext()).equals("0.0")){

            adresse.setText(adress(PrefManager.getLatitude(getApplicationContext()),PrefManager.getLongitude(getApplicationContext())));
            ville.setText(adress2(PrefManager.getLatitude(getApplicationContext()),PrefManager.getLongitude(getApplicationContext())));

        }



        // Get Password ET control value
        //adresse.setText(PrefManager.getUserAdresse(getApplicationContext()));
        //pDisplayDate.setText(getDate(PrefManager.getUserNaiss(getApplicationContext()), "dd/MM/yyyy"));

        //if(PrefManager.getUserSexe(getApplicationContext())=="M"){
        //    group.check(R.id.masculin);
        //}else{
        //    group.check(R.id.feminin);
        //}
        // Get Password ET control value
        // Get Password ET control value


    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Modifier mes infos");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }
    private String adress(String latitude,String longitude){

        Location location = new Location("");
        location.setLatitude(Double.parseDouble(latitude));
        location.setLongitude(Double.parseDouble(longitude));


        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // lat=String.valueOf(Double.parseDouble(latitude));
        // longi=String.valueOf(Double.parseDouble(longitude));
        // Toast.makeText(getApplicationContext(), addresses.toString(), Toast.LENGTH_LONG).show();

        return addresses.get(0).getAddressLine(0);

    }
    private String adress2(String latitude,String longitude){

        Location location = new Location("");
        location.setLatitude(Double.parseDouble(latitude));
        location.setLongitude(Double.parseDouble(longitude));


        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // lat=String.valueOf(Double.parseDouble(latitude));
        // longi=String.valueOf(Double.parseDouble(longitude));

        return addresses.get(0).getAddressLine(1);

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
                        startActivity(new Intent(InfoContactActivity.this, ConcoursActivity.class));
                        break;
                    case R.id.panier:
                        Intent intent4 = new Intent(InfoContactActivity.this, FollowActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.my_account:
                        Intent intent3 = new Intent(InfoContactActivity.this, MyAccountActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.actu:
                        Intent intent5 = new Intent(InfoContactActivity.this, ImagePickActivity.class);
                        startActivity(intent5);
                        break;
                    case R.id.evenements:

                        if (PrefManager.getRatingStatus(getApplicationContext())==1) {
                            Intent intent8 = new Intent(InfoContactActivity.this, CalendarActivity.class);
                            startActivity(intent8);
                            break;

                        }else{
                            break;

                        }
                    case R.id.maps:

                        Intent intent9 = new Intent(InfoContactActivity.this, MapActivity.class);
                        startActivity(intent9);
                        break;
                    case R.id.infopratique:
                        if (PrefManager.getRatingStatus(getApplicationContext())==1) {
                            Intent pratique = new Intent(InfoContactActivity.this, PratiqueActivity.class);
                            startActivity(pratique);
                            break;

                        }else{
                            break;

                        }
                    case R.id.kiosque:

                        if (PrefManager.getRatingStatus(getApplicationContext())==1) {
                            Intent intent6 = new Intent(InfoContactActivity.this, KiosqueActivity.class);
                            startActivity(intent6);

                            break;

                        }else{
                            break;
                        }
                    case R.id.logout:
                        FacebookSdk.sdkInitialize(getApplicationContext());
                        LoginManager loginManager = LoginManager.getInstance();
                        loginManager.logOut();
                        LoginManager.getInstance().logOut();

                        Intent intent2 = new Intent(InfoContactActivity.this, LoginActivity.class);
                        PrefManager.setRatingStatus(getApplicationContext(), 0);
                        PrefManager.setUserName(getApplicationContext(), null);
                        PrefManager.setUserMail(getApplicationContext(), null);
                        PrefManager.setUserProfession(getApplicationContext(), null);
                        PrefManager.setUserAdresse(getApplicationContext(), null);
                        PrefManager.setLatitude(getApplicationContext(), null);
                        PrefManager.setLongitude(getApplicationContext(), null);
                        startActivity(intent2);

                        break;
                }
                return true;
            }
        });
    }
    */
    /**
     * Refactored by Saly Sakey November-07-2017
     */
    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        new Navigation(InfoContactActivity.this, drawerLayout, view);
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

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;

            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    // locationAddress = bundle.getString("address");
                    latitude = bundle.getDouble("latitude");
                    longitude = bundle.getDouble("longitude");
                    break;
                default:
                    locationAddress = null;
            }
            //latLongTV.setText(locationAddress)
            // ;
            //Toast.makeText(getApplicationContext(), locationAddress, Toast.LENGTH_LONG).show();

        }
    }

}
