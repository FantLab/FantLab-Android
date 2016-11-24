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
 * @author kenrube
 * @since 23.08.16
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
    Context getContext() {
        return mContext;
    }

    @Singleton
    @Provides
    ISchedulersResolver provideSchedulersResolver() {
        return new SchedulersResolver();
    }

    @Singleton
    @Provides
    INetworkChecker provideNetworkChecker() {
        return new NetworkChecker();
    }
}
