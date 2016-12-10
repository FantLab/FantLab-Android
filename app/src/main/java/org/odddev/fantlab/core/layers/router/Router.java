package org.odddev.fantlab.core.layers.router;

import android.app.Activity;
import android.support.annotation.IdRes;

/**
 * @author kenrube
 * @since 28.09.16
 */

public class Router<T extends Activity> {

    private T activity;
    private @IdRes int containerId;

    public Router(T activity, @IdRes int containerId) {
        this.activity = activity;
        this.containerId = containerId;
    }

    protected T getActivity() {
        return activity;
    }

    public int getContainerId() {
        return containerId;
    }
}
