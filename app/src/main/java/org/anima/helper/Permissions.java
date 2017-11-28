package org.anima.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.io.File;


/**
 * Created by salysakey on 11/28/17.
 */

public class Permissions {
    //private static final int REQUEST_WRITE_EXTERNAL_STORAGE=19;

    /**
     * @author salysakey
     */
    public static void checkStoragePermission(final Context context, final int REQUEST_WRITE_EXTERNAL_STORAGE){
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //RUNTIME PERMISSION Android M
            if(PackageManager.PERMISSION_GRANTED==ActivityCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "");
            }else{
                requestStoragePermission(context, REQUEST_WRITE_EXTERNAL_STORAGE);
            }

        }// end if
    }// end function

    /**
     * @author salysakey
     * @param context
     */
    public static void requestStoragePermission(final Context context, final int REQUEST_WRITE_EXTERNAL_STORAGE){
        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.

            new AlertDialog.Builder(context)
                    .setMessage("Please allow access to storage")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    }).show();

        }// end if
    }// end function

    public static void storagePermissionResult(int requestCode, String[] permissions, int[] grantResults,Context context,
                                                 final int REQUEST_WRITE_EXTERNAL_STORAGE){
        // check storage permission (salysakey)
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context,"Storage Permission is granted.",
                        Toast.LENGTH_SHORT).show();

            }
            return;
        }// end if request code
    }

}// end class
