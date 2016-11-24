package org.odddev.fantlab.core.utils;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * @author kenrube
 * @since 30.08.16
 */

public class FragmentUtils {

    private static final String RETAIN_FRAGMENT = "retain";

    public static void replaceFragment(FragmentActivity activity, @IdRes int containerId,
                                       Fragment fragment, boolean backStack) {
        try {
            FragmentTransaction fragmentTransaction = getFragmentManager(activity).beginTransaction();
            fragmentTransaction.replace(containerId, fragment, RETAIN_FRAGMENT);
            if (backStack) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private static FragmentManager getFragmentManager(FragmentActivity activity) {
        return activity.getSupportFragmentManager();
    }
}
