package org.odddev.fantlab.core.view;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Developer: Ivan Zolotarev
 * Date: 30.08.16
 */

public class BaseFragment extends Fragment {

    protected AppCompatActivity getAppCompatActivity() {
        Activity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            return (AppCompatActivity) activity;
        }
        return null;
    }

    protected ActionBar getActionBar() {
        AppCompatActivity appCompatActivity = getAppCompatActivity();
        if (appCompatActivity != null) {
            return appCompatActivity.getSupportActionBar();
        }
        return null;
    }

    protected void setActionBarTitle(CharSequence title) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void setActionBarTitle(@StringRes int title) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }
}
