package org.anima.activities;
import org.anima.utils.ConfigurationVille;
import org.anima.animacite.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.squareup.otto.Bus;
//import com.squareup.otto.Subscribe;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.anima.utils.GMailSender;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by BeugFallou on 11/11/2014.
 */
public class ForgetMdp extends Activity implements Chronometer.OnChronometerTickListener,
        AdapterView.OnItemClickListener {

    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    private ListView listView;
    private ListAdapter adapter;
    private EditText emailET;
    private String pwd;
    private  String mail;
    private Tracker mTracker;
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
        setContentView(R.layout.forgetmdp);

        Analytics application = (Analytics) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("MdpOublié");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        // db = new DBAdapter(this);
        //users = getIntent().getParcelableExtra(getString(R.string.bundle_key_users));
        //bus = new Bus();
        //bus.register(this);
        emailET = (EditText) findViewById(R.id.editTextTo);
        errorMsg = (TextView) findViewById(R.id.error);
        Button like = (Button) findViewById(R.id.buttonSend);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mail = emailET.getText().toString();

                RequestParams params = new RequestParams();
                params.put("mail", mail);
                invokeWS(params);




            }

        });

        //initializeEvaluation();

        //initializeControls();



    }

    private void sendMail(EditText emailET) {
        lolo = new GMailSender("noreply@animacite.com","iNeed$$$");



        try {
            String contenu=
                    "Bonjour, \n" +
                    "\n" +
                        "Veuillez utiliser le mot de passe suivant pour accéder à votre application. Vous pouvez modifier votre mot de passe à tout moment. Pour cela, connectez-vous et allez dans les paramètres de votre profil.\n" +
                    "\n" +
                    "Votre mot de passe est: "+pwd+".\n" +
                    "\n" +
                    "Bien cordialement,\n" +
                    "L’équipe AnimaCité\n\n" +
                    "(Ce message vous a été envoyé automatiquement, merci de ne pas y répondre)";

            lolo.sendMail("Mot de passe oublié " + ConfigurationVille.APP_NAME, contenu, "noreply@animacite.com", mail);

        } catch (Exception e) {

            e.printStackTrace();
        }





    }


    @Override
    public void onChronometerTick(Chronometer chronometer) {
        chronometerBehaviour();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent5 = new Intent(ForgetMdp.this, LoginActivity.class);
            startActivity(intent5);
        }
        return true;


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

    public void invokeWS(RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.14:8080/Anima/rest/login/dologin",params ,new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS+"/mdp/domdp", params, new AsyncHttpResponseHandler() {
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

                        pwd=obj.getString("password");



                        new SenderMail().execute();



                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ForgetMdp.this);
                        // Setting Dialog Title
                        alertDialog.setTitle("Merci!");
                        alertDialog.setMessage("Le mot de passe est disponible dans vos mails");
                        alertDialog.setIcon(R.drawable.petite_image);
                        // Setting Dialog Message
                        alertDialog.setNegativeButton("Merci",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Cancel Dialog
                                        Intent homeIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                        //  homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(homeIntent);
                                        dialog.cancel();
                                    }
                                });
                        // Showing Alert Message
                        AlertDialog alert = alertDialog.create();
                        alert.show();





                        //   v.setName(obj.getString("Device"));
                        // Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();

                        // Navigate to Home screen

                    }
                    // Else display error message
                    else {
                        errorMsg.setText("Oups.... Erreur de frappe ? Rentrez une adresse mail valide");
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

    /*

    public void initializeEvaluation(){
        if(db == null){
            throw new NullPointerException(getString(R.string.db_initialization_fail));
        }
        ArrayList<Questions> listQuestions = db.selectAllQuestions();
        ArrayList<Answers> listAnswers = db.selectAllAnswers();
        evaluation.setQuestion_number(listQuestions.size());
        evaluation.setQuestions(listQuestions);
        evaluation.setAnswers(listAnswers);
    }

    */

    public void initializeControls(){
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setOnChronometerTickListener(this);

        chronometer.setBase(SystemClock.elapsedRealtime() - 0);
        chronometer.start();

        listView = (ListView)findViewById(R.id.list);


        //adapter = new ListAdapter(this, evaluation, db);
        //listView.setAdapter(adapter);
        //listView.setOnItemClickListener(this);
    }

    /*

    public void collectEvaluationResult(){
        if(listView == null){
            throw new IllegalStateException(getString(R.string.list_view_null));
        }
        new HelpDialog(this).showProgressDialog();
        HashMap<Integer, Boolean> mapResult = adapter.getMapEvaluationResult();
        if(mapResult == null){
            return;
        }

        for(int hmSize = 0; hmSize < 30; hmSize++){
            if(mapResult.get(hmSize) != null && mapResult.get(hmSize)){
                evaluationResult ++;
            }
        }
        new SenderMail().execute();

    }

    */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
    /*

    @Subscribe
    public void mailSendingEvent(MailSendingEvent event){
        if(event.isSent()){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bus.post(new EvaluationEndedEvent(true));
                }
            }, 3000);

        }else{
            SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM);
            String currentDateTime = sdf.format(new Date());
            db.insertEvaluationResult(new EvaluationResult(users, evaluationResult, currentDateTime));
            bus.post(new EvaluationEndedEvent(false));
        }
    }

    @Subscribe
    public void evaluationEndedEvent(EvaluationEndedEvent event){
        if(EvaluationPreferences.deleteManagerAccessIfRequired(this)){
            IntentHelper.startSubscriptionActivity(this);
        }else{
            IntentHelper.startAuthenticationActivity(this);
        }

    }

    class SenderMail extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return MailSender.send(getApplicationContext(), users, evaluationResult);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            bus.post(new MailSendingEvent(aBoolean));
        }
    }
    */
}
