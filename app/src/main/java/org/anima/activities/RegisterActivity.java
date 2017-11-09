package org.anima.activities;
import org.anima.animacite.R;

/**
 * Created by thiam on 05/10/2015.
 */
import org.anima.utils.ConfigurationVille;
import org.anima.utils.PrefManager;
import org.anima.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.util.Calendar;

/**
 *
 * Register Activity Class
 */
public class RegisterActivity extends AppCompatActivity {
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Name Edit View Object
    EditText nameET;
    // Email Edit View Object
    EditText emailET;
    // Passwprd Edit View Object
    EditText pwdET,pwdET2;
    // Passwprd Edit View Object
    //EditText sexe;
    String sexxe;
    RadioGroup group;
    // Passwprd Edit View Object
    EditText DN;
    // Passwprd Edit View Object
    private TextView pDisplayDate;
    private boolean avalaible = false;
    private boolean pseudo = false;
    private String name,email,password,password2;

    private int pYear;
    private int pMonth;
    private int pDay;
    private int pMonth2;
    static final int DATE_DIALOG_ID = 0;
    GoogleCloudMessaging gcmObj;
    Context applicationContext;
    String regId = "";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    AsyncTask<Void, Void, String> createRegIdTask;

    public static final String REG_ID = "regId";
    public static final String EMAIL_ID = "eMailId";
    private Tracker mTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);


        Analytics application = (Analytics) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("SignUpScreen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());


        // Find Error Msg Text View control by ID
        group = (RadioGroup) findViewById(R.id.group);
        // Find loklo---kkkkkk--kokoko
        errorMsg = (TextView)findViewById(R.id.register_error);
        // Find Name Edit View control by ID
        nameET = (EditText)findViewById(R.id.registerName);
        // Find Email Edit View control by ID
        emailET = (EditText)findViewById(R.id.registerEmail);
        // Find Password Edit View control by ID
        pwdET = (EditText)findViewById(R.id.registerPassword);
        pwdET2 = (EditText)findViewById(R.id.registerPassword2);
        // Find Password Edit View control by ID
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        pDisplayDate = (TextView) findViewById(R.id.displayDate);

        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);


        /** Display the current date in the TextView */
        updateDisplay();

        //probleme

        SharedPreferences prefs = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");

        /*
        //When Email ID is set in Sharedpref, User will be taken to HomeActivity
        if (!TextUtils.isEmpty(registrationId)) {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            i.putExtra("regId", registrationId);
            startActivity(i);
            finish();
        }
        */

    }

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
        // Get Password ET control value
        password = pwdET.getText().toString();
        password2 = pwdET2.getText().toString();

        // Get Password ET control value
        //String profession = prof.getText().toString();
        // Get Password ET control value
        if(group.getCheckedRadioButtonId() == R.id.masculin){
            sexxe = "M";
        }
        if(group.getCheckedRadioButtonId() == R.id.feminin){
            sexxe = "F";
        }

        //String sexxe = sexe.getText().toString();
        // Get Password ET control value
        pMonth2 =pMonth +1;

        if(name.length()<16){

            if(!password.equals(password2)){
                errorMsg.setText("Les mots de passes ne correspondent pas");
            }else{
                RequestParams params2 = new RequestParams();
                params2.put("psd", name);
                invokeWSPseudo(params2);
            }
        }else{

            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Fails")
                    .setAction("FailToSignUp")
                    .setLabel("FailSignUpPseudoTropLong")
                    .build());

            errorMsg.setText("Le pseudo ne pas doit contenir plus de seize caractères");
        }





    }

    /*

    public void registersocialuser(String name2, String email2,String password2,String sexe2,String niassance2){

        name = name2;
        // Get Email ET control value
        email = email2;
        // Get Password ET control value
        password = password2;
        // Get Password ET control value
        //String profession = prof.getText().toString();
        // Get Password ET control value
        if(sexe2=="M"){
            sexxe = "M";
        }
        if(sexe2=="F"){
            sexxe = "F";
        }
        //String adress = adresse.getText().toString();
        // Get Password ET control value
        //String sexxe = sexe.getText().toString();
        // Get Password ET control value
        pMonth2 =pMonth +1;

        RequestParams params2 = new RequestParams();
        params2.put("psd", name);

        invokeWSPseudo(params2);



    } */

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
        //client.get("http://192.168.0.10:8080/Anima/rest/register/doregister",params ,new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS + "/register/doregister", params, new AsyncHttpResponseHandler() {
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
                        setDefaultValues();
                        // Display successfully registered message using Toast

                        RequestParams params2 = new RequestParams();
                        params2.put("username", email);
                        // Put Http parameter password with value of Password Edit Value control
                        params2.put("password", password);
                        // Invoke RESTful Web Service with Http parameters

                        invokeWSconnexion(params2);


                        //Intent intent3 = new Intent(RegisterActivity.this, LoginActivity.class);

                        //startActivity(intent3);
                    }
                    // Else display error message
                    else {
                        errorMsg.setText(obj.getString("error_msg"));
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


    public void invokeWSconnexion(final RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.14:8080/Anima/rest/login/dologin",params ,new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS+"/login/dologin", params, new AsyncHttpResponseHandler() {
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
                        PrefManager.setUserName(getApplicationContext(), obj.getString("nom"));
                        PrefManager.setUserId(getApplicationContext(), obj.getLong("userId"));
                        PrefManager.setUserMail(getApplicationContext(), obj.getString("mail"));
                        PrefManager.setUserNaiss(getApplicationContext(), obj.getLong("anniv"));
                        PrefManager.setUserProfession(getApplicationContext(), obj.getString("profession"));
                        PrefManager.setUserSexe(getApplicationContext(), obj.getString("sexe"));
                        PrefManager.setLatitude(getApplicationContext(), Double.toString(obj.getDouble("latitude")));
                        PrefManager.setLongitude(getApplicationContext(), Double.toString(obj.getDouble("longitude")));

                        RequestParams params2 = new RequestParams();

                        params2.put("userId", ""+PrefManager.getUserId(getApplicationContext()));
                        // Put Http parameter password with value of Password Edit Value control
                        params2.put("IdAndroid", regId);

                        invokeWSRegistration(params2);
                        //   v.setName(obj.getString("Device"));
                        //Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        PrefManager.setRatingStatus(RegisterActivity.this, 1);
                        // Navigate to Home screen
                        //navigatetoHomeActivity();
                        Intent homeIntent = new Intent(getApplicationContext(), ImagePickActivity.class);
                        Toast.makeText(getApplicationContext(), "Bienvenue   dans la communauté "+PrefManager.getUserName(getApplicationContext())+" :D", Toast.LENGTH_LONG).show();
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                    // Else display error message
                    else {
                        errorMsg.setText("Oups.... Erreur de frappe ? Rentrez d'autres identifiants");
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

    public void invokeWSRegistration(RequestParams params){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.10:8080/Anima/rest/register/doregister",params ,new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS+"/register/doregid", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
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


    public boolean invokeWSPseudo(RequestParams params){

        // Show Progress Dialog
//        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.10:8080/Anima/rest/register/doregister",params ,new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS+"/login/doveripsd", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                //  prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {
                        // Set Default Values for Edit View controls
                        //setDefaultValues();
                        RequestParams params = new RequestParams();
                        // Put Http parameter name with value of Name Edit View control
                        // Put Http parameter username with value of Email Edit View control
                        params.put("mail", email);

                        invokeWSMail(params);

                    }
                    // Else display error message
                    else {
                        pseudoexiste();

                        //errorMsg.setText(obj.getString("error_msg"));
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
                //    prgDialog.hide();
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
        return avalaible;
    }

    public boolean invokeWSMail(RequestParams params){

        // Show Progress Dialog
//     prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.10:8080/Anima/rest/register/doregister",params ,new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS+"/register/doverimail", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                //  prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {
                        // Set Default Values for Edit View controls
                        //setDefaultValues();
                        avalaible = true;
                        // Display successfully registered message using Toast
                        //Toast.makeText(getApplicationContext(), "You are successfully registered!", Toast.LENGTH_LONG).show();

                        enregistrer();

                    }
                    // Else display error message
                    else {
                        mailexiste();

                        //errorMsg.setText(obj.getString("error_msg"));
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
                //    prgDialog.hide();
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
        return avalaible;
    }



    public void enregistrer(){

        //Date date = ;
        // Instantiate Http Request Param Object

        // When Name Edit View, Email Edit View and Password Edit View have values other than Null
        if(Utility.isNotNull(name) && Utility.isNotNull(email) && Utility.isNotNull(password)){
            // When Email entered is Valid
            if(Utility.validate(email)){


                if (checkPlayServices()){

                    // Register Device in GCM Server
                    registerInBackground(email);
                }
                //storeRegIdinSharedPref(applicationContext, regId, email);
                //registerInBackground(email);

            }
            // When Email is invalid
            else{

                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Fails")
                        .setAction("FailToSignUp")
                        .setLabel("FailSignUpAdresseNonValide")
                        .build());

                errorMsg.setText("Votre email n'est pas valide");
                // Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
            }
        }
        // When any of the Edit View control left blank
        else{

            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Fails")
                    .setAction("FailToSignUp")
                    .setLabel("FailSignUpChampsVide")
                    .build());


            errorMsg.setText("Vous êtes pressés ? Rentrez tous les champs s'il vous plaît :)");
            //Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }

    }

    public void pseudoexiste(){

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Fails")
                .setAction("FailToSignUp")
                .setLabel("FailSignUpPseudoDejaPris")
                .build());

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
        // Setting Dialog Title
        alertDialog.setIcon(R.drawable.petite_image);
        // Setting Dialog Message
        alertDialog.setMessage("Ce pseudo existe déja");
        // Setting Positive "Yes" Button

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel Dialog
                        dialog.cancel();
                    }
                });
        // Showing Alert Message
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void mailexiste(){

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Fails")
                .setAction("FailToSignUp")
                .setLabel("FailSignUpAdresseMailDejaPrise")
                .build());

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
        // Setting Dialog Title
        alertDialog.setIcon(R.drawable.petite_image);
        // Setting Dialog Message
        alertDialog.setMessage("Cette adresse mail existe déja");
        // Setting Positive "Yes" Button

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel Dialog
                        dialog.cancel();
                    }
                });
        // Showing Alert Message
        AlertDialog alert = alertDialog.create();
        alert.show();
    }
    /**
     * Method which navigates from Register Activity to Login Activity
     */
    public void navigatetoLoginActivity(View view){

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Fails")
                .setAction("WentBackWhileSignUp")
                .setLabel("WentBackSignUp")
                .build());

        Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
        // Clears History of Activity
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    /**
     * Set degault values for Edit View controls
     */
    public void setDefaultValues(){
        nameET.setText("");
        emailET.setText("");
        pwdET.setText("");
    }

    private void registerInBackground(final String emailID) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regId = gcmObj.register("455491445841");
                    msg = "Registration ID :" + regId;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {
                    // Store RegId created by GCM Server in SharedPref
                    storeRegIdinSharedPref(applicationContext, regId, emailID);
                    //  Toast.makeText(applicationContext, "Registered with GCM Server successfully.nn" + msg, Toast.LENGTH_SHORT).show();
                } else {
                    //  Toast.makeText(applicationContext, "Reg ID Creation Failed.nnEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time." + msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }

    // Store  RegId and Email entered by User in SharedPref
    private void storeRegIdinSharedPref(Context context, String regId,
                                        String emailID) {
        //SharedPreferences prefs = getSharedPreferences("UserDetails",
        //      Context.MODE_PRIVATE);
        // PrefManager.setRegistrationID(context,regId);
        //SharedPreferences.Editor editor = prefs.edit();
        //editor.putString(REG_ID, regId);

//        editor.putString(EMAIL_ID, emailID);
        //editor.commit();
        //storeRegIdinServer(params3);
        RequestParams params = new RequestParams();
        // Put Http parameter name with value of Name Edit View control
        params.put("name", name);
        // Put Http parameter username with value of Email Edit View control
        params.put("username", email);
        // Put Http parameter password with value of Password Edit View control
        params.put("password", password);


        //params.put("regId",regId);
        // Put Http parameter password with value of Password Edit View control
        //params.put("profession", profession);
        Calendar calendar = Calendar.getInstance();
        calendar.set(pYear, pMonth, pDay);

        params.put("naiss", ""+calendar.getTimeInMillis());

        // Put Http parameter password with value of Password Edit View control
        //params.put("sexe", sexxe);
        params.put("sexe", sexxe);
        // Invoke RESTful Web Service with Http parameters
        invokeWS(params);

    }

    /*


    // Share RegID with GCM Server Application (Php)
    private void storeRegIdinServer(RequestParams params3) {
        prgDialog.show();
       // RequestParams params3 = new RequestParams();
        //params3.put("regId", regId);
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://192.168.2.4:9000/gcm/gcm.php?shareRegId=true", params3,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        if (prgDialog != null) {
                            prgDialog.dismiss();
                        }
                        Toast.makeText(applicationContext,
                                "Reg Id shared successfully with Web App ",
                                Toast.LENGTH_LONG).show();
                        Intent i = new Intent(applicationContext,
                                LoginActivity.class);
                        i.putExtra("regId", regId);
                        startActivity(i);
                        finish();
                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        if (prgDialog != null) {
                            prgDialog.dismiss();
                        }
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(applicationContext,
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(applicationContext,
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    applicationContext,
                                    "Unexpected Error occcured! [Most common Error: Device might "
                                            + "not be connected to Internet or remote server is not up and running], check for other errors as well",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    */

    // Check if Google Playservices is installed in Device or not
    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getApplicationContext());
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //   Toast.makeText(applicationContext, "This device doesn't support Play services, App will not work normally", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        } else {
            //   Toast.makeText(applicationContext, "This device supports Play services, App will work normally", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    // When Application is resumed, check for Play services support to make sure app will be running normally
    @Override
    protected void onResume() {
        super.onResume();
        //checkPlayServices();
    }
}

