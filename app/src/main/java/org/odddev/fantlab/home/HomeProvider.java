package org.odddev.fantlab.home;

import org.odddev.fantlab.core.di.Injector;
import org.odddev.fantlab.core.rx.ISchedulersResolver;
import org.odddev.fantlab.core.storage.StorageManager;

import javax.inject.Inject;

import rx.Single;

/**
 * @author kenrube
 * @since 11.12.16
 */

public class HomeProvider implements IHomeProvider {

    @Inject
    StorageManager storageManager;

    @Inject
    ISchedulersResolver schedulersResolver;

    public HomeProvider() {
        Injector.getAppComponent().inject(this);
    }

    @Override
    public Single<String> getUserName() {
        return Single.just(storageManager.loadUsername())
                .compose(schedulersResolver.applyDefaultSchedulers());
    }

    @Override
    public void clearCookie() {
        storageManager.clearCookie();
    }
}
