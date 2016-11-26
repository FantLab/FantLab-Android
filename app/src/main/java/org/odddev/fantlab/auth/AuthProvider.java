package org.odddev.fantlab.auth;

import android.text.TextUtils;

import org.odddev.fantlab.core.di.Injector;
import org.odddev.fantlab.core.network.IServerApi;
import org.odddev.fantlab.core.rx.ISchedulersResolver;
import org.odddev.fantlab.core.storage.StorageManager;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author kenrube
 * @since 15.09.16
 */

public class AuthProvider implements IAuthProvider {

    private static final String COOKIE_HEADER = "Set-Cookie";

    @Inject
    ISchedulersResolver schedulersResolver;

    @Inject
    IServerApi serverApi;

    @Inject
    StorageManager storageManager;

    public AuthProvider() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    public Observable<Boolean> login(String username, String password) {
        return serverApi.login(username, password)
                .compose(schedulersResolver.applyDefaultSchedulers())
                .map(response -> {
                    String cookie = response.headers().get(COOKIE_HEADER);
                    if (!TextUtils.isEmpty(cookie)) {
                        storageManager.saveCookie(cookie);
                    }
                    // todo 6.32
                    storageManager.saveUsername(username);
                    return !TextUtils.isEmpty(cookie);
                });
    }

    @Override
    public Observable<Boolean> register(String username, String password, String email) {
        return null;
    }
}
