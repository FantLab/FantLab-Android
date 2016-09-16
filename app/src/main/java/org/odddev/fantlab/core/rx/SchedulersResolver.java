package org.odddev.fantlab.core.rx;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author kenrube
 * @date 23.08.16
 */

public class SchedulersResolver implements ISchedulersResolver {

    @Override
    public Scheduler ioThread() {
        return Schedulers.io();
    }

    @Override
    public Scheduler mainThread() {
        return AndroidSchedulers.mainThread();
    }
}
