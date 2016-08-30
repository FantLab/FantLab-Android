package org.odddev.fantlab.core.view;

import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Developer: Ivan Zolotarev
 * Date: 30.08.16
 */

public class BaseAcitivity extends AppCompatActivity {

    protected void initToolbar(@IdRes int toolbarId, boolean homeAsUp) {
        Toolbar toolbar = (Toolbar) findViewById(toolbarId);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(homeAsUp);
        }
    }
}
