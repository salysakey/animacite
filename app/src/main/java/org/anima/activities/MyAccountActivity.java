package org.anima.activities;
import org.anima.animacite.R;

import org.anima.adapters.AdapterQuestion;
import org.anima.helper.Navigation;
import org.anima.utils.ConfigurationVille;
import org.anima.utils.PrefManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.Calendar;

/**
 * Displays the name and avatar of the user Allows to log out
 * 
 * @author MOMO
 * 
 */
public class MyAccountActivity  extends AppCompatActivity {
    public static final String PREFS_NAME = "LoginPrefs";
    private String name,profession;
    ProgressDialog prgDialog;
    private AdapterQuestion mAdapter;
    //private String address;
    private String mail;
    private String phone;
    private MenuItem update;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private Menu menudeux;
    private ListView listView;
    private TextView text_stat_perso;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);
        listView = (ListView)findViewById(R.id.list);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int density = metrics.densityDpi;
        Double d = new Double((3.3)*density);
        int valeur = d.intValue();

        initToolbar();
        //int age= (int) (System.currentTimeMillis()-PrefManager.getUserNaiss(getApplicationContext()))/1000;
        Calendar cl = Calendar.getInstance();
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(System.currentTimeMillis());
        cl.setTimeInMillis(PrefManager.getUserNaiss(getApplicationContext()));
        int age =current.get(Calendar.YEAR)-cl.get(Calendar.YEAR);

        ScrollView myScrollView = (ScrollView) findViewById(R.id.scrollView);
        //myScrollView.getLayoutParams().height = (valeur);


        //LocalDate birthdate = new LocalDate (1970, 1, 20);
        //LocalDate now = new LocalDate();
        //Years age = Years.yearsBetween(birthdate, now);
        Analytics application = (Analytics) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Profil");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());


        phone=PrefManager.getUserMail(getApplicationContext());
        name = PrefManager.getUserName(getApplicationContext());
        mail= PrefManager.getUserProfession(getApplicationContext());
        //address =PrefManager.getUserAdresse(getApplicationContext());

        profession=PrefManager.getUserProfession(getApplicationContext());
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        invokeWS();
        // Set Cancelable as False
        prgDialog.setCancelable(false);


        setupDrawerLayout();
        //Toast.makeText(getApplicationContext(), PrefManager.getUserSexe(getApplicationContext()), Toast.LENGTH_LONG).show();


        // load avatar and name from authorId
        ImageView avatar = (ImageView) findViewById(R.id.profilhomme);
        ImageView avatar2 = (ImageView) findViewById(R.id.profilfemme);
        if(PrefManager.getUserSexe(getApplicationContext()).equals("M")){
            avatar.setVisibility(View.VISIBLE);
        }else{
            avatar2.setVisibility(View.VISIBLE);


        }

        TextView txt_pseudo = (TextView) findViewById(R.id.pseudo);
        TextView txt_profession = (TextView) findViewById(R.id.profession);
        TextView txt_age = (TextView) findViewById(R.id.age);
        text_stat_perso = (TextView) findViewById(R.id.statperso);
        //TextView userAddress = (TextView) findViewById(R.id.useraddress);
        //TextView userMail = (TextView) findViewById(R.id.usermail);
        //TextView userPhone = (TextView) findViewById(R.id.user_phone);

        //avatar.setImageResource(R.drawable.photoprofil);
        // Verifier si name est null or vide
        if (!TextUtils.isEmpty(name)) {
            txt_pseudo.setText(name);
        }
        // Verifier si address est null or vide
        //if (!TextUtils.isEmpty(address)) {
        //    userAddress.setText("Adresse : "+address);
        //}
        // Verifier si mail est null or vide
        if (age!=0){
            txt_age.setText(age +" "+"ans");
//            userMail.setText("Profession : "+mail);
        }
        // Verifier si phone est null or vide
        if(!TextUtils.isEmpty(profession)&&!profession.equals("null")&&profession!="Aucun"&&!profession.equals(null)){

            txt_profession.setText(profession);
        }



    }

    public void invokeWS(){

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ConfigurationVille.Debut_WS+"/classement/classementgnr", new AsyncHttpResponseHandler() {
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

                        JSONArray jarray = obj.getJSONArray("classement");
                        mAdapter = new AdapterQuestion(MyAccountActivity.this);

                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject object = jarray.getJSONObject(i);
                            if(object.getString("pseudo").equals(PrefManager.getUserName(getApplicationContext()))){

                                text_stat_perso.setText("Vos contribution(s):"+" "+object.getInt("score"));
                                mAdapter.addSeparatorItem(object.getString("pseudo") + "     " + object.getInt("score") + " contibutions ");

                            }else {
                                mAdapter.addItem(object.getString("pseudo") + "     " + object.getInt("score") + " contributions ");

                            }




                        }
                        listView.setAdapter(mAdapter);

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

    public void modifier(View view) {

        Intent intent = new Intent(MyAccountActivity.this, InfoContactActivity.class);
        startActivity(intent);

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Moi citoyen");
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

                    //     case R.id.panier:
                    //         Intent intent4 = new Intent(MyAccountActivity.this, Follows.class);
                    //         startActivity(intent4);
                    //         break;

                    case R.id.concours:
                        startActivity(new Intent(MyAccountActivity.this, ConcoursActivity.class));
                        break;

                    case R.id.compte:
                        Intent intent5 = new Intent(MyAccountActivity.this, InfoContactActivity.class);
                        startActivity(intent5);
                        break;
                    case R.id.map_filter:
                        break;
                    case R.id.panier:
                        Intent intent4 = new Intent(MyAccountActivity.this, FollowActivity.class);
                        startActivity(intent4);
                        break;

                    case R.id.evenements:

                        if (PrefManager.getRatingStatus(getApplicationContext())==1) {
                            Intent intent9 = new Intent(MyAccountActivity.this, CalendarActivity.class);
                            startActivity(intent9);
                            break;

                        }else{
                            break;

                        }
                    case R.id.maps:

                        Intent intent9 = new Intent(MyAccountActivity.this, MapActivity.class);
                        startActivity(intent9);
                        break;
                    case R.id.infopratique:
                        if (PrefManager.getRatingStatus(getApplicationContext())==1) {
                            Intent pratique = new Intent(MyAccountActivity.this, PratiqueActivity.class);
                            startActivity(pratique);
                            break;
                        }else{
                            break;

                        }
                    case R.id.kiosque:

                        if (PrefManager.getRatingStatus(getApplicationContext())==1) {
                            Intent intent6 = new Intent(MyAccountActivity.this, KiosqueActivity.class);
                            startActivity(intent6);

                            break;

                        }else{
                            break;
                        }
                    case R.id.actu:
                        Intent intent7 = new Intent(MyAccountActivity.this, ImagePickActivity.class);
                        startActivity(intent7);
                        break;
                    case R.id.logout:
                        FacebookSdk.sdkInitialize(getApplicationContext());
                        LoginManager loginManager = LoginManager.getInstance();
                        loginManager.logOut();
                        LoginManager.getInstance().logOut();

                        Intent intent2 = new Intent(MyAccountActivity.this, LoginActivity.class);
                        PrefManager.setRatingStatus(getApplicationContext(),0);
                        PrefManager.setUserId(getApplicationContext(), 0);
                        PrefManager.setUserName(getApplicationContext(),null);
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
        new Navigation(MyAccountActivity.this, drawerLayout, view);
    }// end function
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.updateprofil:
                Intent intent = new Intent(MyAccountActivity.this, InfoContactActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        update = menu.findItem(R.id.updateprofil);
        update.setVisible(true);

        return true;
    }
}
