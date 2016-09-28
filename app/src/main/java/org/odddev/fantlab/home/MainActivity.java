package org.odddev.fantlab.home;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.IntentCompat;
import android.os.Bundle;

import org.odddev.fantlab.R;
import org.odddev.fantlab.databinding.MainActivityBinding;

public class MainActivity extends FragmentActivity {

    private static final String LOGGED_IN = "LOGGED_IN";

    private MainActivityBinding mBinding;
    private boolean mLoggedIn;

    public static void start(Context context, boolean loggedIn) {
        start(context, Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK,
                loggedIn);
    }

    public static void start(Context context, int flags, boolean loggedIn) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(intent.getFlags() | flags);
        intent.putExtra(LOGGED_IN, loggedIn);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        mLoggedIn = getIntent().getBooleanExtra(LOGGED_IN, false);

        mBinding.navigationView.getMenu().clear();
        mBinding.navigationView.inflateMenu(mLoggedIn
                ? R.menu.nav_drawer_user
                : R.menu.nav_drawer_guest);
    }
}
