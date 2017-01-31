package org.odddev.fantlab.core.rx

import rx.Observable
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by kefir on 31.01.2017.
 */

fun <T> Observable<T>.applyDefaultSchedulers() : Observable<T> {
	return subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.applyDefaultSchedulers() : Single<T> {
	return subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
}