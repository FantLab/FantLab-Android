package ru.fantlab.android.ui.base.mvp

import android.os.Bundle
import androidx.annotation.StringRes
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import net.grandcentrix.thirtyinch.TiView
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread
import ru.fantlab.android.ui.widgets.dialog.MessageDialogView

/**
 * Created by Kosh on 25 May 2016, 9:09 PM
 */
interface BaseMvp {

	interface View : TiView, MessageDialogView.MessageDialogViewActionCallback, OnScrollTopListener {

		@CallOnMainThread
		fun showProgress(@StringRes resId: Int, cancelable: Boolean = true)

		@CallOnMainThread
		fun hideProgress()

		@CallOnMainThread
		fun showMessage(@StringRes titleRes: Int, @StringRes msgRes: Int)

		@CallOnMainThread
		fun showMessage(titleRes: String, msgRes: String?)

		@CallOnMainThread
		fun showErrorMessage(msgRes: String?)

		fun isLoggedIn(): Boolean

		fun onRequireLogin()

		fun onLogoutPressed()

		fun onThemeChanged()

		fun onOpenSettings()

		fun onOpenUrlInBrowser()
	}

	interface Presenter {

		fun onSaveInstanceState(outState: Bundle)

		fun onRestoreInstanceState(outState: Bundle?)

		fun manageDisposable(vararg disposables: Disposable)

		fun <T> manageObservable(observable: Observable<T>?)

		fun manageViewDisposable(vararg disposables: Disposable)

		fun isApiCalled(): Boolean

		fun onSubscribed(cancelable: Boolean)

		fun onError(throwable: Throwable)

		fun <T> makeRestCall(observable: Observable<T>, onNext: Consumer<T>, cancelable: Boolean = true)
	}

	interface PaginationListener<in P> {

		fun getCurrentPage(): Int

		fun getPreviousTotal(): Int

		fun setCurrentPage(page: Int)

		fun setPreviousTotal(previousTotal: Int)

		fun onCallApi(page: Int, parameter: P?): Boolean
	}

	interface OnScrollTopListener {

		fun onScrollTop(index: Int)
	}
}