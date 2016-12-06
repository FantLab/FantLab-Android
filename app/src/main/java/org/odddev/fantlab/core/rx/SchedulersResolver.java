package org.odddev.fantlab.core.rx;

import rx.Scheduler;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author kenrube
 * @since 23.08.16
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

    public <T> Single.Transformer<T, T> applyDefaultSchedulers() {
        return single -> single
                .subscribeOn(ioScheduler())
                .observeOn(mainThreadScheduler());
    }
}
