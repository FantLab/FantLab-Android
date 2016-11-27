package org.odddev.fantlab.home;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.IntentCompat;

import org.odddev.fantlab.R;
import org.odddev.fantlab.core.di.Injector;
import org.odddev.fantlab.core.storage.StorageManager;
import org.odddev.fantlab.databinding.MainActivityBinding;
import org.odddev.fantlab.databinding.NavDrawerHeaderBinding;

import javax.inject.Inject;

public class MainActivity extends FragmentActivity {

    private static final String EXTRA_LOGGED_IN_KEY = "LOGGED_IN";

    private MainActivityBinding binding;
    private NavDrawerRouter router;

    @Inject
    StorageManager storageManager;

    public static void start(Context context, boolean loggedIn) {
        start(context, Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK,
                loggedIn);
    }

    public static void start(Context context, int flags, boolean loggedIn) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(intent.getFlags() | flags);
        intent.putExtra(EXTRA_LOGGED_IN_KEY, loggedIn);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.getAppComponent().inject(this);

        boolean loggedIn = getIntent().getBooleanExtra(EXTRA_LOGGED_IN_KEY, false);

        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        router = new NavDrawerRouter(this);

        NavDrawerHeaderBinding headerBinding = NavDrawerHeaderBinding.inflate(getLayoutInflater(),
                binding.navigationView, false);

        binding.navigationView.addHeaderView(headerBinding.getRoot());

        binding.navigationView.getMenu().clear();
        binding.navigationView.inflateMenu(loggedIn
                ? R.menu.nav_drawer_user
                : R.menu.nav_drawer_guest);

        binding.navigationView.setNavigationItemSelectedListener(item -> {
            @NavDrawerRouter.NAV_DRAWER_ITEM int itemId = item.getItemId();

            binding.navigationView.setCheckedItem(itemId);
            return router.routeToNavDrawerItem(itemId);
        });
    }
}
