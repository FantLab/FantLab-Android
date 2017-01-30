package org.odddev.fantlab.core.rx;

import android.support.annotation.NonNull;

import rx.Scheduler;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author kenrube
 * @since 23.08.16
 */

public class SchedulersResolver implements ISchedulersResolver {

	@NonNull
	@Override
	public Scheduler ioScheduler() {
		return Schedulers.io();
	}

	@NonNull
	@Override
	public Scheduler mainThreadScheduler() {
		return AndroidSchedulers.mainThread();
	}

	@NonNull
	public <T> Single.Transformer<T, T> applyDefaultSchedulers() {
		return single -> single
				.subscribeOn(ioScheduler())
				.observeOn(mainThreadScheduler());
	}
}
