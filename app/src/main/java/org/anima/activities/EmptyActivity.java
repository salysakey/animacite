package org.anima.activities;
import org.anima.utils.ConfigurationVille;
import org.anima.animacite.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.anima.utils.PrefManager;
import org.json.JSONException;
import org.json.JSONObject;



/**
 *
 */
public class EmptyActivity extends AppCompatActivity {
    private static final String TAG = "EmptyActivity";
    private String description;
    private String titre;
    private String color;
    private ShareDialog shareDialog;
    private int id = -1;
    private int position = -1;
    private String pictureUrl = "";
    private int tipe,p;
    private  long date;
    private ImageButton z=null;
    private ImageButton like,calendrier,jeparticipe = null;
    private ImageButton unlike=null;
    private ImageButton alreadyfollow =null;
    private Tracker mTracker;
    private boolean affichercalendar;

    private int bottom_height;

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
                color = b.getString("color");
                id = b.getInt("ident");
                position = b.getInt("position");
                pictureUrl = b.getString("pictureUrl");
                tipe=b.getInt("tipe");
                date=b.getLong("date");
                affichercalendar=b.getBoolean("affichercalendar");

            }
        }
        //((Analytics) getApplication()).getTracker(Analytics.TrackerName.APP_TRACKER);
        Analytics application = (Analytics) getApplication();
        mTracker = application.getDefaultTracker();

        sendScreenImageName();
        /*

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Share")
                .build());

                */
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int density = metrics.densityDpi;





        //String[] resumes = getResources().getStringArray(R.array.resume_items);



        ImageView avatar = (ImageView) findViewById(R.id.img);
        LinearLayout info_bar = (LinearLayout) findViewById(R.id.info_bar);
        LinearLayout consultation_bar = (LinearLayout) findViewById(R.id.consultation_bar);
        //final ImageButton y = (ImageButton) findViewById(R.id.Comment3);
        ImageButton button = (ImageButton) findViewById(R.id.fcbkshare);
        ImageButton x = (ImageButton) findViewById(R.id.Comment);
        ImageButton x4 = (ImageButton) findViewById(R.id.Comment4);
        ImageButton x5 = (ImageButton) findViewById(R.id.Comment5);
        ImageButton x2 = (ImageButton) findViewById(R.id.Comment2);
        ImageButton calendrierColor = (ImageButton) findViewById(R.id.calendrier);
        ImageButton jeparticipeColor =(ImageButton) findViewById(R.id.jeparticipe);
        TextView titre_info =(TextView) findViewById(R.id.titre_info);
        TextView titre_consultation =(TextView) findViewById(R.id.titre_consultation);
        ImageButton show_result =(ImageButton) findViewById(R.id.show_result);
        ImageButton delete_follow_consultation =(ImageButton) findViewById(R.id.delete_follow_consultation);
        ImageButton follow_consultation =(ImageButton) findViewById(R.id.follow_consultation);



        ImageButton show_result_button = (ImageButton) findViewById(R.id.show_result);

        like = (ImageButton) findViewById(R.id.like);
        unlike=(ImageButton) findViewById(R.id.unlike);

        if(color.substring(0, 1).equals("#"))
            like.setBackgroundColor(Color.parseColor(color));
            unlike.setBackgroundColor(Color.parseColor(color));
            x.setBackgroundColor(Color.parseColor(color));
            x2.setBackgroundColor(Color.parseColor(color));
            x4.setBackgroundColor(Color.parseColor(color));
            x5.setBackgroundColor(Color.parseColor(color));
            show_result_button.setBackgroundColor(Color.parseColor(color));
            info_bar.setBackgroundColor(Color.parseColor(color));
            consultation_bar.setBackgroundColor(Color.parseColor(color));
            calendrierColor.setBackgroundColor(Color.parseColor(color));
            jeparticipeColor.setBackgroundColor(Color.parseColor(color));
            titre_info.setBackgroundColor(Color.parseColor(color));
            titre_consultation.setBackgroundColor(Color.parseColor(color));
            show_result.setBackgroundColor(Color.parseColor(color));
            delete_follow_consultation.setBackgroundColor(Color.parseColor(color));
            follow_consultation.setBackgroundColor(Color.parseColor(color));

        shareDialog = new ShareDialog(this);  // intialize facebook shareDialog.

        /*
        Double d = new Double((3.2)*density);
        int valeur = d.intValue();
        ScrollView myScrollView = (ScrollView) findViewById(R.id.scrollView);
        myScrollView.getLayoutParams().height = (valeur);
*/


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShareDialog.canShow(ShareLinkContent.class)) {


                    if (pictureUrl.equals("null")) {

                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                .setContentTitle(titre)
                                .setContentDescription(
                                        description)
                                .setContentUrl(Uri.parse(ConfigurationVille.FACEBOOK_SHARE_URL))
                                .build();

                        shareDialog.show(linkContent);  // Show facebook ShareDialog
                    } else {

                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                .setContentTitle(titre)
                                .setImageUrl(Uri.parse(pictureUrl))
                                .setContentDescription(
                                        description)
                                .setContentUrl(Uri.parse(ConfigurationVille.FACEBOOK_SHARE_URL))
                                .build();

                        shareDialog.show(linkContent);  // Show facebook ShareDialog
                    }
                }
            }
        });


        if(tipe==1) {
            z = (ImageButton) findViewById(R.id.follow_consultation);
            alreadyfollow =(ImageButton) findViewById(R.id.delete_follow_consultation);
            if (PrefManager.getRatingStatus(getApplicationContext())!=0) {
                RequestParams params = new RequestParams();
                params.put("idp", "" + id);
                params.put("userId", ""+PrefManager.getUserId(getApplicationContext()));
                invokeWSVeri2(params);
            }else{
                z.setVisibility(View.GONE);
            }
            if(date<=0){
                show_result_button.setVisibility(View.VISIBLE);
            }
            else {
                x.setVisibility(View.VISIBLE);
            }
            TextView txtTitre = (TextView) findViewById(R.id.titre_consultation);
            txtTitre.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty("Titre : " + description)) {
                txtTitre.setText(description);
            }
            Log.d(TAG, " - tipe1");
        }else if(tipe==2){
            z = (ImageButton) findViewById(R.id.Comment2);
            alreadyfollow =(ImageButton) findViewById(R.id.Comment5);
            if (PrefManager.getRatingStatus(getApplicationContext())!=0) {
                RequestParams params = new RequestParams();

                params.put("idp", "" + id);
                params.put("userId", ""+PrefManager.getUserId(getApplicationContext()));
                invokeWSVeri(params);
                invokeWSVeri2(params);

            }else{
                unlike.setVisibility(View.VISIBLE);
            }


            TextView txtTitre = (TextView) findViewById(R.id.titre_info);
            txtTitre.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty("Titre : " + description)) {
                txtTitre.setText(description);
            }
            Log.d(TAG, " - tipe2");
        }else{
            z = (ImageButton) findViewById(R.id.calendrier);
            alreadyfollow =(ImageButton) findViewById(R.id.jeparticipe);

            if(affichercalendar==false) {
                z.setVisibility(View.GONE);
                alreadyfollow.setVisibility(View.GONE);
            }else {


                if (PrefManager.getRatingStatus(getApplicationContext()) != 0) {
                    RequestParams params = new RequestParams();

                    params.put("idp", "" + id);
                    params.put("userId", "" + PrefManager.getUserId(getApplicationContext()));
                    //invokeWSVeri(params);
                    invokeWSVeriEvenement(params);

                } else {
                    // unlike.setVisibility(View.VISIBLE);
                }
            }


            TextView txtTitre = (TextView) findViewById(R.id.titre_info);
            txtTitre.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty("Titre : " + description)) {
                txtTitre.setText(description);
            }
            Log.d(TAG, " - tipe3");
        }
        //TextView txtDesc = (TextView) findViewById(R.id.descriptif);
        TextView txtResume = (TextView) findViewById(R.id.resume);

        // avatar.setImageResource(pictureUrl);
        if(pictureUrl.length()!=4){
            Picasso.with(this).load(pictureUrl).into(avatar);
            avatar.setVisibility(View.VISIBLE);
        }

        // Verifier si description est null or vide
        if (!TextUtils.isEmpty("Descriptif : " + titre)) {
            //Petite modification à effectuer pour rendre les liens clickables
            txtResume.setMovementMethod(LinkMovementMethod.getInstance());
            txtResume.setText(Html.fromHtml(titre));
        }



        z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Food newOne = new Food(id, titre, pictureUrl, description, tipe);
                //   FollowModel.getInstance().addToList(newOne);

                if (PrefManager.getRatingStatus(getApplicationContext())==0) {

                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Navigation")
                            .setAction("goLoginFromTuile")
                            .setLabel("goLoginFromTuile")
                            .build());

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(EmptyActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Vous n'etes pas inscrit");
                    alertDialog.setIcon(R.drawable.petite_image);
                    // Setting Dialog Message
                    alertDialog.setMessage("Inscrivez vous pour participer");
                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("Oui",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Move to Next screen
                                    Intent chnIntent = new Intent(EmptyActivity.this, LoginActivity.class);
                                    startActivity(chnIntent);
                                }
                            });
                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("Non",
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
                    if(tipe==1 || tipe==2) {

                        RequestParams params = new RequestParams();
                        params.put("id", "" + id);
                        params.put("userId", "" + PrefManager.getUserId(getApplicationContext()));
                        invokeWS(params);
                    }else{

                        RequestParams params = new RequestParams();
                        params.put("id", "" + id);
                        params.put("userId", "" + PrefManager.getUserId(getApplicationContext()));
                        invokeWSEvenement(params);


                    }
                }
            }
        });

        alreadyfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Food newOne = new Food(id, titre, pictureUrl, description, tipe);
                //   FollowModel.getInstance().addToList(newOne);

                if (PrefManager.getRatingStatus(getApplicationContext()) == 0) {

                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Navigation")
                            .setAction("goLoginFromTuile")
                            .setLabel("goLoginFromTuile")
                            .build());

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(EmptyActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Vous n'etes pas connecté");
                    alertDialog.setIcon(R.drawable.petite_image);
                    // Setting Dialog Message
                    alertDialog.setMessage("Voulez-vous vous connecter ?");
                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("OUI",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Move to Next screen
                                    Intent chnIntent = new Intent(EmptyActivity.this, LoginActivity.class);
                                    startActivity(chnIntent);
                                }
                            });
                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NON",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Cancel Dialog
                                    dialog.cancel();
                                }
                            });
                    // Showing Alert Message
                    AlertDialog alert = alertDialog.create();
                    alert.show();

                } else {
                    if(tipe==1 || tipe==2){

                        RequestParams params2 = new RequestParams();
                        params2.put("idp", "" + id);
                        params2.put("userId", ""+PrefManager.getUserId(getApplicationContext()));
                        invokeWSUnfollow(params2);

                    }else{

                        RequestParams params2 = new RequestParams();
                        params2.put("idp", "" + id);
                        params2.put("userId", ""+PrefManager.getUserId(getApplicationContext()));
                        invokeWSUnParticipate(params2);

                    }


                }
            }
        });

        show_result_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PrefManager.getRatingStatus(getApplicationContext())!=0) {
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Navigation")
                            .setAction("AccessVoteByFooterButton")
                            .setLabel("AccessVote")
                            .build());

                    RequestParams params = new RequestParams();

                    params.put("idp", "" + id);
                    params.put("userId", ""+PrefManager.getUserId(getApplicationContext()));
                    invokeWS3(params);
                }
            }
        });

        x.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //   Food newOne = new Food(id, titre, pictureUrl, description, tipe);
                //   FollowModel.getInstance().addToList(newOne);

                if (PrefManager.getRatingStatus(getApplicationContext())==0) {

                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Navigation")
                            .setAction("goLoginFromTuile")
                            .setLabel("goLoginFromTuile")
                            .build());

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(EmptyActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Vous n'etes pas connecté");
                    alertDialog.setIcon(R.drawable.petite_image);
                    // Setting Dialog Message
                    alertDialog.setMessage("Voulez-vous vous connecter ?");
                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("OUI",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Move to Next screen
                                    Intent chnIntent = new Intent(EmptyActivity.this, LoginActivity.class);
                                    startActivity(chnIntent);
                                }
                            });
                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NON",
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

                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Navigation")
                            .setAction("AccessVoteByFooterButton")
                            .setLabel("AccessVote")
                            .build());

                    RequestParams params = new RequestParams();

                    params.put("idp", "" + id);
                    params.put("userId", ""+PrefManager.getUserId(getApplicationContext()));
                    invokeWS3(params);
                }
            }
        });




        like.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //   Food newOne = new Food(id, titre, pictureUrl, description, tipe);
                //   FollowModel.getInstance().addToList(newOne);
                RequestParams params = new RequestParams();
                params.put("idp", ""+id);
                params.put("userId", ""+PrefManager.getUserId(getApplicationContext()));
                invokeWSLike(params);


            }
        });

        unlike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //   Food newOne = new Food(id, titre, pictureUrl, description, tipe);
                //   FollowModel.getInstance().addToList(newOne);
                if (PrefManager.getRatingStatus(getApplicationContext())==0) {

                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Navigation")
                            .setAction("goLoginFromTuile")
                            .setLabel("goLoginFromTuile")
                            .build());

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(EmptyActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Vous n'etes pas connecté");
                    alertDialog.setIcon(R.drawable.petite_image);
                    // Setting Dialog Message
                    alertDialog.setMessage("Voulez-vous vous connecter ?");
                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("OUI",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Move to Next screen
                                    Intent chnIntent = new Intent(EmptyActivity.this, LoginActivity.class);
                                    startActivity(chnIntent);
                                }
                            });
                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NON",
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

                    RequestParams params = new RequestParams();
                    params.put("idp", ""+id);
                    params.put("userId", ""+PrefManager.getUserId(getApplicationContext()));
                    invokeWSLike(params);


                }





            }
        });


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //Redimenssionnement de la hauteur
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus == true) {
            LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.golayout);
            bottom_height = myLinearLayout.getHeight();
            Log.d(TAG, "BOTTOM HEIGHT "+bottom_height);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int w = dm.heightPixels; // etc...
            Log.d(TAG, "Hauteur "+w);
            Log.d(TAG, "BOTTOM millieu "+bottom_height);
            int padding_height =(int) convertDpToPixel(20, this.getApplicationContext());
            int scroll_height = w-bottom_height-padding_height;
            Log.d(TAG, "Scroll view height"+ scroll_height);
            ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
            Log.d(TAG, "Real Scroll view height"+ scrollView.getLayoutParams().height);
            //scrollView.getLayoutParams().height = scroll_height;
            Log.d(TAG, "Real Scroll view height"+ scrollView.getLayoutParams().height);
        }
    }
    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(int dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }


    public void invokeWS(RequestParams params) {

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.10:8080/Anima/rest/follow/dofollow",params, new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS+"/follow/dofollow",params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {


                        //Toast.makeText(getApplicationContext(), "Merci ! Vous suivez ce projet!", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EmptyActivity.this);
                        // Setting Dialog Title
                        alertDialog.setTitle("Vous pouvez me consulter dans vos favoris");
                        alertDialog.setIcon(R.drawable.petite_image);
                        // Setting Dialog Message
                        alertDialog.setNegativeButton("Merci",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Cancel Dialog
                                        dialog.cancel();
                                    }
                                });
                        // Showing Alert Message
                        AlertDialog alert = alertDialog.create();
                        alert.show();

                        //On désactive le bouton
                        z.setVisibility(View.GONE);
                        alreadyfollow.setVisibility(View.VISIBLE);
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
                    Toast.makeText(getApplicationContext(), "Une erreur s'est produite, vérifiez votre connection internet.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void invokeWSsignalement(RequestParams params) {

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.10:8080/Anima/rest/follow/dofollow",params, new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS+"//classement/targetpseudo",params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {

                        if(obj.getInt("scocre")==3){

                            //Toast.makeText(getApplicationContext(), "Merci ! Vous suivez ce projet!", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(EmptyActivity.this);
                            // Setting Dialog Title
                            alertDialog.setTitle("Vous pouvez me consulter dans vos favoris");
                            alertDialog.setIcon(R.drawable.petite_image);
                            // Setting Dialog Message
                            alertDialog.setNegativeButton("Merci",
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

    public void invokeWSEvenement(RequestParams params) {

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.10:8080/Anima/rest/follow/dofollow",params, new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS+"/evenements/dofollow",params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {


                        //Toast.makeText(getApplicationContext(), "Merci ! Vous suivez ce projet!", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EmptyActivity.this);
                        // Setting Dialog Title
                        alertDialog.setTitle("Merci pour votre engagement");
                        alertDialog.setIcon(R.drawable.petite_image);
                        // Setting Dialog Message
                        alertDialog.setNegativeButton("Continuer",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Cancel Dialog
                                        dialog.cancel();
                                    }
                                });
                        // Showing Alert Message
                        AlertDialog alert = alertDialog.create();
                        alert.show();

                        //On désactive le bouton
                        z.setVisibility(View.GONE);
                        alreadyfollow.setVisibility(View.VISIBLE);
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

    public void invokeWSUnfollow(RequestParams params) {

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

                        if(tipe==3){
                            //Toast.makeText(getApplicationContext(), "Vous ne suivez plus ce projet!", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(EmptyActivity.this);
                            // Setting Dialog Title
                            alertDialog.setTitle("C'est dommage que vous changiez d'avis.");
                            alertDialog.setIcon(R.drawable.petite_image);
                            // Setting Dialog Message
                            alertDialog.setNegativeButton("Continuer",
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

                            //Toast.makeText(getApplicationContext(), "Vous ne suivez plus ce projet!", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(EmptyActivity.this);
                            // Setting Dialog Title
                            alertDialog.setTitle("Ce post ne fait plus partie de vos favoris ");
                            alertDialog.setIcon(R.drawable.petite_image);
                            // Setting Dialog Message
                            alertDialog.setNegativeButton("Continuer",
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



                        //On désactive le bouton
                        z.setVisibility(View.VISIBLE);
                        alreadyfollow.setVisibility(View.GONE);

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

    public void invokeWSUnParticipate(RequestParams params) {

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.10:8080/Anima/rest/follow/dodelete",params, new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS+"/evenements/dodelete",params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {


                        //Toast.makeText(getApplicationContext(), "Vous ne suivez plus ce projet!", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EmptyActivity.this);
                        // Setting Dialog Title
                        alertDialog.setTitle("C'est dommage que vous changiez d'avis.");
                        alertDialog.setIcon(R.drawable.petite_image);
                        // Setting Dialog Message
                        alertDialog.setNegativeButton("Continuer",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Cancel Dialog
                                        dialog.cancel();
                                    }
                                });
                        // Showing Alert Message
                        AlertDialog alert = alertDialog.create();
                        alert.show();




                        //On désactive le bouton
                        z.setVisibility(View.VISIBLE);
                        alreadyfollow.setVisibility(View.GONE);

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
        //client2.get("http://192.168.0.10:8080/Anima/rest/reponse/doveri",params, new AsyncHttpResponseHandler() {
        client2.get(ConfigurationVille.Debut_WS+"/reponse/doveri",params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog


                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);


                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {


                        // Toast.makeText(getApplicationContext(), "Merci ! Vous pour votre participation!", Toast.LENGTH_LONG).show();

                        //On désactive le bouton
                        //z.setEnabled(false);
                        decision();
                    }
                    // Else display error message
                    else {

                        decision2();



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

    public void invokeWSLike(RequestParams params) {

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.10:8080/Anima/rest/follow/dofollow",params, new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS+"/like/dolike",params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {


                        //Toast.makeText(getApplicationContext(), "Merci ! Vous suivez ce projet!", Toast.LENGTH_LONG).show();
                        //On désactive le bouton
                        like.setVisibility(View.VISIBLE);
                        unlike.setVisibility(View.GONE);
                    }
                    // Else display error message
                    else {
                        unlike.setVisibility(View.VISIBLE);
                        like.setVisibility(View.GONE);

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

    public void invokeWSVeri(RequestParams params) {

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.10:8080/Anima/rest/follow/dofollow",params, new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS+"/like/doveri",params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {

                        //Toast.makeText(getApplicationContext(), "Merci ! Vous suivez ce projet!", Toast.LENGTH_LONG).show();
                        //On désactive le bouton
                        unlike.setVisibility(View.VISIBLE);
                    }
                    // Else display error message
                    else {
                        like.setVisibility(View.VISIBLE);
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

    public void invokeWSVeri2(RequestParams params) {

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.10:8080/Anima/rest/follow/dofollow",params, new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS+"/follow/doveri",params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {
                        z.setVisibility(View.VISIBLE);
                        alreadyfollow.setVisibility(View.GONE);

                        //Toast.makeText(getApplicationContext(), "Merci ! Vous suivez ce projet!", Toast.LENGTH_LONG).show();
                        //On désactive le bouton
                    }
                    // Else display error message
                    else {
                        z.setVisibility(View.GONE);
                        alreadyfollow.setVisibility(View.VISIBLE);
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

    public void invokeWSVeriEvenement(RequestParams params) {

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.10:8080/Anima/rest/follow/dofollow",params, new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS+"/evenements/doveri",params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {
                        z.setVisibility(View.VISIBLE);
                        alreadyfollow.setVisibility(View.GONE);

                        //Toast.makeText(getApplicationContext(), "Merci ! Vous suivez ce projet!", Toast.LENGTH_LONG).show();
                        //On désactive le bouton
                    }
                    // Else display error message
                    else {
                        z.setVisibility(View.GONE);
                        alreadyfollow.setVisibility(View.VISIBLE);
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

    private void sendScreenImageName() {
        // String name = getCurrentImageTitle();

        // [START screen_view_hit]
        //  Log.i(TAG, "Setting screen name: " + name);
        mTracker.setScreenName("Tuile-" + id);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        // [END screen_view_hit]
    }

    public void decision(){

        Intent intent = new Intent( EmptyActivity.this , Sondage.class);

        Bundle b = new Bundle();

        b.putInt("id", id);
        b.putString("titre", description);
        if(date<=0){
            b.putBoolean("resultat",true);

        }else{
            b.putBoolean("resultat",false);

        }

        intent.putExtras(b);

        startActivity(intent);


    }

    public void decision2(){


        if(date<=0){
            Intent intent = new Intent( EmptyActivity.this , Sondage.class);

            Bundle b = new Bundle();

            b.putInt("id", id);
            b.putString("titre", description);
            b.putBoolean("resultat", true);
            intent.putExtras(b);

            startActivity(intent);

        } else {

            Toast.makeText(getApplicationContext(), "Vous avez déjà répondu à ce sondage "+PrefManager.getUserName(getApplicationContext())+" :/", Toast.LENGTH_LONG).show();


            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Fails")
                    .setAction("HasAlreadyVote")
                    .setLabel("AlreadyVoted")
                    .build());
        }

    }
}
