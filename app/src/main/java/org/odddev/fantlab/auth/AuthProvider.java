package org.odddev.fantlab.auth;

import android.text.TextUtils;

import org.odddev.fantlab.core.di.Injector;
import org.odddev.fantlab.core.network.IServerApi;
import org.odddev.fantlab.core.rx.ISchedulersResolver;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author kenrube
 * @date 15.09.16
 */

public class AuthProvider implements IAuthProvider {

    private static final String COOKIE_HEADER = "Cookie";

    @Inject
    ISchedulersResolver mSchedulersResolver;

    @Inject
    IServerApi mServerApi;

    public AuthProvider() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    public Observable<Boolean> login(String login, String password) {
        return mServerApi.login(login, password)
                .compose(mSchedulersResolver.applyDefaultSchedulers())
                .map(response -> {
                    String cookie = response.headers().get(COOKIE_HEADER);
                    if (!TextUtils.isEmpty(cookie)) {
                        //TODO save cookie to storage
                    }
                    return !TextUtils.isEmpty(cookie);
                });
    }

    @Override
    public Observable<Boolean> register(String login, String password, String email) {
        return null;
    }
}
