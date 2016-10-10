package org.odddev.fantlab.home;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.IntentCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.odddev.fantlab.R;
import org.odddev.fantlab.core.di.Injector;
import org.odddev.fantlab.core.storage.StorageManager;
import org.odddev.fantlab.databinding.MainActivityBinding;

import javax.inject.Inject;

public class MainActivity extends FragmentActivity {

    private static final String LOGGED_IN = "LOGGED_IN";

    private MainActivityBinding mBinding;

    private boolean mLoggedIn;

    @Inject
    StorageManager mStorageManager;

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

        Injector.getAppComponent().inject(this);

        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        mLoggedIn = getIntent().getBooleanExtra(LOGGED_IN, false);

        View header = mBinding.navigationView.getHeaderView(0);

        TextView username = (TextView) header.findViewById(R.id.username);

        username.setText(mLoggedIn ? mStorageManager.loadUsername() : getString(R.string.nav_drawer_guest));

        TextView classProgress = (TextView) header.findViewById(R.id.class_progress);

        if (mLoggedIn) {
            classProgress.setText(getString(R.string.nav_drawer_class_progress));
        }
        classProgress.setVisibility(mLoggedIn ? View.VISIBLE : View.GONE);

        mBinding.navigationView.getMenu().clear();
        mBinding.navigationView.inflateMenu(mLoggedIn
                ? R.menu.nav_drawer_user
                : R.menu.nav_drawer_guest);
    }
}
