package org.odddev.fantlab.auth;

import android.support.annotation.IdRes;

import org.odddev.fantlab.auth.login.LoginFragment;
import org.odddev.fantlab.auth.reg.RegFragment;
import org.odddev.fantlab.core.layers.router.Router;
import org.odddev.fantlab.core.utils.FragmentUtils;
import org.odddev.fantlab.home.HomeActivity;

/**
 * @author kenrube
 * @since 28.09.16
 */

public class AuthRouter extends Router<AuthActivity> {

    public AuthRouter(AuthActivity activity, @IdRes int containerId) {
        super(activity, containerId);
    }

    public void routeToLogin() {
        FragmentUtils.replaceFragment(getActivity(), getContainerId(), new LoginFragment(), true);
    }

    public void routeToReg() {
        FragmentUtils.replaceFragment(getActivity(), getContainerId(), new RegFragment(), true);
    }

    public void routeToHome(boolean loggedIn) {
        HomeActivity.start(getActivity(), loggedIn);
        getActivity().overridePendingTransition(0, 0);
    }
}
