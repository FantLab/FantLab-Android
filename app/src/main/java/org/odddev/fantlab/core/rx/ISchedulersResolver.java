package org.odddev.fantlab.core.rx;

import rx.Observable;
import rx.Scheduler;

/**
 * @author kenrube
 * @date 23.08.16
 */

public interface ISchedulersResolver {

    Scheduler ioScheduler();

    Scheduler mainThreadScheduler();

    <T> Observable.Transformer<T, T> applyDefaultSchedulers();
}
