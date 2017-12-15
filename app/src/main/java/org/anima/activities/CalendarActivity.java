package org.anima.activities;
import org.anima.helper.Navigation;
import org.anima.utils.ConfigurationVille;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.imanoweb.calendarview.CalendarListener;
import com.imanoweb.calendarview.CustomCalendarView;
import com.imanoweb.calendarview.DayDecorator;
import com.imanoweb.calendarview.DayView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.anima.adapters.AdapterCalendar;
import org.anima.entities.Food;
import org.anima.entities.Project;
import org.anima.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by momo on 16/03/2016.
 */
public class CalendarActivity extends AppCompatActivity{
    String[] titles;
    String[] descriptifs;
    TextView txtEmpty;
    int [] idents;
    String[] listPictures;
    String[] resumes;
    int[] types;
    public long[] date;
    private AdapterCalendar mAdapter;
    ProgressDialog prgDialog;
    private ListView listView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private Calendar currentCalendar;
    final ArrayList<DayDecorator> decorators = new ArrayList<>();

    private Menu menudeux;
    private CustomCalendarView calendarView;
    private String url;
    private  int n =56;
    ArrayList<Food> listFood;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendrier);
        initToolbar();
        setupDrawerLayout();
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        listFood = new ArrayList<Food>();
        listView = (ListView)findViewById(R.id.list);
        mAdapter = new AdapterCalendar(this);
        RequestParams params = new RequestParams();
        // Put Http parameter username with value of Email Edit View control
        params.put("userId", ""+PrefManager.getUserId(getApplicationContext()));
        invokeWS(params);
        //Initialize CustomCalendarView from layout
    }

    public void invokeWS(RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ConfigurationVille.Debut_WS+"/evenement/doshow", params, new AsyncHttpResponseHandler() {
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
                            project.setDate_debut(object.getLong("date_debut"));

                            date[i] = project.getDate_debut();
                            Log.d("Date:",""+date[i]);
                            titles[i] = project.getTitle();
                            descriptifs[i] = project.getDescription();
                            resumes[i] = project.getResume();
                            types[i] = project.getType();
                            //	listPictures[i]="http://i.imgur.com/DvpvklR.png";
                            listPictures[i] = project.getPicture();
                            idents[i] = object.getInt("id");
                            //	Picasso.with(ImagePickActivity.this).load(R.drawable.piscine).placeholder(listPictures[i]);
                            listFood.add(new Food(i + 1, titles[i], listPictures[i], resumes[i],
                                    descriptifs[i], types[i], idents[i], date[i]));
                            // Navigate to Home screen
                        }
                        prgDialog.hide();
                        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);

                        //Initialize calendar with date
                        currentCalendar = Calendar.getInstance(Locale.getDefault());

                        //Show Monday as first date of week
                        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
                        //Show/hide overflow days of a month
                        calendarView.setShowOverflowDate(false);

                        // Use API from here: http://stacktips.com/tutorials/android/custom-calendar-view-library-in-android
                        decorators.add(new ColorDecorator(date));
                        calendarView.setDecorators(decorators);

                        //call refreshCalendar to update calendar the view
                        calendarView.refreshCalendar(currentCalendar);

                        //Handling custom calendar events
                        calendarView.setCalendarListener(new CalendarListener() {

                            @Override
                            public void onDateSelected(Date calendarDate) {
                                //listView.removeAllViews();


                                mAdapter.delete();
                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    calendarDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(df.format(calendarDate));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                //long milliseconds = date.getTime();
                                // Toast.makeText(CalendarActivity.this, ""+milliseconds, Toast.LENGTH_SHORT).show();
                                String strCalDate = calendarDate.getDate()+"/"+calendarDate.getMonth()+"/"+calendarDate.getYear();

                                for(int i=0;i<listFood.size();i++){

                                    Long timestamp = listFood.get(i).getDate();
                                    Date eventDate = new Date(timestamp);
                                    String strEventDate = eventDate.getDate()+"/"+eventDate.getMonth()+"/"+eventDate.getYear();
                                    if(strCalDate.equals(strEventDate)){
                                        mAdapter.addItem(listFood.get(i).getName(),listFood.get(i).getPictureUrl(),listFood.get(i).getPrice());
                                    }
                                    listView.setAdapter(mAdapter);
                                }
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, final View view,
                                                            int position, long id) {
                                            //Toast.makeText(KiosqueActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                                            //download
                                            showPickActivity(position);
                                    /*      AlertDialog.Builder alertDialog = new AlertDialog.Builder(CalendarActivity.this);
                                            // Setting Dialog Title
                                            alertDialog.setTitle(listFood.get(position).getName());
                                            ImageView image = new ImageView(getApplicationContext());

                                            Picasso.with(getBaseContext()).load(listFood.get(position).getPictureUrl()).into(image);
                                            Drawable drawImage = image.getDrawable();
                                        drawImage.setBounds(0,0,10,10);
                                        //drawImage.setHotspot(10,10);
                                            alertDialog.setIcon(drawImage);
                                            // Setting Dialog Message
                                            alertDialog.setMessage(listFood.get(position).getDescriptif());
                                            // Setting Positive "Yes" Button
                                            alertDialog.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            //Move to Next screen
                                                            dialog.cancel();
                                                        }
                                                    });
                                            // Setting Negative "NO" Button

                                            // Showing Alert Message
                                            AlertDialog alert = alertDialog.create();
                                            alert.show(); */

                                        }

                                });

                            }

                            @Override
                            public void onMonthChanged(Date date) {
                                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                                //Toast.makeText(CalendarActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
                            }
                        });

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

    public void showPickActivity(int index) {
        Log.d("index", ""+index);
        Intent intent = new Intent( CalendarActivity.this , EmptyActivity.class);
        Food selectedFood = listFood.get(index);

        Bundle b = new Bundle();
        b.putString("description", selectedFood.getName());
        b.putString("titre", selectedFood.getDescriptif());
        b.putInt("ident", selectedFood.getIdent());
        b.putInt("position", index);
        b.putString("pictureUrl", selectedFood.getPictureUrl());
        b.putInt("tipe", selectedFood.getType());
        b.putBoolean("affichercalendar",false);
        b.putLong("date", selectedFood.getDate());

        intent.putExtras(b);

        startActivity(intent);

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Mes évènements");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    /**
     * Refactored by Saly Sakey November-07-2017
     */
    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        new Navigation(CalendarActivity.this, drawerLayout, view);
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

    private class ColorDecorator implements DayDecorator {

        public long[] dates3;


        public ColorDecorator(long[] dates2){
          //  longueur=dates2.length;
            dates3 = new long[dates2.length];

            this.dates3=dates2;


        }


        @Override
        public void decorate(DayView cell) {

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

            Date cell2 =cell.getDate();

            try {
                cell2 = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(df.format(cell2));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            for(int i=0;i<dates3.length;i++) {
                long timestamp = dates3[i];
                Date date = new Date(timestamp);
                String strEventDate = date.getDate()+"/"+date.getMonth()+"/"+date.getYear();
                String strCalDate = cell2.getDate()+"/"+cell2.getMonth()+"/"+cell2.getYear();

                if (strCalDate.equals(strEventDate)){
                    cell.setBackgroundColor(getResources().getColor(R.color.orange));
                }// end if

            }// end for
        }// end function decorate
    }// end class
}
