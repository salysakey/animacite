package org.anima.activities;
import org.anima.animacite.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.anima.utils.ConfigurationVille;
import org.anima.utils.PrefManager;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class InfoActivity extends AppCompatActivity {

    private String description;
    private String couleur;
    private String titre;
    private int id = -1;
    private int idp;
    private int position = -1;
    private String pictureUrl = null;
    private int tipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle b = intent.getExtras();
            if (b != null) {
                description = b.getString("description");
                titre = b.getString("titre");
                id = b.getInt("id");
                idp = b.getInt("idp");
                position = b.getInt("position");
                pictureUrl = b.getString("pictureUrl");
                tipe=b.getInt("tipe");
                couleur = b.getString("couleur");
            }
        }
        //String[] resumes = getResources().getStringArray(R.array.resume_items);
        ImageView avatar = (ImageView) findViewById(R.id.img);
        //avatar.setVisibility(View.INVISIBLE);
        //final ImageButton x = (ImageButton) findViewById(R.id.Comment3);
        final ImageButton z = (ImageButton) findViewById(R.id.Comment5);
        //
        // z.setVisibility(View.VISIBLE);

        if(tipe==1) {
            TextView txtTitre = (TextView) findViewById(R.id.titre_consultation);
            txtTitre.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty("Titre : " + description)) {
                txtTitre.setText(description);
            }
        }else{
            TextView txtTitre = (TextView) findViewById(R.id.titre_info);
            txtTitre.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty("Titre : " + description)) {
                txtTitre.setText(description);
            }
        }
        //Couleur de la barre


        //TextView txtDesc = (TextView) findViewById(R.id.descriptif);
        TextView txtResume = (TextView) findViewById(R.id.resume);


        if(pictureUrl.length()!=4){
            avatar.setVisibility(View.VISIBLE);
            Picasso.with(this).load(pictureUrl).into(avatar);
        }
        // avatar.setImageResource(pictureUrl);

        // Verifier si titre est null or vide

        // Verifier si description est null or vide
        if (!TextUtils.isEmpty("Descriptif : " + titre)) {
            txtResume.setText(titre);
        }

        z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Food newOne = new Food(id, titre, pictureUrl, description, tipe);
                //   FollowModel.getInstance().addToList(newOne);


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfoActivity.this);
                // Setting Dialog Title
                alertDialog.setIcon(R.drawable.petite_image);
                // Setting Dialog Message
                alertDialog.setMessage("Voulez vous ne plus suivre ce projet ?");
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Move to Next screen
                                RequestParams params = new RequestParams();
                                params.put("idp", "" + idp);
                                params.put("userId", ""+PrefManager.getUserId(getApplicationContext()));
                                invokeWS(params);


                                Intent chnIntent = new Intent(InfoActivity.this, FollowActivity.class);
                                startActivity(chnIntent);
                            }
                        });
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO",
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
        });

    }
    public void invokeWS(RequestParams params) {

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.10:8080/Anima/rest/follow/dodelete",params, new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS+"/follow/dodelete",params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {


                        Toast.makeText(getApplicationContext(), "Tu ne suis plus ce projet :/", Toast.LENGTH_LONG).show();
                        //On désactive le bouton

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



}

