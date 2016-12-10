package org.odddev.fantlab.launch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import org.odddev.fantlab.auth.AuthActivity;
import org.odddev.fantlab.core.di.Injector;
import org.odddev.fantlab.core.storage.StorageManager;
import org.odddev.fantlab.home.HomeActivity;

import javax.inject.Inject;

public class LaunchActivity extends AppCompatActivity {

    @Inject
    StorageManager storageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.getAppComponent().inject(this);

        String cookie = storageManager.loadCookie();
        // todo 8.35, 8.36
        if (!TextUtils.isEmpty(cookie)) {
            HomeActivity.start(this, true);
        } else {
            AuthActivity.start(this);
        }
    }
}
