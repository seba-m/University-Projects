package com.seba.inventariado.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.seba.inventariado.activity.LoginActivity;
import com.seba.inventariado.activity.MainActivity;
import com.seba.inventariado.model.Users;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "SessionManager";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // Email address 
    public static final String KEY_EMAIL = "Email";

    // Email address 
    public static final String KEY_USERID = "UserID";

    // First Name 
    public static final String KEY_FIRSTNAME = "FirstName";

    // Last Name address 
    public static final String KEY_LASTNAME = "LastName";

    // Last Name address
    public static final String KEY_IMAGE = "Image";

    // Token address 
    public static final String KEY_TOKEN = "Token";

    // Constructor
    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(Users users) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_FIRSTNAME, users.getFirstName());
        editor.putString(KEY_USERID, users.getUserID());
        editor.putString(KEY_LASTNAME, users.getLastName());
        editor.putString(KEY_TOKEN, users.getToken());
        editor.putString(KEY_EMAIL, users.getEmail());
        editor.putString(KEY_IMAGE, users.getPhoto());
        editor.commit();
    }

    public void updatePhoto(String url) {
        editor.putString(KEY_IMAGE, url);
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Get stored session data
     */
    public Users getUserDetails() {
        Users users = new Users();
        users.setEmail(pref.getString(KEY_EMAIL, null));
        users.setUserID(pref.getString(KEY_USERID, null));
        users.setFirstName(pref.getString(KEY_FIRSTNAME, null));
        users.setLastName(pref.getString(KEY_LASTNAME, null));
        users.setPhoto(pref.getString(KEY_IMAGE, null));
        users.setToken(pref.getString(KEY_TOKEN, null));

        // return user
        return users;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public Boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
