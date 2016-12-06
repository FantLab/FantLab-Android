package org.odddev.fantlab.core.rx;

import rx.Scheduler;
import rx.Single;

/**
 * @author kenrube
 * @since 23.08.16
 */

public interface ISchedulersResolver {

    Scheduler ioScheduler();

    Scheduler mainThreadScheduler();

    <T> Single.Transformer<T, T> applyDefaultSchedulers();
}
