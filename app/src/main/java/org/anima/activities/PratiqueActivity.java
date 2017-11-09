package org.anima.activities;
import org.anima.animacite.R;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import org.anima.adapters.AdapterKiosque;
import org.anima.helper.Navigation;
import org.anima.utils.PrefManager;


/**
 * Created by momo on 16/03/2016.
 */
public class PratiqueActivity extends AppCompatActivity{
    private AdapterKiosque mAdapter;
    private TextView texte;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private Menu menudeux;
    private String url;
    private  int n =56;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pratique);
        texte = (TextView)findViewById(R.id.texte);
        texte.setText("Salut "+PrefManager.getUserName(getApplicationContext())+",\n\n\n L'application de feucherolles propose aux citoyens de " +
                "s’impliquer dans la politique " +
                "locale depuis leur smartphone, en participant aux concertations municipales." +
                "\n\n Il faut savoir que son utilisation reste anonyme, il est impossible pour la mairie d'accéder à toute vos données personelles!\n\n" +
                "\n Nous te remercions pour ton engagement et ta confiance :)  \n\ncoordialement; \n\nla ville de feucherolles :D");

        initToolbar();
        setupDrawerLayout();


    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Bon à savoir");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    /*
    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        ImageView avatarhomme = (ImageView) view.findViewById(R.id.avatarhomme);
        ImageView avatarfemme = (ImageView) view.findViewById(R.id.avatarfemme);
        if(PrefManager.getUserSexe(getApplicationContext()).equals("F")){
            avatarfemme.setVisibility(View.VISIBLE);
        }else{
            avatarhomme.setVisibility(View.VISIBLE);
        }
        String phone=PrefManager.getUserMail(getApplicationContext());
        String name = PrefManager.getUserName(getApplicationContext());

        TextView txtUserName = (TextView)view.findViewById(R.id.user_name);
        TextView txtUserMail = (TextView)view.findViewById(R.id.user_email);

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
                        startActivity(new Intent(PratiqueActivity.this, ConcoursActivity.class));
                        break;

                    case R.id.my_account:

                        Intent intent8 = new Intent(PratiqueActivity.this, MyAccountActivity.class);
                        startActivity(intent8);
                        break;
                    //     case R.id.panier:
                    //         Intent intent4 = new Intent(MyAccountActivity.this, Follows.class);
                    //         startActivity(intent4);
                    //         break;
                    case R.id.compte:
                        Intent intent5 = new Intent(PratiqueActivity.this, InfoContactActivity.class);
                        startActivity(intent5);
                        break;
                    case R.id.map_filter:
                        break;
                    case R.id.evenements:

                        if (PrefManager.getRatingStatus(getApplicationContext())==1) {
                            Intent intent9 = new Intent(PratiqueActivity.this, CalendarActivity.class);
                            startActivity(intent9);
                            break;

                        }else{
                            break;

                        }
                    case R.id.maps:

                        Intent intent9 = new Intent(PratiqueActivity.this, MapActivity.class);
                        startActivity(intent9);
                        break;
                    case R.id.panier:
                        Intent intent4 = new Intent(PratiqueActivity.this, FollowActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.infopratique:
                        if (PrefManager.getRatingStatus(getApplicationContext())==1) {
                            Intent pratique = new Intent(PratiqueActivity.this, PratiqueActivity.class);
                            startActivity(pratique);
                            break;

                        }else{
                            break;

                        }
                    case R.id.actu:
                        Intent intent7 = new Intent(PratiqueActivity.this, ImagePickActivity.class);
                        startActivity(intent7);
                        break;
                    case R.id.kiosque:

                        if (PrefManager.getRatingStatus(getApplicationContext())==1) {
                            Intent kiosque = new Intent(PratiqueActivity.this, KiosqueActivity.class);
                            startActivity(kiosque);

                            break;

                        }else{
                            break;
                        }
                    case R.id.logout:
                        FacebookSdk.sdkInitialize(getApplicationContext());
                        LoginManager loginManager = LoginManager.getInstance();
                        loginManager.logOut();
                        LoginManager.getInstance().logOut();

                        Intent intent2 = new Intent(PratiqueActivity.this, LoginActivity.class);
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
    }*/
    /**
     * Refactored by Saly Sakey November-07-2017
     */
    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        new Navigation(PratiqueActivity.this, drawerLayout, view);
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

}
