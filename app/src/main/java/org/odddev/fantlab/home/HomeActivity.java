package org.odddev.fantlab.home;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;

import org.odddev.fantlab.R;
import org.odddev.fantlab.databinding.HomeActivityBinding;
import org.odddev.fantlab.databinding.NavDrawerHeaderBinding;

public class HomeActivity extends AppCompatActivity implements HomeOptionSelectListener {

    private static final String EXTRA_LOGGED_IN_KEY = "LOGGED_IN";
    private static final String NAVIGATION_DRAWER_ITEM_ID_KEY = "NAVIGATION_DRAWER_ITEM_ID";

    private HomeActivityBinding binding;
    private NavDrawerRouter router;
    private @NavDrawerRouter.NAV_DRAWER_ITEM int navDrawerItemId = R.id.nav_awards;

    public static void start(Context context, boolean loggedIn) {
        start(context, Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK,
                loggedIn);
    }

    public static void start(Context context, int flags, boolean loggedIn) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(intent.getFlags() | flags);
        intent.putExtra(EXTRA_LOGGED_IN_KEY, loggedIn);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean loggedIn = getIntent().getBooleanExtra(EXTRA_LOGGED_IN_KEY, false);

        binding = DataBindingUtil.setContentView(this, R.layout.home_activity);

        router = new NavDrawerRouter(this, R.id.container);

        NavDrawerHeaderBinding headerBinding = NavDrawerHeaderBinding.inflate(getLayoutInflater(),
                binding.navigationView, false);

        binding.navigationView.addHeaderView(headerBinding.getRoot());

        binding.navigationView.getMenu().clear();
        binding.navigationView.inflateMenu(loggedIn
                ? R.menu.nav_drawer_user
                : R.menu.nav_drawer_guest);

        binding.navigationView.setNavigationItemSelectedListener(item -> {
            navDrawerItemId = item.getItemId();

            binding.navigationView.setCheckedItem(navDrawerItemId);
            return router.routeToNavDrawerItem(navDrawerItemId, this);
        });

        if (savedInstanceState != null) {
            navDrawerItemId = savedInstanceState.getInt(NAVIGATION_DRAWER_ITEM_ID_KEY);
        }

        router.routeToNavDrawerItem(navDrawerItemId, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAVIGATION_DRAWER_ITEM_ID_KEY, navDrawerItemId);
    }

    @Override
    public void onHomeOptionSelected() {
        binding.drawerLayout.openDrawer(GravityCompat.START);
    }
}
