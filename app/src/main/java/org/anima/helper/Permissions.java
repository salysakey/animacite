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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import org.anima.animacite.R;

import java.io.File;


/**
 * Created by salysakey on 11/28/17.
 */

public class Permissions {
    //private static final int REQUEST_WRITE_EXTERNAL_STORAGE=19;

    /**
     * @author salysakey
     */
    public static boolean checkStoragePermission(final Context context, final int REQUEST_WRITE_EXTERNAL_STORAGE){
        File storageDir = null;
        boolean result = false;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //RUNTIME PERMISSION Android M
            if(PackageManager.PERMISSION_GRANTED==ActivityCompat.checkSelfPermission((Activity)context,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "");
                result = true;
            }else{
                requestStoragePermission(context, REQUEST_WRITE_EXTERNAL_STORAGE);
            }

        }// end if
        return result;
    }// end function

    /**
     * @author salysakey
     * @param context
     */
    public static void requestStoragePermission(final Context context, final int REQUEST_WRITE_EXTERNAL_STORAGE){
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Veuillez autoriser l'autorisation de stockage");
            // Setting Dialog Message
            // Setting Positive "Yes" Button
            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    });
            // Showing Alert Message
            AlertDialog alert = alertDialog.create();
            alert.show();
        }// end if
    }// end function

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param context
     * @param REQUEST_WRITE_EXTERNAL_STORAGE
     */
    public static void storagePermissionResult(int requestCode, String[] permissions, int[] grantResults,Context context,
                                                 final int REQUEST_WRITE_EXTERNAL_STORAGE){
        // check storage permission (salysakey)
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {}
            return;
        }// end if request code
    }// end function

    public static void informLocationDisabled(final Context context){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // Setting Dialog Title
        alertDialog.setTitle("Veuillez activer votre localisation");
        alertDialog.setIcon(R.drawable.petite_image);
        alertDialog.setNegativeButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Activity activity = (Activity) context;
                        activity.finish();
                    }
                });
        // Showing Alert Message
        AlertDialog alert = alertDialog.create();
        alert.show();
    }
    /**
     *
     * @param context
     * @param LOCATION_PERMISSION
     * @param location
     * @param longitude
     * @param latitude
     * @param MIN_TIME_BW_UPDATES
     * @param MIN_DISTANCE_CHANGE_FOR_UPDATES
     */
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
          return null;
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
            }// end if
        }// end if
        return location;
    }// end function

    public static boolean checkLocationPermission(final Context context, final int MY_PERMISSIONS_SIGNALEMENT){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED) {
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
                            ActivityCompat.requestPermissions((Activity)context,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_SIGNALEMENT);
                        }
                    });
            // Showing Alert Message
            AlertDialog alert = alertDialog.create();
            alert.show();
            return false;
        }else{
            return true;
        }

    }// end function

    public static boolean checkCameraPermission(final Context context, final int PERMISSION_CONSTANT){
        boolean result = true;
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                !=PackageManager.PERMISSION_GRANTED){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Veuillez activer votre camera");
            alertDialog.setIcon(R.drawable.petite_image);
            alertDialog.setNegativeButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context,
                                    new String[]{Manifest.permission.CAMERA},
                                    PERMISSION_CONSTANT);
                        }
                    });
            AlertDialog alert = alertDialog.create();
            alert.show();
            result = false;
        }
        return result;
    }// end function


}// end class
