package org.anima.activities;
import org.anima.animacite.R;
import org.anima.helper.Navigation;
import org.anima.utils.ConfigurationVille;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
/*
import com.zimberland.community.R;
import com.zimberland.community.adapter.DrawerListAdapter;
import com.zimberland.community.business.MenuDrawerItem;
import com.zimberland.community.connector.LocationConnector;
import com.zimberland.community.helper.FragmentHelper;
import com.zimberland.community.listener.LocationListener;
import com.zimberland.community.utils.ActionBarUtils;
import com.zimberland.community.utils.MapUtils;
import com.zimberland.community.utils.MenuUtils;
import com.zimberland.community.widget.TalibeBullDialog;
*/

import org.anima.utils.PrefManager;


public class MapActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, LocationListener, OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    public static final String CURRENT_POSITION = "CURRENT POSITION";

    private DrawerLayout drawerLayout2;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private Menu menudeux;
    private ListView listView;
    private ActionBar actionBar;
    private ActionBarDrawerToggle toggle;
    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;
    private Tracker mTracker;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private GoogleMap map;
    private GoogleMapOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        MapsInitializer.initialize(this);

        Analytics application = (Analytics) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Map");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        initToolbar();
        setupDrawerLayout();
        //drawerLayout.setDrawerListener(toggle);
        //map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        //initializeDrawerMenu(savedInstanceState);
        //initializeConnector();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Map");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private void setupDrawerLayout(){
        drawerLayout2 = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        new Navigation(MapActivity.this, drawerLayout2, view);
    }
    /*
    private void setupDrawerLayout() {
        drawerLayout2 = (DrawerLayout) findViewById(R.id.drawer_layout);

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
                drawerLayout2.closeDrawers();
                //fab.setVisibility(View.GONE);

                switch (menuItem.getItemId()) {
                    case R.id.concours:
                        startActivity(new Intent(MapActivity.this, ConcoursActivity.class));
                        break;
                    case R.id.my_account:

                        Intent intent8 = new Intent(MapActivity.this, MyAccountActivity.class);
                        startActivity(intent8);
                        break;
                    //     case R.id.panier:
                    //         Intent intent4 = new Intent(MyAccountActivity.this, Follows.class);
                    //         startActivity(intent4);
                    //         break;
                    case R.id.infopratique:
                        if (PrefManager.getRatingStatus(getApplicationContext())==1) {
                            Intent pratique = new Intent(MapActivity.this, PratiqueActivity.class);
                            startActivity(pratique);
                            break;

                        }else{
                            break;

                        }
                    case R.id.compte:
                        Intent intent5 = new Intent(MapActivity.this, InfoContactActivity.class);
                        startActivity(intent5);
                        break;
                    case R.id.map_filter:
                        break;
                    case R.id.evenements:

                        if (PrefManager.getRatingStatus(getApplicationContext())==1) {
                            Intent intent9 = new Intent(MapActivity.this, CalendarActivity.class);
                            startActivity(intent9);
                            break;

                        }else{
                            break;

                        }
                    case R.id.kiosque:

                        if (PrefManager.getRatingStatus(getApplicationContext())==1) {
                            Intent kiosque = new Intent(MapActivity.this, KiosqueActivity.class);
                            startActivity(kiosque);

                            break;

                        }else{
                            break;
                        }
                    case R.id.panier:
                        Intent intent4 = new Intent(MapActivity.this, FollowActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.actu:
                        Intent intent7 = new Intent(MapActivity.this, ImagePickActivity.class);
                        startActivity(intent7);
                        break;
                    case R.id.logout:
                        FacebookSdk.sdkInitialize(getApplicationContext());
                        LoginManager loginManager = LoginManager.getInstance();
                        loginManager.logOut();
                        LoginManager.getInstance().logOut();

                        Intent intent2 = new Intent(MapActivity.this, LoginActivity.class);
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
                drawerLayout2.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeMap();
    }



    @Override
    protected void onResume() {
        super.onResume();

        //#TODO get all talibe of the Mbed-mi (positions)
    }

    private void initializeActionBar(){
        actionBar = getActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    private void initializeMap(){
        if(map == null){

            ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
            // map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(FRAGMENT_ID)).getMap();
            // map= ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
/*
            LatLng feuch = new LatLng(48.872724, 1.972612);

            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(feuch, 15));

            map.addMarker(new MarkerOptions()
                    .title("feucherolles")
                    .position(feuch));

                    */

            //  map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(11.562276, 104.920292), 10));
        }
        /*

        if(map == null){
            throw new IllegalStateException("Unable to initialize the map");
        }

        map.setOnMarkerClickListener(this);
        */

        findViewById(R.id.current_position).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //#TODO get the current position when the talibe decides it
                //#TODO use radio button instead
                Location location = new Location(CURRENT_POSITION);
                location.setLatitude(48.0);
                location.setLongitude(2.0);


            }
        });
    }



    private void initializeDrawerMenu(Bundle savedInstanceState){
        mTitle = mDrawerTitle = getTitle();

        // load slide menu items


        // nav drawer icons from resources

        //drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //listView = (ListView) findViewById(R.id.list_slidermenu);



        // adding nav drawer items to array
        // Account


        // Recycle the typed array
        navMenuIcons.recycle();

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "Nothing selected");
            }
        });

        // setting the nav drawer list adapter


        initializeActionBar();

        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.anima, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(toggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            //MenuUtils.displayView(this, 0, R.id.main_layout);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }



    /* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
//        boolean drawerOpen = drawerLayout.isDrawerOpen(listView);

        return super.onPrepareOptionsMenu(menu);
    }




    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

/*


        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setMyLocationEnabled(true);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);

        */


        map = googleMap;
        LatLng city = new LatLng(ConfigurationVille.LATITUDE_VILLE, ConfigurationVille.LONGITUDE_VILLE);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(city, 13));

        map.addMarker(new MarkerOptions()
                .title(ConfigurationVille.CITY_NAME)
                .position(city));
        //PLESSISVILLE
        map.addMarker(new MarkerOptions()
                .title("Amphithéâtre Léo-Paul-Boutin")
                .snippet("1051 Rue Saint Jean")
                .position(new LatLng(46.217513, -71.787351)));

        map.addMarker(new MarkerOptions()
                .title("Bibliothèque Linette Jutras-Laperle")
                .snippet("1800 Rue Saint Calixte")
                .position(new LatLng(46.219840, -71.770546)));

        map.addMarker(new MarkerOptions()
                .title("Carrefour de l’Érable")
                .snippet("1280 avenue Trudelle")
                .position(new LatLng(46.214154, -71.771597)));
        map.addMarker(new MarkerOptions()
                .title("Centre communautaire")
                .snippet("1470 avenue Trudelle")
                .position(new LatLng(46.216004, -71.774387)));
        map.addMarker(new MarkerOptions()
                .title("Polyvalente La Samare")
                .snippet("1159 rue Saint-Jean")
                .position(new LatLng(46.223003, -71.778794)));
        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.home49)));
        map.addMarker(new MarkerOptions()
                .title("École Jean-Rivard ")
                .snippet("1850 avenue Rousseau")
                .position(new LatLng(46.224368, -71.771883)));
        map.addMarker(new MarkerOptions()
                .title("École Saint-Édouard")
                .snippet("1666 rue Savoie")
                .position(new LatLng(46.221032, -71.776799)));
        map.addMarker(new MarkerOptions()
                .title("École Sainte-Famille")
                .snippet("1114 rue Savoie")
                .position(new LatLng(46.216256, -71.784195)));
        map.addMarker(new MarkerOptions()
                .title("Parc de la Rivière Bourdon")
                .snippet("1280 avenue Trudelle")
                .position(new LatLng(46.214154, -71.771597)));
        map.addMarker(new MarkerOptions()
                .title("Parc Bourassa")
                .snippet("rue des Pins")
                .position(new LatLng(46.222689, -71.762026)));
        map.addMarker(new MarkerOptions()
                .title("Parc Garneau ")
                .snippet("1780 rue Garneau")
                .position(new LatLng(46.228735, -71.781529)));
        map.addMarker(new MarkerOptions()
                .title("Piscine municipale")
                .snippet("1470 Avenue Trudelle")
                .position(new LatLng(46.216004, -71.774387))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pool)));
        map.addMarker(new MarkerOptions()
                .title("Terrain de tennis")
                .snippet("1470 Avenue Trudelle")
                .position(new LatLng(46.216004, -71.774387)));
        map.addMarker(new MarkerOptions()
                .title("Terrain de soccer")
                .snippet("1159 rue Saint-Jean")
                .position(new LatLng(46.218543, -71.785698)));
        map.addMarker(new MarkerOptions()
                .title("Stationnement Bélanger")
                .snippet("1658 Av. St-Louis")
                .position(new LatLng(46.220429, -71.772619))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_parking)));
        map.addMarker(new MarkerOptions()
                .title("Stationnement poisson")
                .snippet("1640 av. St-Édouard")
                .position(new LatLng(46.219959, -71.773533))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_parking)));
        map.addMarker(new MarkerOptions()
                .title("Stationnement Place du Centre")
                .snippet("1587 av. St-Louis")
                .position(new LatLng(46.219881, -71.771882))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_parking)));
        map.addMarker(new MarkerOptions()
                .title("Stationnement Hotel de Ville")
                .snippet("1700 rue Saint-Calixte")
                .position(new LatLng(46.218720, -71.771692))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_parking)));
        map.addMarker(new MarkerOptions()
                .title("Stationnement bibliothèque")
                .snippet("1800 rue Saint-Calixte")
                .position(new LatLng(46.219840, -71.770546))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_parking)));
        map.addMarker(new MarkerOptions()
                .title("Borne recharge véhicule électrique Hotel de Ville")
                .snippet("1700 rue Saint-Calixte")
                .position(new LatLng(46.218720, -71.771692))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_recharge)));
        map.addMarker(new MarkerOptions()
                .title("Borne recharge véhicule électrique Carrefour de l'Erable")
                .snippet("1280 avenue Trudelle")
                .position(new LatLng(46.214154, -71.771597))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_recharge)));
        map.addMarker(new MarkerOptions()
                .title("Borne recharge véhicule Bélanger")
                .snippet("1658 Av. St-Louis")
                .position(new LatLng(46.220429, -71.772619))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_recharge)));
        map.addMarker(new MarkerOptions()
                .title("CPE Fleur de soleil")
                .snippet("2081 Rue Lupien")
                .position(new LatLng(46.225530, -71.771582)));
        map.addMarker(new MarkerOptions()
                .title("CPE la Girouette")
                .snippet("1514 Av St Louis")
                .position(new LatLng(46.219083, -71.770842)));

        //RIVIERE DU LOUP
        map.addMarker(new MarkerOptions ()
                .title("Service | Hôtel de ville")
                .snippet("65 Rue de l'Hôtel de Ville, Rivière-du-Loup, QC G5R 3Y7")
                .position(new LatLng(
                        47.8360068,-69.5367364
                )));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.8679299,-69.5438899
                ))
                .title("Parc | Belvédère de la Pointe")
                .snippet("4-A route de l'Anse-au-Persil, Rivière-du-Loup")
        );

        map.addMarker(new MarkerOptions()
                .title("Parc | Parc de la Pointe")
                .position(new LatLng(
                        47.8603052,-69.55217299999998
                ))
                .snippet("80 rue Mackay, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.8566334,-69.52637249999998
                ))
                .title("Parc | Parc Painchaud")
                .snippet("120 Rue Painchaud, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.85228499999999,-69.5362465
                ))
                .title("Parc | Parc Cartier")
                .snippet("11 Rue Wilfrid Laurier, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.8473552,-69.50332939999998
                ))
                .title("Parc | Parc des Cônes")
                .snippet("228 Rue des Cônes, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.8398766,-69.5420322
                ))
                .title("Parc | Parc Saint-Marc")
                .snippet("16-18 rue Lafontaine, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.8377546,-69.53826240000001
                ))
                .title("Parc | Parc Blais")
                .snippet("121 rue Lafontaine, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.8371752,-69.53638109999997
                ))
                .title("Service | Théâtre extérieur la Goëte")
                .snippet("67-69 rue du Rocher, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.83146009999999,-69.5316133
                ))
                .title("Parc | Parc des Chutes")
                .snippet("25 Rue de la Chute, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.8306799,-69.54949799999997
                ))
                .title("Parc | Parc des Epinettes")
                .snippet("9 rue des Epinettes, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.83053049999999,-69.5433615
                ))
                .title("Parc | Parc du Campus-et-de-la-Cité")
                .snippet("30 rue Desjardins, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.83019110000001,-69.5337586
                ))
                .title("Parc | Aire de repos de Frontenac")
                .snippet("423 rue Lafontaine, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.8291603,-69.52686219999998
                ))
                .title("Parc | Parc de la Croix")
                .snippet("2 rue Sainte-Claire, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.83049019999999,-69.51619870000002
                ))
                .title("Parc | Parc des Princes")
                .snippet("5 rue Rosaire-Gendron, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.8272527,-69.5230325
                ))
                .title("Parc | Parc Vézina")
                .snippet("16 rue Vézina, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.8231705,-69.5312773
                ))
                .title("Parc | Parc Dionne")
                .snippet("84 rue Fraserville, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.82233369999999,-69.54719360000001
                ))
                .title("Parc | Parc des Jonquilles")
                .snippet("14 rue des Jonquilles, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.8251394,-69.55863369999997
                ))
                .title("Parc | Parc Fraser")
                .snippet("258 rue Fraser, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.8188242,-69.5491864
                ))
                .title("Parc | Parc du Ruisseau")
                .snippet("102 rue des Pivoines, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.8200656,-69.50880419999999
                ))
                .title("Parc | Parc Mailloux")
                .snippet("1 rue de la Rive, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.8220022,-69.50009060000002
                ))
                .title("Parc | Parc Goscobec")
                .snippet("15 rue Alfred Dionne, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.8152358,-69.53855709999999
                ))
                .title("Parc | Parc des Pruches")
                .snippet("40 rue des Trembles, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.8168264,-69.52978000000002
                ))
                .title("Parc | Parc des Quatre-Vents")
                .snippet("55 rue Lionel-Chalifour, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                        47.809271,-69.5432328
                ))
                .title("Parc | Parc Fraserville")
                .snippet("232 chemin Fraserville, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parc)));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.82709620000001,-69.5292632
                        )
                )
                .title("Stationnement | Carré Dubé")
                .snippet("555 rue Lafontaine, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parking)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.83378219999999,-69.53833050000003
                        )
                )
                .title("Stationnement | Stationnement Devost")
                .snippet("35 rue Amyot, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parking)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.8353092,-69.53828759999999
                        )
                )
                .title("Stationnement | Stationnement rue de l'Hotel de Ville")
                .snippet("86 rue de l'Hôtel-de-Ville, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parking)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.839236,-69.537286
                        )
                )
                .title("Stationnement | Stationnement Beaubien/Iberville")
                .snippet("Entre les rues Beaubien et Iberville, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parking)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.825997,-69.535749
                        )
                )
                .title("Stationnement | Stationnement rue Laval")
                .snippet("Rue Laval, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parking)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.831155,-69.525570
                        )
                )
                .title("Stationnement | Stationnements rue Gilles")
                .snippet("Rue Gilles, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parking)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.834736,-69.519805
                        )
                )
                .title("Stationnement | Stationnements ski de fond/raquette")
                .snippet("8 rue P.E Grandbois, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parking)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.843648,-69.505567
                        )
                )
                .title("Stationnement | Stationnements pour le vélo de montagne")
                .snippet("Rue Henri-Percival-Monsarrat, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parking)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.830346,-69.535824
                        )
                )
                .title("Stationnement | Stationnements CLSC côté rue Saint-Laurent")
                .snippet("22 rue Saint-Laurent, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parking)));


        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.827127,-69.536768
                        )
                )
                .title("Sport | Stade de la Cité des Jeunes")
                .snippet("75 rue Frontenac, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_sport)));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.828107,-69.539689
                        )
                )
                .title("Education | École de musique Alain-Caron")
                .snippet("75 rue Sainte-Anne, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_educ)));


        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.827407,-69.539261
                        )
                )
                .title("Sport | Centre sportif et piscine")
                .snippet("400 rue Landry, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_sport)));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.828919,-69.537610
                        )
                )
                .title("Education | Centre de formation professionnelle Pavillon de l'Avenir")
                .snippet("65 rue Sainte-Anne, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_educ)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.823394,-69.534230
                        )
                )
                .title("Education | École Internationale Saint-François-Xavier")
                .snippet("8-A rue Pouliot, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_educ)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.832996,-69.540124
                        )
                )
                .title("Education | École Joly")
                .snippet("72 rue Joly, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_educ)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.82787,-69.523618
                        )
                )
                .title("Education | École La Croisée 1")
                .snippet("10 rue Vézina, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_educ)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.827026,-69.522627
                        )
                )
                .title("Education | École La Croisée 2")
                .snippet("15 rue Vézina, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_educ)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.838015,-69.535360
                        )
                )
                .title("Education | École Roy")
                .snippet("55 rue du Rocher, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_educ)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.825870,-69.542080
                        )
                )
                .title("Education | École secondaire de Rivière-du-Loup")
                .snippet("320 rue Saint-Pierre, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_educ)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.822447,-69.535924
                        )
                )
                .title("Education | Collège Notre-Dame")
                .snippet("56 rue Saint-Henri, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_educ)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.827002,-69.539024
                        )
                )
                .title("Education | Cégep de Rivière-du-Loup")
                .snippet("80 rue Frontenac, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_educ)));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.826522,-69.537163
                        )
                )
                .title("Service | Centre Premier Tech")
                .snippet("77 rue Frontenac, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.826058,-69.537804
                        )
                )
                .title("Service | Maisons des jeunes l'Entre-Jeunes")
                .snippet("79 rue Frontenac, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.826112,-69.537730
                        )
                )
                .title("Service | Atelier de menuiserie communautaire")
                .snippet("81-A rue Frontenac, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.830342,-69.532545
                        )
                )
                .title("Service | Club des 50 ans et plus")
                .snippet("15 rue Frontenac, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.828045,-69.537081
                        )
                )
                .title("Service | Pavillon Adrien-Giard")
                .snippet("70 rue Frontenac, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.827183,-69.540106
                        )
                )
                .title("Service | Centre culturel Berger")
                .snippet("85 rue Sainte-Anne, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));


        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.829056,-69.545443
                        )
                )
                .title("Service | Musée du Bas-Saint-Laurent")
                .snippet("300 rue Saint-Pierre, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));



        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.825094,-69.531168
                        )
                )
                .title("Service | Joujouthèque")
                .snippet("31 rue Delage, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.835367,-69.518417
                        )
                )
                .title("Service | Jardin communautaire Saint-Ludger")
                .snippet("Derrière place Saint-Georges sur le chemin des Raymond, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));



        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.835920,-69.549782
                        )
                )
                .title("Service | Jardin communautaire Bellevue")
                .snippet("276 rue Bellevue, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.830624,-69.536220
                        )
                )
                .title("Service | CLSC Rivières et Marées")
                .snippet("22 rue Saint-Laurent, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.818686,-69.540264
                        )
                )
                .title("Santé | Hôpital CHRGP")
                .snippet("75 rue Saint-Henri, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_sante)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.834400,-69.537888
                        )
                )
                .title("Santé | Clinique médicale de Rivière-du-Loup")
                .snippet("242 rue Lafontaine, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_sante)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.829665,-69.534776
                        )
                )
                .title("Santé | Clinique médicale Frontenac")
                .snippet("24 rue Frontenac, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_sante)));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.827161,-69.529182
                        )
                )
                .title("Service | Poste de police de la Sûreté du Québec")
                .snippet("555 rue Lafontaine, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.827365,-69.529268
                        )
                )
                .title("Service | Caserne de pompiers Service de sécurité incendie")
                .snippet("553 rue Lafontaine, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.837217,-69.544563
                        )
                )
                .title("Service | Complexe Jean-Léon-Marquis")
                .snippet("108 rue Fraser, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));


        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.835321,-69.537608
                        )
                )
                .title("Service | Greffe et cour municipale")
                .snippet("75 rue de l'Hôtel-de-Ville, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.837305,-69.536408
                        )
                )
                .title("Service | Service loisirs, culture et vie communautaire")
                .snippet("67 rue du Rocher, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.827346,-69.537120
                        )
                )
                .title("Service | Service loisirs, culture et vie communautaire")
                .snippet("75 rue Frontenac, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.837305,-69.536408
                        )
                )
                .title("Service | Maison de la culture")
                .snippet("67 rue du Rocher, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));




        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.824335,-69.532151
                        )
                )
                .title("Service | Éducation des adultes")
                .snippet("30 rue Delage, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));





        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.859872,-69.550911
                        )
                )
                .title("Service | Camping municipal de la Pointe")
                .snippet("2 Côte-des-Bains, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_municip)));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(
                                47.837305,-69.536408
                        )
                )
                .title("Bibliothèque | Bibliothèque municipale Françoise-Bédard")
                .snippet("67 rue du Rocher, Rivière-du-Loup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_biblio)));


        if(map == null){
            throw new IllegalStateException("Impossible d'initialiser la map");
        }

        map.setOnMarkerClickListener(this);
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item

        }


    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
//        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        //  toggle.onConfigurationChanged(newConfig);
    }
}
