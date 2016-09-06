package org.odddev.fantlab.core.rx;

import rx.Scheduler;

/**
 * Developer: Ivan Zolotarev
 * Date: 23.08.16
 */

public interface ISchedulersResolver {

    Scheduler ioThread();

    Scheduler mainThread();
}
