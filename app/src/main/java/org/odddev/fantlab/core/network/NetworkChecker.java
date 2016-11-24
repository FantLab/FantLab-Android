package org.odddev.fantlab.core.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.odddev.fantlab.core.di.Injector;

import javax.inject.Inject;

/**
 * @author kenrube
 * @since 23.08.16
 */

public class NetworkChecker implements INetworkChecker {

    @Inject
    Context mContext;

    public NetworkChecker() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isConnected();
    }
}
