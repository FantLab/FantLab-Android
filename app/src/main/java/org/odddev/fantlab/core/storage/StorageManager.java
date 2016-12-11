package org.odddev.fantlab.core.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author kenrube
 * @since 29.09.16
 */

public class StorageManager {

    private static final String COOKIE_KEY = "COOKIE";
    private static final String USERNAME_KEY = "USERNAME";

    private SharedPreferences sharedPreferences;

    public StorageManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveCookie(String cookie) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COOKIE_KEY, cookie);
        editor.apply();
    }

    public String loadCookie() {
        return sharedPreferences.getString(COOKIE_KEY, null);
    }

    public void clearCookie() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(COOKIE_KEY);
        editor.apply();
    }

    // todo 6.32
    public void saveUsername(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME_KEY, username);
        editor.apply();
    }

    public String loadUsername() {
        return sharedPreferences.getString(USERNAME_KEY, null);
    }
}
