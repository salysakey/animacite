package org.anima.activities;
import org.anima.utils.ConfigurationVille;
import org.anima.animacite.R;

import org.anima.utils.PrefManager;
import org.anima.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;



/**
 * Login Activity Class
 */
public class LoginActivity extends AppCompatActivity {

    private String TAG = "LoginActivity";
    // Progress Dialog Object
    ProgressDialog prgDialog;
    GoogleCloudMessaging gcmObj;
    private String name,email,password,sexxe,psswd;
    private long datemilli;
    private long fcbk_id;
    private RequestParams lastparams = new RequestParams();
    // Error Msg TextView Object
    TextView errorMsg;
    private Tracker mTracker;
    TextView errorMsg2;
    Context applicationContext;
    // Email Edit View Object
    EditText emailET;
    String regId = "";
    int i=0;

    // Passwprd Edit View Object
    EditText pwdET;
    public static final String REG_ID = "regId";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private TextView info_facebook_request;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    // User v = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Analytics application = (Analytics) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("LoginScreen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        String str = getIntent().getStringExtra("msg");

        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.error);
        // Find Email Edit View control by ID
        emailET = (EditText) findViewById(R.id.login);
        // Find Password Edit View control by ID
        pwdET = (EditText) findViewById(R.id.password);
        String regId = "";


        //getregistrationId();

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);



        // Toast.makeText(getApplicationContext(),  printKeyHash(this), Toast.LENGTH_LONG).show();

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        info_facebook_request = (TextView)findViewById(R.id.info_facebook_request);
        loginButton = (LoginButton)findViewById(R.id.facebookbutton);
        //AccessToken.setCurrentAccessToken(null);
        //AccessToken.getCurrentAccessToken();
        //loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        Log.d(TAG," -  Before register callback");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                if(i==0){
                    Log.d(TAG," -  In the IF of i=0");
                    LoginManager.getInstance().logInWithPublishPermissions(LoginActivity.this , Arrays.asList("publish_actions"));
                    loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
                    i++;
                }
                else {
                    Log.d(TAG," -  In the ELSE of i!=0");
                }

                if (AccessToken.getCurrentAccessToken() != null) {
                    Log.d(TAG," -  In the IF of Acess Token");
                    //loginResult.getAccessToken().getPermissions();
                    GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            //JSONObject json = response.getJSONObject();
                            String text = null;

                            if (object != null) {
                                try {
                                    //Profile profile = Profile.getCurrentProfile();
                                    Log.d(TAG, "In the try.");
                                    //text = object.getString("email");

                                    long id = 0;

                                    String gender = "";
                                    String birthday ="01/01/2017";
                                    String name = "";
                                    String email = "facebook";

                                    if (object.has("id"))
                                        id = object.getLong("id");
                                    if (object.has("name"))
                                        name = object.getString("name");
                                    if (object.has("email"))
                                        email = object.getString("email");
                                    else
                                        email = name.replaceAll(" ",".") + "@compte-facebook.com";
                                    if (object.has("gender"))
                                        gender = object.getString("gender");
                                    if (object.has("birthday"))
                                        gender = object.getString("birthday");

                                    Log.d(TAG, "Recieved data from Facebook: " + response.toString());

                                    registerSocialUser(id, email, "mdpfcbkanima", gender, birthday);
                                    //  Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender,birthday");
                    request.setParameters(parameters);
                    request.executeAsync();
                    //Bundle parameters = new Bundle();
                    //parameters.putString("fields", "id,name,email,gender, birthday");
                    //request.setParameters(parameters);
                    //request.executeAsync();
                    //navigatetoHomeActivity();
                }
                else{
                    Log.d(TAG," -  No current Token");
                }
            }

            @Override
            public void onCancel() {
                Log.d(TAG, " - Facebook cancel");
                Toast.makeText(getApplicationContext(), "Login attempt canceled.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Log.d(TAG," - Error Facebook register callback");
                //Log.d("LoginActivity", e.getCause().toString());
                Toast.makeText(getApplicationContext(), "Login attempt failed.", Toast.LENGTH_LONG).show();
            }
        });





        // Check if Google Play Service is installed in Device
        // Play services is needed to handle GCM stuffs
        /*
        if (!checkPlayServices()) {
            Toast.makeText(
                    getApplicationContext(),
                    "This device doesn't support Play services, App will not work normally",
                    Toast.LENGTH_LONG).show();
        }
        */

        // When Message sent from Broadcase Receiver is not empty
        // voir sur http://programmerguru.com/android-tutorial/how-to-send-push-notifications-using-gcm-service/


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent5 = new Intent(LoginActivity.this, ImagePickActivity.class);
            startActivity(intent5);
        }
        return true;
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    public void registerSocialUser(Long name2, String email2,String password2,String sexe2,String naissance2){
        Log.d(TAG, " - LoginActivity.registersocialuser");
        fcbk_id = name2;
        // Get Email ET control value
        email = email2;

        // Get Password ET control value
        password = password2;
        // Get Password ET control value
        //String profession = prof.getText().toString();
        // Get Password ET control value
        if(sexe2.equals("male")){
            sexxe = "M";
        }
        if(sexe2.equals("female")){
            sexxe = "F";
        }
        datemilli = getDatemilliseconde(naissance2);

        //String adress = adresse.getText().toString();
        // Get Password ET control value
        //String sexxe = sexe.getText().toString();
        // Get Password ET control value
        //pMonth2 =pMonth +1;

        name="totodu92";

        RequestParams params2 = new RequestParams();
        params2.put("id_fcbk", ""+fcbk_id);

        invokeWSid(params2);
    }

    public long getDatemilliseconde(String someDate){

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        Date date = null;
        try {

            date = sdf.parse(someDate);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        System.out.println(date.getTime());

        return date.getTime();


    }



    /**
     * Method gets triggered when Login button is clicked
     *
     * @param view
     */
    public void loginUser(View view) {
        // Get Email Edit View Value
        String email = emailET.getText().toString();
        // Get Password Edit View Value
        String password = pwdET.getText().toString();
        // Instantiate Http Request Param Object
        RequestParams params = new RequestParams();
        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(email) && Utility.isNotNull(password)) {
            // When Email entered is Valid
            if (Utility.validate(email)) {
                Log.d(TAG, "MAIL VALIDE");
                // Put Http parameter username with value of Email Edit View control
                //params.put("username", email);
                params.put("psd", email);
                // Put Http parameter password with value of Password Edit Value control
                params.put("password", password);
                // Invoke RESTful Web Service with Http parameters
                invokeWS(params);
            }
            // When Email is invalid
            else {
                Log.d(TAG, "MAIL NON VALIDE");
                params.put("psd", email);
                params.put("password", password);
                // Invoke RESTful Web Service with Http parameters
                invokeWS(params);
                //Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
            }
        } else {
            errorMsg.setText("Ne laissez aucun champ libre");
            //Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();
        Log.d(TAG, " - PrgDialog InvokeWS");
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
                        PrefManager.setFirstSignalement(getApplicationContext(),false);
                        PrefManager.setUserName(getApplicationContext(), obj.getString("nom"));
                        PrefManager.setUserId(getApplicationContext(), obj.getLong("userId"));
                        PrefManager.setUserMail(getApplicationContext(), obj.getString("mail"));
                        PrefManager.setUserNaiss(getApplicationContext(), obj.getLong("anniv"));
                        PrefManager.setUserProfession(getApplicationContext(), obj.getString("profession"));
                        PrefManager.setUserSexe(getApplicationContext(), obj.getString("sexe"));
                        PrefManager.setLatitude(getApplicationContext(), Double.toString(obj.getDouble("latitude")));
                        PrefManager.setLongitude(getApplicationContext(), Double.toString(obj.getDouble("longitude")));
                        //   v.setName(obj.getString("Device"));

                        RequestParams params2 = new RequestParams();

                        params2.put("userId", "" + PrefManager.getUserId(getApplicationContext()));
                        params2.put("IdAndroid", regId);
                        Log.d(TAG, "regid" + regId );
                        invokeWSRegistration(params2);

                        // Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        PrefManager.setRatingStatus(LoginActivity.this, 1);
                        // Navigate to Home screen
                        navigatetoHomeActivity();
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

    public void invokeWSRegistration(RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();
        Log.d(TAG, " - PrgDialog invokeWSRegistration");
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.14:8080/Anima/rest/login/dologin",params ,new AsyncHttpResponseHandler() {
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

    public void invokeWSregister(RequestParams params){
        // Show Progress Dialog
        prgDialog.show();
        Log.d(TAG, " - PrgDialog invokeWSRegister");
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.10:8080/Anima/rest/register/doregister",params ,new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS+"/register/doregister", params, new AsyncHttpResponseHandler() {
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
                        // Display successfully registered message using Toast

                        RequestParams params2 = new RequestParams();
                        params2.put("username", email);
                        // Put Http parameter password with value of Password Edit Value control
                        params2.put("password", psswd);
                        // Invoke RESTful Web Service with Http parameters

                        invokeWS(params2);


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

    /**
     * Method which navigates from Login Activity to Home Activity
     */
    public void navigatetoHomeActivity() {
        Log.d(TAG," - Navigate to home");
        prgDialog.hide();
        Intent homeIntent = new Intent(getApplicationContext(), ImagePickActivity.class);
        //homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    /**
     * Method gets triggered when Register button is clicked
     *
     * @param view
     */

    public void navigatetoRegisterActivity(View view){


        Intent loginIntent = new Intent(getApplicationContext(),RegisterActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);

    }

    public void invokeWSPseudo(RequestParams params){

        // Show Progress Dialog
//        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.10:8080/Anima/rest/register/doregister",params ,new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS + "/login/doveripsd", params, new AsyncHttpResponseHandler() {
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

                        // Display successfully registered message using Toast
                        //Toast.makeText(getApplicationContext(), "You are successfully registered!", Toast.LENGTH_LONG).show();

                        lastparams.put("name", name);
                        lastparams.put("username", email);
                        lastparams.put("password", psswd);
                        invokeWSregister(lastparams);

                    }
                    // Else display error message
                    else {


                        // pseudoexiste();

                        errorMsg2.setText("Le pseudo est déjà pris :/ ");
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

    }

    public void invokeWSid(RequestParams params){
        Log.d(TAG, " - LoginAciticty.invokeWSid");
        // Show Progress Dialog
        //prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.10:8080/Anima/rest/register/doregister",params ,new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS+"/login/doveriid", params, new AsyncHttpResponseHandler() {
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

                        PrefManager.setUserName(getApplicationContext(), obj.getString("nom"));
                        PrefManager.setUserId(getApplicationContext(), obj.getLong("userId"));
                        PrefManager.setUserMail(getApplicationContext(), obj.getString("mail"));
                        PrefManager.setUserNaiss(getApplicationContext(), obj.getLong("anniv"));
                        PrefManager.setUserProfession(getApplicationContext(), obj.getString("profession"));
                        PrefManager.setLatitude(getApplicationContext(), Double.toString(obj.getDouble("latitude")));
                        PrefManager.setLongitude(getApplicationContext(), Double.toString(obj.getDouble("longitude")));
                        //PrefManager.setUserAdresse(getApplicationContext(), obj.getString("adresse"));
                        PrefManager.setUserSexe(getApplicationContext(), obj.getString("sexe"));

                        PrefManager.getLatitude(getApplicationContext());
                        //   v.setName(obj.getString("Device"));
                        //Toast.makeText(getApplicationContext(),  PrefManager.getLatitude(getApplicationContext()), Toast.LENGTH_LONG).show();
                        PrefManager.setRatingStatus(LoginActivity.this, 1);
                        // Navigate to Home screen
                        navigatetoHomeActivity();

                    }
                    // Else display error message
                    else {

                        enregistrer();
                        // pseudoexiste();
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

    }

    public void enregistrer(){
        Log.d(TAG," - LoginActivity.enregistrer");
        //Date date = ;
        // Instantiate Http Request Param Object

        // When Name Edit View, Email Edit View and Password Edit View have values other than Null
        if(Utility.isNotNull(email)){
            // When Email entered is Valid
            if(Utility.validate(email)){


                if (checkPlayServices()){

                    //Register Device in GCM Server
                    registerInBackground(email);
                }
                else{
                    Log.d(TAG, " - check play is not");
                }
                storeRegIdinSharedPref(applicationContext, regId, email);
                //registerInBackground(email);

            }
            // When Email is invalid
            else{
                Log.d(TAG," - LoginActivity.enregistrer Invalid mail");
                errorMsg.setText("Votre email n'est pas valide");
                // Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
            }
        }
        // When any of the Edit View control left blank
        else{
            errorMsg.setText("Vous n'avez pas d'email :/");
            //Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }

    }

    private void registerInBackground(final String emailID) {
        Log.d(TAG, " - LoginActivity.registerInBackground");
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
                    //storeRegIdinSharedPref(applicationContext, regId, emailID);
                    //  Toast.makeText(applicationContext, "Registered with GCM Server successfully.nn" + msg, Toast.LENGTH_SHORT).show();
                } else {
                    //  Toast.makeText(applicationContext, "Reg ID Creation Failed.nnEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time." + msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }

    public void getregistrationId(){
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
                    //    storeRegIdinSharedPref(applicationContext, regId, emailID);
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
        Log.d(TAG, " - LoginActivity.storeRegIdinSharedPref");
        //SharedPreferences prefs = getSharedPreferences("UserDetails",
        //      Context.MODE_PRIVATE);
        // PrefManager.setRegistrationID(context,regId);
        //SharedPreferences.Editor editor = prefs.edit();
        //editor.putString(REG_ID, regId);

//        editor.putString(EMAIL_ID, emailID);
        //editor.commit();
        //storeRegIdinServer(params3);

        // Put Http parameter name with value of Name Edit View control
        //lastparams.put("name", name);
        // Put Http parameter username with value of Email Edit View control
        lastparams.put("username", email);
        // Put Http parameter password with value of Password Edit View control
        lastparams.put("password", password);

        lastparams.put("id_fcbk", ""+fcbk_id);
        //params.put("id_fcbk", ""+fcbk_id);

        lastparams.put("regId",regId);
        // Put Http parameter password with value of Password Edit View control
        //params.put("profession", profession);
        Calendar calendar = Calendar.getInstance();
        //calendar.set(pYear, pMonth, pDay);

        lastparams.put("naiss", "" + datemilli);
        // Put Http parameter password with value of Password Edit View control
        //params.put("sexe", sexxe);
        lastparams.put("sexe", sexxe);
        // Invoke RESTful Web Service with Http parameters

        derniereEtape();


    }


    public void navigatetoForgetmdp(View view){

        /*

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Bientôt disponible");
        alertDialog.setIcon(R.drawable.petite_image);
        alertDialog.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel Dialog
                        dialog.cancel();
                    }
                });
        // Showing Alert Message
        AlertDialog alert = alertDialog.create();
        alert.show(); */

        Intent loginIntent = new Intent(getApplicationContext(),ForgetMdp.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);

    }
    public void navigatetoActua(View view){


        Intent loginIntent = new Intent(getApplicationContext(),ImagePickActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);

    }

    public void derniereEtape(){
        Log.d(TAG, " - LoginActivity.derniereEtape");
        // When Email entered is Valid
        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.alertdialog2, null);

        //Création de l'AlertDialog

        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
        adb.setView(alertDialogView);

        //On donne un titre à l'AlertDialog
        adb.setTitle("Entrez un pseudo et mot de passe :)");

        //On modifie l'icône de l'AlertDialog pour le fun ;)
        adb.setIcon(android.R.drawable.ic_dialog_alert);

        Button button = (Button) alertDialogView.findViewById(R.id.Accept);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText psd = (EditText) alertDialogView.findViewById(R.id.Editpseudo);
                EditText et = (EditText) alertDialogView.findViewById(R.id.EditText1);
                errorMsg2 = (TextView) alertDialogView.findViewById(R.id.error);

                name=psd.getText().toString();
                psswd=et.getText().toString();

                if(name.length()<16){

                    if(psswd.length()>4){

                        RequestParams params2 = new RequestParams();

                        params2.put("psd", name);
                        invokeWSPseudo(params2);


                    }else{

                        errorMsg2.setText("Le mot de passe doit etre supérieur à cinq caractères");
                    }

                }else{
                    errorMsg2.setText("Le pseudo doit contenir moins de seize caractères.");
                }
            }
        });

        //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
        /*
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //Lorsque l'on cliquera sur le bouton "OK", on récupère l'EditText correspondant à notre vue personnalisée (cad à alertDialogView)
                EditText psd = (EditText) alertDialogView.findViewById(R.id.Editpseudo);
                EditText et = (EditText) alertDialogView.findViewById(R.id.EditText1);
                errorMsg2 = (TextView) alertDialogView.findViewById(R.id.error);

                name=psd.getText().toString();
                psswd=et.getText().toString();

                if(name.length()>7){

                    if(psswd.length()>4){

                        RequestParams params2 = new RequestParams();
                        params2.put("psd", name);

                        invokeWSPseudo(params2);


                    }else{

                        errorMsg2.setText("Le mot de passe doit etre supérieur à cinq caractères");



                    }

                }else{
                    errorMsg2.setText("Le pseudo ne pas doit contenir plus de huit caractères et ne doit pas etre null");
                }


              //  oldmdp = et.getText().toString();


                // Invoke RESTful Web Service with Http parameters
            //    invokeWSValiation(params);
       //On affiche dans un Toast le texte contenu dans l'EditText de notre AlertDialog
                //Toast.makeText(Tutoriel18_Android.this, et.getText(), Toast.LENGTH_SHORT).show();
            } });
            */

        //On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un évènement
        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Lorsque l'on cliquera sur annuler on quittera l'application
                LoginManager.getInstance().logOut();
                finish();
            }
        });
        adb.show();

    }


    // Check if Google Playservices is installed in Device or not
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //Toast.makeText(getApplicationContext(), "This device doesn't support Play services, App will not work normally", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        } else {
            //Toast.makeText(getApplicationContext(), "This device supports Play services, App will work normally", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    // When Application is resumed, check for Play services support to make sure
    // app will be running normally
    @Override
    protected void onResume() {
        super.onResume();
//        checkPlayServices();
    }

}