package ru.fantlab.android.helper

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.observe() : Observable<T> =
		this
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnError({ it.printStackTrace() })

fun <T> Single<T>.observe() : Single<T> =
		this
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnError({ it.printStackTrace() })
