package ru.fantlab.android.helper

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

fun <T> Observable<T>.observe(): Observable<T> =
		this
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnError { Timber.e(it) }

fun <T> Single<T>.single(): Single<T> =
		this
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnError { Timber.e(it) }
