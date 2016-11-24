package org.odddev.fantlab.core.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author kenrube
 * @since 29.09.16
 */

public class StorageManager {

    private static final String COOKIE_KEY = "COOKIE_KEY";
    private static final String USERNAME_KEY = "USERNAME_KEY";

    private SharedPreferences mSharedPreferences;

    public StorageManager(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveCookie(String cookie) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(COOKIE_KEY, cookie);
        editor.apply();
    }

    public String loadCookie() {
        return mSharedPreferences.getString(COOKIE_KEY, null);
    }

    // todo 6.32
    public void saveUsername(String username) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USERNAME_KEY, username);
        editor.apply();
    }

    public String loadUsername() {
        return mSharedPreferences.getString(USERNAME_KEY, null);
    }
}
