package org.anima.activities;
import org.anima.helper.Navigation;
import org.anima.utils.ConfigurationVille;
import org.anima.animacite.R;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.anima.adapters.AdapterKiosque;
import org.anima.utils.PrefManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by momo on 16/03/2016.
 */
public class KiosqueActivity extends AppCompatActivity{
    private AdapterKiosque mAdapter;
    private ListView listView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private Menu menudeux;

    private String url;
    private int n =56;
    private String[] tab;
    private int t=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.kiosque);
        listView = (ListView)findViewById(R.id.list);

        initToolbar();
        setupDrawerLayout();
        invokeWSKiosque();

        mAdapter = new AdapterKiosque(this);

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                if (tab[position]=="") {

                }else {

                    //Toast.makeText(KiosqueActivity.this, "No App" + tab[position], Toast.LENGTH_SHORT).show();


                    //url = "http://feucherolles.fr/docu/vav/0" + tab[position] + ".pdf";
                    //url = "http://dev.animacite.fr/images/anima.pdf";
                    url = tab[position];//"http://dev.animacite.fr/images/anima" + tab[position] + ".pdf";
                    //File pdfFile = new File(Environment.getExternalStorageDirectory() + "http://feucherolles.fr/docu/vav/056.pdf");  // -> filename = maven.pdf
                    //Uri path = Uri.fromFile(pdfFile);
                    Uri path = Uri.parse(url);
                    Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                    pdfIntent.setDataAndType(path, "application/pdf");
                    pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    try {
                        startActivity(pdfIntent);
                    } catch (ActivityNotFoundException e) {
                        //Toast.makeText(KiosqueActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();

                        //download
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(KiosqueActivity.this);
                        // Setting Dialog Title
                        alertDialog.setTitle("Pas d'application disponible pour lire le fichier pdf :/ ");
                        alertDialog.setIcon(R.drawable.petite_image);
                        // Setting Dialog Message
                        alertDialog.setMessage("Télécharger le fichier pdf ?");
                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("Oui",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(url));
                                        startActivity(i);
                                        //Move to Next screen
                                        dialog.cancel();
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

                    }
                }
            }

        });

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Journal de la commune");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    public void invokeWSKiosque() {
        // Show Progress Dialog

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //client.get("http://192.168.0.14:8080/Anima/rest/login/dologin",params ,new AsyncHttpResponseHandler() {
        client.get(ConfigurationVille.Debut_WS + "/actu/kiosque", new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {

                try {

                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj !=null) {
                        JSONArray jarray = obj.getJSONArray("kiosque");
                        int h=0;
                        int a=0;
                        tab=new String[jarray.length()*2];


                        for (int i = jarray.length()-1; i >= 0 ; i--) {

                            // JSON Object
                            JSONObject object = jarray.getJSONObject(i);
                            // When the JSON response has status boolean value assigned with true
                            if(h!=object.getInt("annee")){
                                mAdapter.addSeparatorItem(""+object.getInt("annee"));
                                h=object.getInt("annee");
                                a++;
                            }

                            mAdapter.addItem(ConfigurationVille.KIOSQUE_PAPER_NAME +" "+object.getString("edition"));
                            tab[a]=object.getString("lien");

                            a++;

                        }

                        listView.setAdapter(mAdapter);
                    }



                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Une erreur est survenue lors du charge", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
                // Hide Progress Dialog


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


    private  void setupDrawerLayout(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        new Navigation(KiosqueActivity.this, drawerLayout, view);
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
            menudeux.findItem(R.id.evenements).setVisible(true);
            menudeux.findItem(R.id.my_account).setVisible(true);
            menudeux.findItem(R.id.maps).setVisible(true);
            //menudeux.findItem(R.id.stat).setVisible(true);
            //menudeux.findItem(R.id.parametre).setVisible(true);
            //menudeux.findItem(R.id.infopratique).setVisible(true);
            menudeux.findItem(R.id.panier).setVisible(true);
            menudeux.findItem(R.id.actu).setVisible(true);
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
                        startActivity(new Intent(KiosqueActivity.this, ConcoursActivity.class));
                        break;
                    case R.id.my_account:

                        Intent intent8 = new Intent(KiosqueActivity.this, MyAccountActivity.class);
                        startActivity(intent8);
                        break;
                    //     case R.id.panier:
                    //         Intent intent4 = new Intent(MyAccountActivity.this, Follows.class);
                    //         startActivity(intent4);
                    //         break;
                    case R.id.infopratique:
                        if (PrefManager.getRatingStatus(getApplicationContext())==1) {
                            Intent pratique = new Intent(KiosqueActivity.this, PratiqueActivity.class);
                            startActivity(pratique);
                            break;

                        }else{
                            break;

                        }
                    case R.id.compte:
                        Intent intent5 = new Intent(KiosqueActivity.this, InfoContactActivity.class);
                        startActivity(intent5);
                        break;
                    case R.id.map_filter:
                        break;
                    case R.id.evenements:

                        if (PrefManager.getRatingStatus(getApplicationContext())==1) {
                            Intent intent9 = new Intent(KiosqueActivity.this, CalendarActivity.class);
                            startActivity(intent9);
                            break;

                        }else{
                            break;

                        }
                    case R.id.maps:

                        Intent intent9 = new Intent(KiosqueActivity.this, MapActivity.class);
                        startActivity(intent9);
                        break;
                    case R.id.panier:
                        Intent intent4 = new Intent(KiosqueActivity.this, FollowActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.actu:
                        Intent intent7 = new Intent(KiosqueActivity.this, ImagePickActivity.class);
                        startActivity(intent7);
                        break;
                    case R.id.logout:
                        FacebookSdk.sdkInitialize(getApplicationContext());
                        LoginManager loginManager = LoginManager.getInstance();
                        loginManager.logOut();
                        LoginManager.getInstance().logOut();

                        Intent intent2 = new Intent(KiosqueActivity.this, LoginActivity.class);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
