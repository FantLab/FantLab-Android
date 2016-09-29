package org.odddev.fantlab.launch;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import org.odddev.fantlab.R;
import org.odddev.fantlab.auth.AuthActivity;
import org.odddev.fantlab.core.di.Injector;
import org.odddev.fantlab.core.storage.StorageManager;
import org.odddev.fantlab.home.MainActivity;

import javax.inject.Inject;

public class LaunchActivity extends AppCompatActivity {

    @Inject
    StorageManager mStorageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_activity);

        Injector.getAppComponent().inject(this);

        new Handler().postDelayed(() -> {
            String cookie = mStorageManager.loadCookie();
            //TODO проверять еще expire-date (< 1 года)
            if (!TextUtils.isEmpty(cookie)) {
                MainActivity.start(this, true);
            } else {
                AuthActivity.start(this);
            }
            overridePendingTransition(0, 0);
        }, 1000);
    }
}
