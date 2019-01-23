package com.rajouriya.shubham.exoplayerpoc.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

public class Utility {

    public static String getPref(Context ct, String key) {
        try {
            SharedPreferences shredPref = PreferenceManager.getDefaultSharedPreferences(ct);
            return shredPref.getString(key, "");
        } catch (Exception e) {
            e.printStackTrace();
            return  "";
        }
    }


    public static void savePref(Context ct, String key, String val) {
        SharedPreferences shredPref = PreferenceManager.getDefaultSharedPreferences(ct);
        SharedPreferences.Editor prefEditor = shredPref.edit();
        prefEditor.putString(key, val);
        prefEditor.commit();

    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = connectivityManager.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;
        } else
            return false;
    }

}
