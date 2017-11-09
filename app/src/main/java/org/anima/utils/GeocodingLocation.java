package org.anima.utils;

/**
 * Created by momo on 03/05/16.
 */
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocodingLocation {

    private static final String TAG = "GeocodingLocation";

    public static void getAddressFromLocation(final String locationAddress, final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                Double latitude = 0.0;
                Double longitude = 0.0;
                String result ="";
                try {
                    List
                            addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = (Address) addressList.get(0);
                      /*  StringBuilder lat = new StringBuilder();
                        StringBuilder longi = new StringBuilder();
                        lat.append(address.getLatitude()).append("\n");
                        longi.append(address.getLongitude()).append("\n");
                        result = sb.toString();
                        */
                        latitude=address.getLatitude();
                        longitude=address.getLongitude();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable to connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (latitude !=0 && longitude !=0) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                      //  result = "Address: " + locationAddress + "\n\nLatitude and Longitude :\n" + result;
                        bundle.putDouble("latitude", latitude);
                        bundle.putDouble("longitude", longitude);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                       result = "Address: " + locationAddress + "\n L'adresse n'est pas correcte";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}
