package org.odddev.fantlab.home;

import android.support.annotation.IdRes;
import android.support.annotation.IntDef;

import org.odddev.fantlab.R;
import org.odddev.fantlab.auth.AuthActivity;
import org.odddev.fantlab.award.AwardsFragment;
import org.odddev.fantlab.catalog.CatalogFragment;
import org.odddev.fantlab.core.layers.router.Router;
import org.odddev.fantlab.core.utils.FragmentUtils;
import org.odddev.fantlab.profile.ProfileFragment;
import org.odddev.fantlab.search.SearchFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author kenrube
 * @since 11.10.16
 */

class NavDrawerRouter extends Router<HomeActivity> {

    @IntDef({
            R.id.nav_autors,
            R.id.nav_awards,
            R.id.nav_search,
            R.id.nav_profile,
            R.id.nav_logout,
            R.id.nav_login
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface NAV_DRAWER_ITEM{}

    NavDrawerRouter(HomeActivity activity, @IdRes int containerId) {
        super(activity, containerId);
    }

    boolean routeToNavDrawerItem(@NAV_DRAWER_ITEM int item) {
        switch (item) {
            case R.id.nav_autors: {
                FragmentUtils.replaceFragment(getActivity(), getContainerId(), new CatalogFragment(), false);
                return true;
            }
            case R.id.nav_awards: {
                FragmentUtils.replaceFragment(getActivity(), getContainerId(), new AwardsFragment(), false);
                return true;
            }
            case R.id.nav_search: {
                FragmentUtils.replaceFragment(getActivity(), getContainerId(), new SearchFragment(), false);
                return true;
            }
            case R.id.nav_profile: {
                FragmentUtils.replaceFragment(getActivity(), getContainerId(), new ProfileFragment(), false);
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
