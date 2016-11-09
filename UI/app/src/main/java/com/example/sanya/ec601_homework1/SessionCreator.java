package com.example.sanya.ec601_homework1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.facebook.LoginActivity;
import com.facebook.Session;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;

/**
 * Created by sanya on 25/3/15.
 */
public class SessionCreator extends Activity{
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    LoginActivity log;
    // an editor is used to edit your preferences
    Context context;
    GoogleApiClient mGoggleApiClient;
    // Shared Preference file name
    private static final String PREF_NAME = "Pref";

    // Shared Preferences Key
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";

    public SessionCreator(Context context) {
        this.context = context;
        /*
         * Setting the mode as Private so that the preferences should only be
         * used in this application and not by any other application
         * also the preferences can be Shared Globally by using -
         *Activity.MODE_WORLD_READABLE - to read Application components data
         *globally and,
         *Activity.MODE_WORLD_WRITEABLE -file can be written globally by any
         *other application.
         */

        log=new LoginActivity();
        pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        /*
         * the same pref mode can be set to private by using 0 as a flag instead
         * of Acticity.MODE_PRIVATE
         */

        editor = pref.edit();
    }
    // Creating a login session
    public void createLoginSession(String name, String email) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        return user;
    }
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect to Login Activity
            Intent i = new Intent(context, LoginActivity.class);
////            // Closing all the Activities
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }
    }

    // Clearing a session data
    public void logoutUser() {
        editor.clear();
        editor.commit();
        Session session = Session.getActiveSession();
        if (session != null) {

            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
                //clear your preferences if saved
            }
        } else {

            session = new Session(context);
            Session.setActiveSession(session);
            session.closeAndClearTokenInformation();
        }

//        Toast.makeText(context, "You have successfully logged out", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(context, HomeActivity.class);
////            // Closing all the Activities
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        context.startActivity(i);
        finish();

    }
    public void logoutUser2() {
        editor.clear();
        editor.commit();
        Session session = Session.getActiveSession();
        if (session != null) {

            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
                //clear your preferences if saved
            }
        } else {

            session = new Session(context);
            Session.setActiveSession(session);
            session.closeAndClearTokenInformation();
        }
    }
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}

