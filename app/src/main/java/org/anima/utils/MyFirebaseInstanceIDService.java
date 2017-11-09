package org.anima.utils;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.util.Log;
import android.widget.Toast;

/**
 * Created by Jo on 19/02/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    public void onTokenRefresh(){
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, " - ID User " + PrefManager.getUserId(getApplicationContext()));

        // Enregistrement du token pour l'envoyer après, lorsque la personne à créé un compte
        PrefManager.setRegistrationId(getApplicationContext(), refreshedToken);

        // Envoie du token pour l'envoie de notifications même au gens qui ne sont pas connecté
        RequestParams param = new RequestParams();
        param.put("userId", "" + PrefManager.getUserId(getApplicationContext()));
        param.put("IdAndroid", refreshedToken);
        invokeWSRegistration(param);
    }

    public void invokeWSRegistration(RequestParams params) {
        // Show Progress Dialog

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(ConfigurationVille.Debut_WS + "/register/doregid", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Une erreur s'est produite, vous ne recevrez pas de notifications pour le moment.", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Une erreur s'est produite, vous ne recevrez pas de notifications pour le moment.", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Une erreur s'est produite lors de l'enrengistrement de votre appareil pour les notifications.", Toast.LENGTH_LONG).show();
                }
            }

        });
    }



}
