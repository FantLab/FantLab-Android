package org.odddev.fantlab.launch;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.IntentCompat;
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
            Intent intent = new Intent(LaunchActivity.this, AuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }, 1000);
    }
}
