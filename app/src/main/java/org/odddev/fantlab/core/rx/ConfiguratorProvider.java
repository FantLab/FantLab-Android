package org.odddev.fantlab.core.rx;

import org.odddev.fantlab.core.di.Injector;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * @author kenrube
 * @date 06.09.16
 */

public class ConfiguratorProvider {

    @Inject
    ISchedulersResolver mSchedulersResolver;

    public ConfiguratorProvider() {
        Injector.getAppComponent().inject(this);
    }

    public <T> Observable.Transformer<T,T> applySchedulers() {
        return observable -> observable
                .subscribeOn(mSchedulersResolver.ioThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Timber.e(throwable.getLocalizedMessage()));
    }
}
