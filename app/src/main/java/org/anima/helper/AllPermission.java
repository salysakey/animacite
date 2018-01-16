package org.anima.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.anima.animacite.R;

import java.io.File;

/**
 * Created by salysakey on 1/16/18.
 */

public class AllPermission {
    public static boolean checkPermission(final Context context, final int ACTIVITY_PERMISSION){
        boolean result = false;
        File storageDir = null;
        boolean storage_allow = false;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //RUNTIME PERMISSION Android M
            if(
                    PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission((Activity)context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission((Activity)context,
                    Manifest.permission.CAMERA)
                    && PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission((Activity)context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    && PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission((Activity)context,
                    Manifest.permission.ACCESS_COARSE_LOCATION))
            {
                storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "");
                result = true;

            }else{
                ActivityCompat.requestPermissions((Activity)context,
                        new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        ACTIVITY_PERMISSION);
            }

        }// end if

        return result;
    }

    public static boolean permissionResult(int requestCode, String[] permissions, int[] grantResults,Context context,
                                 final int ACTIVITY_PERMISSION){
        boolean result = false;
        if (requestCode == ACTIVITY_PERMISSION){
            if (grantResults.length >0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
               result = true;
            }
        }// end if request code
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Location getLocation(final Context context, int LOCATION_PERMISSION, Location location,
                                       double longitude, double latitude, long MIN_TIME_BW_UPDATES,
                                       long MIN_DISTANCE_CHANGE_FOR_UPDATES){
        LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationChangeListener = new LocationListener() {
            public void onLocationChanged(Location l) {
                if (l != null) {
                    Log.i("SuperMap", "Location changed : Lat: " + l.getLatitude() + " Lng: " +
                            l.getLongitude());
                }
            }

            public void onProviderEnabled(String p) {}
            public void onProviderDisabled(String p) {}
            public void onStatusChanged(String p, int status, Bundle extras) {}
        };

        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false
                && locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == false) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            // Setting Dialog Title
            alertDialog.setTitle("Veuillez activer votre localisation");
            alertDialog.setIcon(R.drawable.petite_image);
            alertDialog.setNegativeButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancel Dialog

                            //Intent intent4 = new Intent(context, SignalementActivity.class);
                            //startActivity(intent4);
                            dialog.cancel();
                        }
                    });
            // Showing Alert Message
            AlertDialog alert = alertDialog.create();
            alert.show();

        } else {
            if ((ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED )
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity)context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION);
                //return;
            }else{

                locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationChangeListener);
                location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (location == null) {
                    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationChangeListener);
                    location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

                if (location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    // Setting Dialog Title
                    alertDialog.setTitle("Veuillez activer votre localisation");
                    alertDialog.setIcon(R.drawable.petite_image);
                    // Setting Dialog Message
                    // Setting Positive "Yes" Button
                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Cancel Dialog
                                    dialog.cancel();
                                }
                            });
                    // Showing Alert Message
                    AlertDialog alert = alertDialog.create();
                    alert.show();
                }// end if
            }// end if
        }// end if
        return location;
    }// end function
}
