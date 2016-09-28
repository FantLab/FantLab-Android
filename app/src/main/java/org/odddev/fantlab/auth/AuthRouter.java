package org.odddev.fantlab.auth;

import android.support.v4.app.FragmentActivity;

import org.odddev.fantlab.R;
import org.odddev.fantlab.auth.reg.RegFragment;
import org.odddev.fantlab.core.layers.router.Router;
import org.odddev.fantlab.core.utils.FragmentUtils;

/**
 * @author kenrube
 * @date 28.09.16
 */

public class AuthRouter extends Router<FragmentActivity> {

    public AuthRouter(FragmentActivity activity) {
        super(activity);
    }

    public void routeToReg() {
        FragmentUtils.replaceFragment(getActivity(), R.id.container, new RegFragment(), true);
    }

    public void routeToHome(boolean loggedIn) {
        //TODO open MainActivity with key "loggedIn"
    }
}
