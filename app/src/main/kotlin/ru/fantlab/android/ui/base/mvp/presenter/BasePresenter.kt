package ru.fantlab.android.ui.base.mvp.presenter

import android.os.Bundle
import com.evernote.android.state.StateSaver
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.grandcentrix.thirtyinch.TiPresenter
import net.grandcentrix.thirtyinch.rx2.RxTiPresenterDisposableHandler
import ru.fantlab.android.R
import ru.fantlab.android.helper.observe
import ru.fantlab.android.ui.base.mvp.BaseMvp

/**
 * Created by Kosh on 25 May 2016, 9:12 PM
 */
open class BasePresenter<V : BaseMvp.View> : TiPresenter<V>(), BaseMvp.Presenter {

	private var apiCalled: Boolean = false
	private val subscriptionHandler = RxTiPresenterDisposableHandler(this)

	override fun onSaveInstanceState(outState: Bundle) {
		StateSaver.saveInstanceState(this, outState)
	}

	override fun onRestoreInstanceState(outState: Bundle?) {
		outState?.let { StateSaver.restoreInstanceState(this, it) }
	}

	override fun manageDisposable(vararg disposables: Disposable) {
		subscriptionHandler.manageDisposables(*disposables)
	}

	override fun <T> manageObservable(observable: Observable<T>?) {
		observable?.let { manageDisposable(it.observe().subscribe(
				{ /**/ },
				{ throwable -> throwable.printStackTrace() }
		)) }
	}

	override fun manageViewDisposable(vararg disposables: Disposable) {
		if (isViewAttached) {
			subscriptionHandler.manageViewDisposables(*disposables)
		} else {
			sendToView { manageViewDisposable(*disposables) }
		}
	}

	override fun isApiCalled(): Boolean = apiCalled

	override fun onSubscribed(cancelable: Boolean) {
		sendToView { v -> v.showProgress(R.string.in_progress, cancelable) }
	}

	override fun onError(throwable: Throwable) {
		TODO("not implemented")
	}

	override fun <T> makeRestCall(observable: Observable<T>, onNext: Consumer<T>, cancelable: Boolean) {
		manageDisposable(
				observable
						.observe()
						.doOnSubscribe { onSubscribed(cancelable) }
						.subscribe(
								onNext,
								Consumer { throwable -> onError(throwable) },
								Action { apiCalled = true }
						)
		)
	}
}