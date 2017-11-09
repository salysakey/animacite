package org.anima.activities;
import org.anima.animacite.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.anima.adapters.FoodListAdapter;
import org.anima.entities.Food;
import org.anima.entities.Project;
import org.anima.helper.Navigation;
import org.anima.utils.ConfigurationVille;
import org.anima.utils.PrefManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by thiam on 05/11/2015.
 */
public class FollowActivity extends AppCompatActivity {
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object
    EditText emailET;
    // Passwprd Edit View Object
    EditText pwdET;

    String[] titles;
    String[] descriptifs;
    String[] couleurs;
    TextView txtEmpty;
    int [] idents;
    String[] listPictures;
    String[] resumes;
    int[] types;
    long[] date;


    ArrayList<Food> listFood;
    private StaggeredGridView mGridView;
    EditText search;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private MenuItem menuSearch;
    private MenuItem menuHide;
    private Menu menudeux;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow);

        initToolbar();
        setupDrawerLayout();


        mGridView = (StaggeredGridView) findViewById(R.id.grid);
        search = (EditText) findViewById(R.id.search);


        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        RequestParams params = new RequestParams();

        // Put Http parameter username with value of Email Edit View control
        params.put("userId", ""+PrefManager.getUserId(getApplicationContext()));
        invokeWS(params);
    }

    public void showPickActivity(int index) {
        Intent intent = new Intent( FollowActivity.this , InfoActivity.class);
        Food selectedFood = listFood.get(index);

        Bundle b = new Bundle();
        b.putString("description", selectedFood.getName());
        b.putString("titre", selectedFood.getPrice());
        b.putInt("id", selectedFood.getId());
        b.putInt("idp", selectedFood.getIdent());
        b.putInt("position", index);
        b.putString("pictureUrl", selectedFood.getPictureUrl());
        b.putInt("tipe", selectedFood.getType());
        b.putString("couleur", selectedFood.getCouleur());

        intent.putExtras(b);

        startActivity(intent);
    }

    public void invokeWS(RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ConfigurationVille.Debut_WS+"/showfollow/doshow",params, new AsyncHttpResponseHandler() {
        //client.get("http://192.168.0.10:8080/Anima/rest/showfollow/doshow",params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                try {
                    Calendar cl = Calendar.getInstance();
                    Calendar current = Calendar.getInstance();
                    current.setTimeInMillis(System.currentTimeMillis());
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {

                        JSONArray jarray = obj.getJSONArray("phoneNumber");
                        listPictures = new String[jarray.length()];
                        titles = new String[jarray.length()];
                        descriptifs = new String[jarray.length()];
                        resumes = new String[jarray.length()];
                        couleurs = new String[jarray.length()];
                        types = new int[jarray.length()];
                        date = new long[jarray.length()];
                        idents = new int[jarray.length()];

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject object = jarray.getJSONObject(i);


                            Project project = new Project();

                            project.setTitle(object.getString("Titre"));
                            project.setDescription(object.getString("description"));
                            project.setPicture(object.getString("picture"));
                            project.setResume(object.getString("resume"));
                            project.setType(object.getInt("type"));
                            project.setCouleur(object.getString("couleur"));

                            cl.setTimeInMillis(project.getDate_fin());
                            date[i]=current.get(Calendar.DAY_OF_MONTH)-cl.get(Calendar.DAY_OF_MONTH);
                            titles[i]=project.getTitle();
                            couleurs[i] = project.getCouleur();
                            descriptifs[i]=project.getDescription();
                            resumes[i] = project.getResume();
                            types[i] = project.getType();
                            //listPictures[i]="http://i.imgur.com/DvpvklR.png";
                            listPictures[i] = project.getPicture();
                            idents[i] = object.getInt("id");
                        }

                        init();
                        prgDialog.hide();
                        //Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                    }
                    // Else display error message
                    else {
                        //errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), "Pas de projet actuellement", Toast.LENGTH_LONG).show();
                        prgDialog.hide();
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

    public void filtrer() {
        // retourner la chaine saisie par l'utilisateur
        String name = search.getText().toString();
        // créer une nouvelle liste qui va contenir la résultat à afficher
        ArrayList<Food> listFoodNew = new ArrayList<Food>();

        for (Food food : listFood) {
            // si le nom du food commence par la chaine saisie , ajouter-le !
            if (food.getName().toLowerCase().toString().startsWith(name)) {
                listFoodNew.add(food);
            }
        }
        // vider la liste
        mGridView.setAdapter(null);
        if (listFoodNew.size() == 0) {

            listFoodNew.add(new Food(100, "Pas d'élements.. réessayer !",
                    "jfjtdr","", "",1,1,1));
        }
        // ajouter la nouvelle liste
        mGridView.setAdapter(new FoodListAdapter(getApplicationContext(), listFoodNew));
    }

    private void init(){

        listFood = new ArrayList<Food>();

        System.out.println(titles);

        for (int i = 0; i < listPictures.length; i++) {
            listFood.add(new Food(i + 1, titles[i], listPictures[i],resumes[i],
                    descriptifs[i],types[i],idents[i],date[i], couleurs[i]));
        }

        mGridView.setAdapter(new FoodListAdapter(getApplicationContext(), listFood));
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                filtrer();
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(FollowActivity.this, "Selected Position: " + position, Toast.LENGTH_SHORT).show();
                showPickActivity(position);
            }
        });


    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppCompatActivity dodo = new AppCompatActivity();
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Mes coups de coeur");
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
                        startActivity(new Intent(FollowActivity.this, ConcoursActivity.class));
                        break;
                    case R.id.my_account:

                        Intent intent4 = new Intent(FollowActivity.this, MyAccountActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.actu:
                        Intent intent6 = new Intent(FollowActivity.this, ImagePickActivity.class);
                        startActivity(intent6);
                        break;
                    case R.id.evenements:

                        if (PrefManager.getRatingStatus(getApplicationContext())==1) {
                            Intent intent9 = new Intent(FollowActivity.this, CalendarActivity.class);
                            startActivity(intent9);
                            break;

                        }else{
                            break;

                        }
                    case R.id.maps:

                        Intent intent9 = new Intent(FollowActivity.this, MapActivity.class);
                        startActivity(intent9);
                        break;
                    case R.id.compte:
                        Intent intent5 = new Intent(FollowActivity.this, InfoContactActivity.class);
                        startActivity(intent5);
                        break;
                    case R.id.logout:
                        FacebookSdk.sdkInitialize(getApplicationContext());
                        LoginManager loginManager = LoginManager.getInstance();
                        loginManager.logOut();
                        LoginManager.getInstance().logOut();

                        Intent intent2 = new Intent(FollowActivity.this, LoginActivity.class);
                        PrefManager.setRatingStatus(getApplicationContext(), 0);
                        PrefManager.setUserName(getApplicationContext(), null);
                        PrefManager.setUserId(getApplicationContext(), 0);
                        PrefManager.setUserMail(getApplicationContext(), null);
                        PrefManager.setUserProfession(getApplicationContext(), null);
                        PrefManager.setUserAdresse(getApplicationContext(), null);
                        PrefManager.setLatitude(getApplicationContext(), null);
                        PrefManager.setLongitude(getApplicationContext(), null);
                        startActivity(intent2);

                        break;
                    case R.id.kiosque:

                        if (PrefManager.getRatingStatus(getApplicationContext())==1) {
                            Intent intent7 = new Intent(FollowActivity.this, KiosqueActivity.class);
                            startActivity(intent7);

                            break;

                        }else{
                            break;
                        }
                    case R.id.login:

                        Intent intent3 = new Intent(FollowActivity.this, LoginActivity.class);

                        startActivity(intent3);

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
        new Navigation(FollowActivity.this, drawerLayout, view);
    }// end function

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        menuSearch = menu.findItem(R.id.menu_search);
        menuHide = menu.findItem(R.id.menu_hide);
        if(search.getVisibility()==View.VISIBLE){
            menuSearch.setVisible(false);
            menuHide.setVisible(true);
        }else{
            menuSearch.setVisible(true);
            menuHide.setVisible(false);
        }
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_search:
                search.setVisibility(View.VISIBLE);
                menuSearch.setVisible(false);
                menuHide.setVisible(true);
                return true;
            case R.id.menu_hide:
                search.setVisibility(View.GONE);
                menuSearch.setVisible(true);
                menuHide.setVisible(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
