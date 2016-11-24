package org.odddev.fantlab.core.layers.router;

import android.app.Activity;

/**
 * @author kenrube
 * @since 28.09.16
 */

public class Router<T extends Activity> {

    private T mActivity;

    public Router(T activity) {
        mActivity = activity;
    }

    protected T getActivity() {
        return mActivity;
    }
}
