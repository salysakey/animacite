package org.anima.activities;
import org.anima.utils.ConfigurationVille;
import org.anima.animacite.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.anima.adapters.AdapterQuestion;
import org.anima.adapters.SondageListAdapter;
import org.anima.entities.Proposition;
import org.anima.entities.Question;
import org.anima.utils.PrefManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Sondage extends AppCompatActivity {

    private String TAG = "Sondage";
    private int id = -1;
    private int position = -1;
    private String pictureUrl = "";
    private int tipe;
    private ImageButton z=null;
    ArrayList<Question> listQuestion;
    String[] titles;
    String[] resumes;
    String titre;
    Boolean result;
    private int[] types,ids,statut;
    int a,b,c,f;
    float d;
    //ListView list;
    private AdapterQuestion mAdapter;

    SondageListAdapter adapter;
    ListView listView;
    public List<SondageListAdapter.QuestionResponse> resultats = new ArrayList<>();
    public List<SondageListAdapter.QuestionResponse> resultats2 = new ArrayList<>();
    private List<SondageListAdapter.RatingResponseNUm> ratingResponses = new ArrayList<>();
    private Tracker mTracker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);


        // list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle b = intent.getExtras();
            if (b != null) {

                id = b.getInt("id");
                titre = b.getString("titre");
                //result = b.getBoolean("result");
                result=b.getBoolean("resultat");

            }

        }


        Analytics application = (Analytics) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Consultation-"+id);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        //mAdapter = new AdapterQuestion(this)
        //}
        final ImageButton y = (ImageButton) findViewById(R.id.Comment2);
        listView = (ListView)findViewById(R.id.list);

        TextView txtTitre = (TextView) findViewById(R.id.txt_title_question);
        txtTitre.setText(titre);
        if(result){
            Log.d(TAG," - title background color");
            txtTitre.setBackgroundColor(getResources().getColor(R.color.orange));
        }else{
            Log.d(TAG," - title background color green");
            txtTitre.setBackgroundColor(getResources().getColor(R.color.vert));
        }

        z = (ImageButton) findViewById(R.id.Comment);
        if(result){
            z.setVisibility(View.GONE);
            RequestParams params = new RequestParams();
            params.put("id", "" + id);
            invokeWSResultat(params);

        }else{
            RequestParams params = new RequestParams();
            params.put("id", "" + id);
            invokeWS(params);
        }

        z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Food newOne = new Food(id, titre, pictureUrl, description, tipe);
                //   FollowModel.getInstance().addToList(newOne);


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Sondage.this);
                // Setting Dialog Title
                alertDialog.setIcon(R.drawable.petite_image);
                alertDialog.setTitle("Vos élus vous écoutent...");
                // Setting Dialog Message
                alertDialog.setMessage("C'est votre dernier mot" +"?");
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Oui",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Move to Next screen
                                RequestParams params = new RequestParams();
                                params.put("idp", "" + id);
                                params.put("userId",""+ PrefManager.getUserId(getApplicationContext()));
                                invokeWS3(params);
                            }
                        });
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NON",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                // Showing Alert Message
                AlertDialog alert = alertDialog.create();
                alert.show();
            }
        });

        y.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sondage.this, ImagePickActivity.class);
                startActivity(intent);
            }
        });




    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Fails")
                .setAction("WentBackWithoutAnswerQuestions")
                .setLabel("FailVote")
                .build());

        return true;
    }

    public void invokeWS(RequestParams params) {
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ConfigurationVille.Debut_WS +"/question/doformulaire",params, new AsyncHttpResponseHandler() {
            //client.get("http://192.168.0.10:8080/Anima/rest/question/doformulaire",params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj !=null) {
                        Log.d(TAG," - invokeWS if not null object");
                        JSONArray jarray = obj.getJSONArray("formulaires");
                        titles = new String[44];
                        resumes = new String[44];
                        ids = new int[44];
                        types = new int[45];
                        //listQuestion = new ArrayList<Question>();
                        int p=0;
                        int h=0;

                        List<Question> questions = new ArrayList<>();
                        for (int i = 0; i < jarray.length(); i++) {
                            Log.d(TAG," - In the for question");
                            JSONObject object = jarray.getJSONObject(i);
                            Question question = new Question();

                            question.setName(object.getString("Titre"));
                            question.setType(object.getInt("nbr_solution"));
                            question.setId(object.getInt("id_question"));
                            List<Proposition> propositions = new ArrayList<>();
                            JSONArray jarray2 = object.getJSONArray("propositions");

                            for (int y = 0; y < jarray2.length(); y++) {
                                Log.d(TAG," - In the for proposition");
                                Proposition proposition = new Proposition();
                                JSONObject object2 = jarray2.getJSONObject(y);
                                proposition.setId(object2.getInt("id_proposition"));
                                if(question.getType()!=0) {
                                    proposition.setRank(object2.getString("text_proposition"));
                                    Log.d(TAG," - Recuperation proposition");
                                }
                                propositions.add(proposition);
                            }

                            question.setPropositions(propositions);
                            questions.add(question);


                        }

                        //Attente de % secondes pour les photos
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        affect(questions);
                        Toast.makeText(getApplicationContext(), "À vous de jouer!", Toast.LENGTH_LONG).show();
                        //On désactive le bouton
                        //z.setEnabled(false);
                    }
                    // Else display error message
                    else {

                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    System.out.println(e.toString());
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
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
                }
            }

        });

    }

    public void invokeWSResultat(RequestParams params) {

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ConfigurationVille.Debut_WS +"/question/doresultat",params, new AsyncHttpResponseHandler() {
            //client.get("http://192.168.0.10:8080/Anima/rest/question/doformulaire",params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj !=null) {

                        JSONArray jarray = obj.getJSONArray("formulaires");
                        titles = new String[4];
                        resumes = new String[14];
                        ids = new int[4];
                        types = new int[15];
                        //listQuestion = new ArrayList<Question>();
                        int p=0;
                        int h=0;

                        List<Question> questions = new ArrayList<>();
                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject object = jarray.getJSONObject(i);
                            Question question = new Question();

                            question.setName(object.getString("Titre"));
                            question.setType(object.getInt("nbr_solution"));
                            question.setId(object.getInt("id_question"));

                            //}
                            List<Proposition> propositions = new ArrayList<>();
                            JSONArray jarray2 = object.getJSONArray("propositions");


                            for (int y = 0; y < jarray2.length(); y++) {

                                Proposition proposition = new Proposition();
                                JSONObject object2 = jarray2.getJSONObject(y);


                                proposition.setId(object2.getInt("id_proposition"));
                                if(question.getType()!=0) {
                                    //caster pour arrondir
                                    String newLine = System.getProperty("line.separator");
                                    p = (int) object2.getDouble("moyenne_votant");
                                    proposition.setRank(object2.getString("text_proposition")+" "+newLine+" "+p+" %");
                                    proposition.setResultat(object2.getDouble("moyenne_votant"));
                                }else{
                                    proposition.setResultat(object2.getDouble("moyenne_votant_etoile"));
                                }
                                //types[p] = object2.getInt("id_proposition");

                                //p++;
                                propositions.add(proposition);

                            }
                            question.setPropositions(propositions);
                            questions.add(question);


                        }
                        affectResultat(questions);
                        //Toast.makeText(getApplicationContext(), "À vous de jouer!", Toast.LENGTH_LONG).show();
                        //On désactive le bouton
                        //z.setEnabled(false);
                    }
                    // Else display error message
                    else {

                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    System.out.println(e.toString());
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
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
                }
            }
        });
    }

    public void invokeWS2(RequestParams params) {

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ConfigurationVille.Debut_WS +"/reponse/doreponse",params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {


                        //Toast.makeText(getApplicationContext(), "Merci ! Vous pour votre participation!", Toast.LENGTH_LONG).show();
                        //On désactive le bouton
                        //z.setEnabled(false);

                    }
                    // Else display error message
                    else {

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

    public void invokeWS3(RequestParams params) {


        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client2 = new AsyncHttpClient();
        client2.get(ConfigurationVille.Debut_WS +"/reponse/doveri",params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                int r;
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    statut = new int[3];

                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {

                        statut[0]=1;


                        //On désactive le bouton
                        //z.setEnabled(false);
                        decision();
                    }
                    // Else display error message
                    else {


                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();

                        Intent chnIntent = new Intent(Sondage.this, ImagePickActivity.class);
                        startActivity(chnIntent);



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

    public void affect(List<Question> questions){
        Log.d(TAG, " - In the Affect");
        adapter = new SondageListAdapter(this, questions,false);
        listView.setAdapter(adapter);
    }

    public void affectResultat(List<Question> questions){
        adapter = new SondageListAdapter(this, questions,true);
        listView.setAdapter(adapter);
    }

    public void select(){
        resultats = adapter.getReponses();
        resultats2 = adapter.getReponses2();
        ratingResponses = adapter.getRatingResponsesNum();
    }

    public void decision(){
        select();
        if(!resultats.isEmpty() || !resultats2.isEmpty() || !ratingResponses.isEmpty()){
            if(!resultats.isEmpty()){
                RequestParams params2 = new RequestParams();
                for( int i=0;i<resultats.size();i++){
                    params2.put("a", "" +resultats.get(i).getResponseId());
                    params2.put("userId", ""+PrefManager.getUserId(getApplicationContext()));
                    invokeWS2(params2);
                }
            }

            if(!resultats2.isEmpty()){
                RequestParams params2 = new RequestParams();
                for( int i=0;i<resultats2.size();i++){
                    params2.put("a", "" +resultats2.get(i).getResponseId());
                    params2.put("userId", ""+PrefManager.getUserId(getApplicationContext()));
                    invokeWS2(params2);
                }
            }

            if (!ratingResponses.isEmpty()){
                RequestParams params3 = new RequestParams();
                for( int i=0;i<ratingResponses.size();i++){
                    params3.put("a", "" + ratingResponses.get(i).getId_propo());
                    params3.put("d", "" + ratingResponses.get(i).getRatingValue());
                    params3.put("userId", ""+ PrefManager.getUserId(getApplicationContext()));
                    invokeWS2(params3);
                }
            }
            Toast.makeText(getApplicationContext(), "Les jeux sont fait ! Votre ville a bien recu votre avis.", Toast.LENGTH_LONG).show();

            Intent chnIntent = new Intent(Sondage.this, ImagePickActivity.class);
            startActivity(chnIntent);

        }else{

            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Fails")
                    .setAction("DidNotAnswerAllQuestions")
                    .setLabel("FailVote")
                    .build());

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Sondage.this);
            // Setting Dialog Title
            alertDialog.setIcon(R.drawable.petite_image);
            // Setting Dialog Message
            alertDialog.setMessage("Vous devez repondre à au moins une question");
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

    }

    @Override
    public void onBackPressed() {
        //Intent intent = new Intent(Sondage.this, EmptyActivity.class);
        finish();
    }// end function



}