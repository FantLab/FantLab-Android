package org.odddev.fantlab.core.di;

import android.content.Context;
import android.support.annotation.NonNull;

import org.odddev.fantlab.core.network.INetworkChecker;
import org.odddev.fantlab.core.network.NetworkChecker;
import org.odddev.fantlab.core.rx.ISchedulersResolver;
import org.odddev.fantlab.core.rx.SchedulersResolver;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Developer: Ivan Zolotarev
 * Date: 23.08.16
 */

@Module
public class AppModule {

    private final Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    @Singleton
    @Provides
    @NonNull
    public Context getContext() {
        return mContext;
    }

    @Provides
    public ISchedulersResolver provideSchedulersResolver() {
        return new SchedulersResolver();
    }

    @Singleton
    @Provides
    public INetworkChecker provideNetworkChecker() {
        return new NetworkChecker();
    }
}
