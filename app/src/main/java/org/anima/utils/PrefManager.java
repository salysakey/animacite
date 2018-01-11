package org.anima.utils;

import android.content.Context;
import android.preference.PreferenceManager;


/**
 * Created by BeugFallou on 27/07/15.
 */
public class PrefManager {

    public static final String TELLING_STATE_KEY = "TELLING_STATE";
    public static final String RATING_STATUS_KEY = "RATING_STATUS";
    public static final String COUNTER_KEY = "COUNTER_KEY";
    public static final String TUTORIAL_STATUS_KEY = "TUTORIAL_STATUS";

    public static void setFirstStart(Context context, boolean status) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean("firststrat", status)
                .apply();
    }

    public static Boolean getFirstStart(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean("firststrat", true);
    }

    public static void setFirstSignalement(Context context, boolean status) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean("firstSignalement", status)
                .apply();
    }

    public static Boolean getFirstSignalement(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean("firstSignalement", false);
    }


    public static void setRatingStatus(Context context, int status) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(RATING_STATUS_KEY, status)
                .apply();
    }

    public static int getRatingStatus(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getInt(RATING_STATUS_KEY, 0);
    }

    public static void setLatitude(Context context, String latitude) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("latitude", latitude)
                .apply();
    }

    public static String getLatitude(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("latitude", "");
    }

    public static void setImageUrl(Context context, String imageUrl) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("imageUrl", imageUrl)
                .apply();
    }

    public static String getImageUrl(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("imageUrl", "");
    }

    public static void setImageUrlSignal(Context context, String imageUrl) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("imageUrlSignal", imageUrl)
                .apply();
    }

    public static String getImageUrlSignal(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("imageUrlSignal", "");
    }

    public static void setLongitude(Context context, String longitude) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("longitude", longitude)
                .apply();
    }

    public static String getLongitude(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("longitude", "");
    }

    public static void setCounter(Context context, int count) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(COUNTER_KEY, count)
                .apply();
    }

    public static int getCounter(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getInt(COUNTER_KEY, 0);
    }


    public static void setUserName(Context context, String name) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("name", name)
                .apply();
    }
    public static void setRegistrationId(Context context, String registrationId) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("registrationId", registrationId)
                .apply();
    }
    public static String getRegistrationId(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("registrationId", "");
    }

    public static String getUserName(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("name", "");
    }

    public static void setUserProfession(Context context, String prof) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("prof",prof)
                .apply();
    }

    public static String getUserProfession(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("prof", "");
    }

    public static void setUserMail(Context context, String mail) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("mail",mail)
                .apply();
    }

    public static String getUserMail(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("mail", "");
    }

    public static void setUserNaiss(Context context, long naissance) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong("naissance", naissance)
                .apply();
    }

    public static long getUserNaiss(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getLong("naissance", 0);
    }

    public static void setUserAdresse(Context context, String add) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("add", add)
                .apply();
    }

    public static long getUserId(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getLong("userId", 0);
    }

    public static void setUserId(Context context, long userId) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong("userId",userId)
                .apply();
    }

    public static String getUserAdresse(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("add", "");
    }

    public static void setUserSexe(Context context, String sexe) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("sexe",sexe)
                .apply();
    }

    public static String getUserSexe(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("sexe", "");
    }
}
