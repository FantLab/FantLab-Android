package org.odddev.fantlab.home;

import android.support.annotation.IdRes;
import android.support.annotation.IntDef;

import org.odddev.fantlab.R;
import org.odddev.fantlab.auth.AuthActivity;
import org.odddev.fantlab.award.AwardsFragment;
import org.odddev.fantlab.core.layers.router.Router;
import org.odddev.fantlab.core.utils.FragmentUtils;

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

    boolean routeToNavDrawerItem(@NAV_DRAWER_ITEM int item, HomeOptionSelectListener listener) {
        switch (item) {
            case R.id.nav_autors: {
                return true;
            }
            case R.id.nav_awards: {
                FragmentUtils.replaceFragment(getActivity(), getContainerId(),
                        new AwardsFragment(listener), true);
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
