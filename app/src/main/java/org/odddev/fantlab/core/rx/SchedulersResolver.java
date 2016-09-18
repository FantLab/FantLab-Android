package org.odddev.fantlab.core.rx;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author kenrube
 * @date 23.08.16
 */

public class SchedulersResolver implements ISchedulersResolver {

    @Override
    public Scheduler ioScheduler() {
        return Schedulers.io();
    }

    @Override
    public Scheduler mainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    public <T> Observable.Transformer<T, T> applyDefaultSchedulers() {
        return observable -> observable
                .subscribeOn(ioScheduler())
                .observeOn(mainThreadScheduler());
    }
}
