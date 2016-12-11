package org.odddev.fantlab.home;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.GravityCompat;
import android.text.TextUtils;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import org.odddev.fantlab.R;
import org.odddev.fantlab.databinding.HomeActivityBinding;
import org.odddev.fantlab.databinding.NavDrawerHeaderBinding;

public class HomeActivity extends MvpAppCompatActivity implements IHomeView {

    private static final String SELECTED_NAV_DRAWER_ITEM_ID_KEY = "SELECTED_NAV_DRAWER_ITEM_ID";
    private static final String EXTRA_LOGGED_IN_KEY = "LOGGED_IN";

    private HomeActivityBinding binding;
    private NavDrawerHeaderBinding headerBinding;
    private NavDrawerRouter router;

    private @NavDrawerRouter.NAV_DRAWER_ITEM int selectedNavDrawerItemId;

    @InjectPresenter
    HomePresenter presenter;

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

        binding = DataBindingUtil.setContentView(this, R.layout.home_activity);

        router = new NavDrawerRouter(this, R.id.container);

        int selectedItem = R.id.nav_awards;
        if (savedInstanceState != null) {
            selectedItem = savedInstanceState.getInt(SELECTED_NAV_DRAWER_ITEM_ID_KEY);
        }
        initNavigationDrawer(selectedItem);

        presenter.getUserName();
    }

    private void initNavigationDrawer(int selectedNavDrawerItemId) {
        headerBinding = NavDrawerHeaderBinding.inflate(getLayoutInflater(),
                binding.navigationView, false);

        binding.navigationView.addHeaderView(headerBinding.getRoot());

        binding.navigationView.getMenu().clear();

        boolean loggedIn = getIntent().getBooleanExtra(EXTRA_LOGGED_IN_KEY, false);

        binding.navigationView.inflateMenu(loggedIn
                ? R.menu.nav_drawer_user
                : R.menu.nav_drawer_guest);

        binding.navigationView.setNavigationItemSelectedListener(navigationListener);
        binding.navigationView.getMenu().performIdentifierAction(selectedNavDrawerItemId, 0);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
            if (backStackEntryCount == 1) {
                finish();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_NAV_DRAWER_ITEM_ID_KEY, selectedNavDrawerItemId);
    }

    @Override
    public void showUserName(String userName) {
        if (!TextUtils.isEmpty(userName)) {
            headerBinding.username.setText(userName);
            headerBinding.classProgress.setText(R.string.nav_drawer_user_class_progress);
        }
    }

    NavigationView.OnNavigationItemSelectedListener navigationListener = item -> {
        @NavDrawerRouter.NAV_DRAWER_ITEM int navDrawerItemId = item.getItemId();

        selectedNavDrawerItemId = navDrawerItemId;

        binding.navigationView.setCheckedItem(navDrawerItemId);
        binding.drawerLayout.closeDrawer(GravityCompat.START);

        if (navDrawerItemId == R.id.nav_logout) presenter.clearCookie();

        return router.routeToNavDrawerItem(navDrawerItemId);
    };
}
