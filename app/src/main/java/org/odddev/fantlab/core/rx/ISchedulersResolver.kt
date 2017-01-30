package org.odddev.fantlab.core.rx

import rx.Scheduler
import rx.Single

/**
 * @author kenrube
 * *
 * @since 23.08.16
 */

interface ISchedulersResolver {

	fun ioScheduler(): Scheduler

	fun mainThreadScheduler(): Scheduler

	fun <T> applyDefaultSchedulers(): Single.Transformer<T, T>
}
