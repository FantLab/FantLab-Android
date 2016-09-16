package org.odddev.fantlab.core.rx;

import rx.Scheduler;

/**
 * @author kenrube
 * @date 23.08.16
 */

public interface ISchedulersResolver {

    Scheduler ioThread();

    Scheduler mainThread();
}
