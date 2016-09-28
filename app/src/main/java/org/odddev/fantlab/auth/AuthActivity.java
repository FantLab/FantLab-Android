package org.odddev.fantlab.auth;

import org.odddev.fantlab.R;
import org.odddev.fantlab.auth.login.LoginFragment;
import org.odddev.fantlab.core.utils.FragmentUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.IntentCompat;

/**
 * @author kenrube
 * @date 23.08.16
 */

public class AuthActivity extends FragmentActivity {

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
        setContentView(R.layout.auth_activity);

        routeToLogin();
    }

    private void routeToLogin() {
        FragmentUtils.initFragment(this, getContainerResId(), new LoginFragment(), false);
    }

    @IdRes
    public int getContainerResId() {
        return R.id.container;
    }
}
