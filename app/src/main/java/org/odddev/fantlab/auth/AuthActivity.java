package org.odddev.fantlab.auth;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.IntentCompat;

import org.odddev.fantlab.R;

/**
 * @author kenrube
 * @since 23.08.16
 */

public class AuthActivity extends FragmentActivity {

    private AuthRouter router;

    public static void start(Context context) {
        start(context, Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
    }

    public static void start(Context context, int flags) {
        Intent intent = new Intent(context, AuthActivity.class);
        intent.setFlags(intent.getFlags() | flags);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.auth_activity);

        router = new AuthRouter(this, R.id.container);
        router.routeToLogin();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
