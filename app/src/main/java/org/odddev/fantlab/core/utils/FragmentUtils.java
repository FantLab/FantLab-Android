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

    public static void replaceFragment(FragmentActivity activity, @IdRes int containerId,
                                       Fragment fragment, boolean backStack) {
        try {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(containerId, fragment, fragment.getClass().getSimpleName());
            if (backStack) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
