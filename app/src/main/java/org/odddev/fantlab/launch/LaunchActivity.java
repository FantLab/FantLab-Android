package org.odddev.fantlab.launch;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.odddev.fantlab.R;
import org.odddev.fantlab.auth.AuthActivity;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_activity);

        new Handler().postDelayed(() -> {
            AuthActivity.start(this);
            overridePendingTransition(0, 0);
        }, 1000);
    }
}
