package org.odddev.fantlab.core.layers.router;

import android.app.Activity;

/**
 * @author kenrube
 * @since 28.09.16
 */

public class Router<T extends Activity> {

    private T activity;

    public Router(T activity) {
        this.activity = activity;
    }

    protected T getActivity() {
        return activity;
    }
}
