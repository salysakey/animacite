package org.anima.helper;

import android.content.ContextWrapper;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import org.anima.activities.CalendarActivity;
import org.anima.activities.ConcoursActivity;
import org.anima.activities.FollowActivity;
import org.anima.activities.ImagePickActivity;
import org.anima.activities.InfoContactActivity;
import org.anima.activities.KiosqueActivity;
import org.anima.activities.LoginActivity;
import org.anima.activities.MapActivity;
import org.anima.activities.MyAccountActivity;
import org.anima.activities.PratiqueActivity;
import org.anima.animacite.R;
import org.anima.utils.PrefManager;

/**
 * Created by salysakey on 11/7/17.
 */



public class Navigation {
    public Navigation(final ContextWrapper context, DrawerLayout drawer, NavigationView view){
        setupDrawerLayout(context, drawer, view);
    }

    DrawerLayout drawerLayout;
    private void setupDrawerLayout(final ContextWrapper context, DrawerLayout drawer, NavigationView view) {
        drawerLayout = drawer;
        Menu menudeux;
        View headerLayout = view.getHeaderView(0);
        /** HEADER **/
        ImageView avatarhomme = (ImageView) headerLayout.findViewById(R.id.avatarhomme);
        ImageView avatarfemme = (ImageView) headerLayout.findViewById(R.id.avatarfemme);
        if(PrefManager.getUserSexe(context.getApplicationContext()).equals("F")){
            avatarfemme.setVisibility(View.VISIBLE);
        }else{
            avatarhomme.setVisibility(View.VISIBLE);
        }
        String phone=PrefManager.getUserMail(context.getApplicationContext());
        String name = PrefManager.getUserName(context.getApplicationContext());

        TextView txtUserName = (TextView)headerLayout.findViewById(R.id.user_name);
        TextView txtUserMail = (TextView)headerLayout.findViewById(R.id.user_email);

        if (!TextUtils.isEmpty(phone)) {
            txtUserMail.setText(phone);
        }

        if (!TextUtils.isEmpty(name)) {
            txtUserName.setText(name);
        }

        menudeux = view.getMenu();

        if (PrefManager.getRatingStatus(context.getApplicationContext())==1) {
            menudeux.findItem(R.id.logout).setVisible(true);
            menudeux.findItem(R.id.my_account).setVisible(true);
            menudeux.findItem(R.id.maps).setVisible(true);
            menudeux.findItem(R.id.evenements).setVisible(true);
            menudeux.findItem(R.id.kiosque).setVisible(true);
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

                    //     case R.id.panier:
                    //         Intent intent4 = new Intent(MyAccountActivity.this, Follows.class);
                    //         startActivity(intent4);
                    //         break;

                    //"Moi citoyen"
                    case R.id.my_account:
                        Intent intent8 = new Intent(context.getApplicationContext(), MyAccountActivity.class);
                        context.startActivity(intent8);
                        break;
                    // "Concours"
                    case R.id.concours:
                        context.startActivity(new Intent(context.getApplicationContext(), ConcoursActivity.class));
                        break;

                    // "Unknown"
                    case R.id.compte:
                        Intent intent5 = new Intent(context.getApplicationContext(), InfoContactActivity.class);
                        context.startActivity(intent5);
                        break;

                    // "Unknown"
                    case R.id.map_filter:
                        break;
                    // "Mes coups de coeur"
                   case R.id.panier:
                        Intent intent4 = new Intent(context.getApplicationContext(), FollowActivity.class);
                        context.startActivity(intent4);
                        break;

                    // "Mes evenements"
                    case R.id.evenements:
                        if (PrefManager.getRatingStatus(context.getApplicationContext())==1) {
                            Intent intent9 = new Intent(context.getApplicationContext(), CalendarActivity.class);
                            context.startActivity(intent9);
                            break;

                        }else{
                            break;
                        }

                    case R.id.maps:
                        Intent intent9 = new Intent(context.getApplicationContext(), MapActivity.class);
                        context.startActivity(intent9);
                        break;

                    case R.id.infopratique:
                        if (PrefManager.getRatingStatus(context.getApplicationContext())==1) {
                            Intent pratique = new Intent(context.getApplicationContext(), PratiqueActivity.class);
                            context.startActivity(pratique);
                            break;
                        }else{
                            break;

                        }

                    //"Kiosque"
                    case R.id.kiosque:

                        if (PrefManager.getRatingStatus(context.getApplicationContext())==1) {
                            Intent intent6 = new Intent(context.getApplicationContext(), KiosqueActivity.class);
                            context.startActivity(intent6);

                            break;

                        }else{
                            break;
                        }

                     // "Dans ma ville"
                    case R.id.actu:
                        Intent intent7 = new Intent(context.getApplicationContext(), ImagePickActivity.class);
                        context.startActivity(intent7);
                        break;

                    case R.id.login:
                        Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
                        context.startActivity(intent);
                        break;
                    case R.id.logout:
                        FacebookSdk.sdkInitialize(context.getApplicationContext());
                        LoginManager loginManager = LoginManager.getInstance();
                        loginManager.logOut();
                        LoginManager.getInstance().logOut();

                        Intent intent2 = new Intent(context.getApplicationContext(), LoginActivity.class);
                        PrefManager.setRatingStatus(context.getApplicationContext(),0);
                        PrefManager.setUserId(context.getApplicationContext(), 0);
                        PrefManager.setUserName(context.getApplicationContext(),null);
                        PrefManager.setUserMail(context.getApplicationContext(), null);
                        PrefManager.setUserProfession(context.getApplicationContext(), null);
                        PrefManager.setUserAdresse(context.getApplicationContext(), null);
                        PrefManager.setLatitude(context.getApplicationContext(), null);
                        PrefManager.setLongitude(context.getApplicationContext(), null);
                        context.startActivity(intent2);

                        break;
                }
                return true;
            }
        });
    }
}
