package org.odddev.fantlab.auth;

import org.odddev.fantlab.core.di.Injector;
import org.odddev.fantlab.core.network.IServerApi;
import org.odddev.fantlab.core.rx.ISchedulersResolver;

import javax.inject.Inject;

import rx.Observable;
import timber.log.Timber;

/**
 * @author kenrube
 * @date 15.09.16
 */

public class AuthProvider implements IAuthProvider {

    @Inject
    ISchedulersResolver mSchedulersResolver;

    @Inject
    IServerApi mServerApi;

    public AuthProvider() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    public Observable<Void> login(String login, String password) {
        return mServerApi.login(login, password)
                .compose(mSchedulersResolver.applyDefaultSchedulers())
                .map(response -> {
                    Timber.e(response.toString());
                    return null;
                });
    }

    @Override
    public Observable<Void> register(String login, String password, String email) {
        return null;
    }
}
