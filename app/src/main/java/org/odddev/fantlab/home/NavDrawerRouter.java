package org.odddev.fantlab.home;

import android.support.annotation.IntDef;

import org.odddev.fantlab.R;
import org.odddev.fantlab.auth.AuthActivity;
import org.odddev.fantlab.core.layers.router.Router;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author kenrube
 * @date 11.10.16
 */

public class NavDrawerRouter extends Router<MainActivity> {

    @IntDef({R.id.nav_autors, R.id.nav_search, R.id.nav_profile, R.id.nav_logout, R.id.nav_login})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NAV_DRAWER_ITEM{}

    public NavDrawerRouter(MainActivity activity) {
        super(activity);
    }

    public boolean routeToNavDrawerItem(@NAV_DRAWER_ITEM int item) {
        switch (item) {
            case R.id.nav_autors: {
                return true;
            }
            case R.id.nav_search: {
                return true;
            }
            case R.id.nav_profile: {
                return true;
            }
            case R.id.nav_logout:
            case R.id.nav_login: {
                AuthActivity.start(getActivity());
                getActivity().overridePendingTransition(0, 0);
                return true;
            }
        }
        return false;
    }
}
