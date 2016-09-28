package org.odddev.fantlab.auth;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;

import org.odddev.fantlab.R;
import org.odddev.fantlab.auth.reg.RegFragment;
import org.odddev.fantlab.core.layers.router.Router;
import org.odddev.fantlab.core.utils.FragmentUtils;
import org.odddev.fantlab.home.MainActivity;

/**
 * @author kenrube
 * @date 28.09.16
 */

public class AuthRouter extends Router<AuthActivity> {

    public AuthRouter(AuthActivity activity) {
        super(activity);
    }

    public void routeToReg() {
        @IdRes int containerId = getActivity().getContainerResId();
        FragmentUtils.replaceFragment(getActivity(), containerId, new RegFragment(), true);
    }

    public void routeToHome(boolean loggedIn) {
        MainActivity.start(getActivity(), loggedIn);
        getActivity().overridePendingTransition(0, 0);
    }
}
