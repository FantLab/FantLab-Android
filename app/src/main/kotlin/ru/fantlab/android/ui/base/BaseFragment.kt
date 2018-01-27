package ru.fantlab.android.ui.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v7.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.evernote.android.state.StateSaver
import net.grandcentrix.thirtyinch.TiFragment
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

/**
 * Created by Kosh on 27 May 2016, 7:54 PM
 */

abstract class BaseFragment<V : BaseMvp.View, P : BasePresenter<V>> : TiFragment<P, V>(), BaseMvp.View {

	protected var callback: BaseMvp.View? = null

	private var unbinder: Unbinder? = null

	@LayoutRes
	protected abstract fun fragmentLayout(): Int

	protected abstract fun onFragmentCreated(view: View, savedInstanceState: Bundle?)

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		if (context is BaseMvp.View) {
			callback = context
		}
	}

	override fun onDetach() {
		super.onDetach()
		callback = null
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		StateSaver.saveInstanceState(this, outState)
		presenter.onSaveInstanceState(outState)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (savedInstanceState != null && !savedInstanceState.isEmpty) {
			StateSaver.restoreInstanceState(this, savedInstanceState)
			presenter.onRestoreInstanceState(savedInstanceState)
		}
	}

	@SuppressLint("RestrictedApi")
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		if (fragmentLayout() != 0) {
			val contextThemeWrapper = ContextThemeWrapper(context, context?.theme)
			val themeAwareInflater = inflater.cloneInContext(contextThemeWrapper)
			val view = themeAwareInflater.inflate(fragmentLayout(), container, false)
			unbinder = ButterKnife.bind(this, view)
			return view
		}
		return super.onCreateView(inflater, container, savedInstanceState)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		//if (AbstractLogin.getUser() != null) {
			onFragmentCreated(view, savedInstanceState)
		//}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		unbinder?.unbind()
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		callback?.showProgress(resId, cancelable)
	}

	override fun hideProgress() {
		callback?.hideProgress()
	}

	override fun showMessage(@StringRes titleRes: Int, @StringRes msgRes: Int) {
		callback?.showMessage(titleRes, msgRes)
	}

	override fun showMessage(titleRes: String, msgRes: String) {
		callback?.showMessage(titleRes, msgRes)
	}

	override fun showErrorMessage(msgRes: String) {
		callback?.showErrorMessage(msgRes)
	}

	override fun isLoggedIn(): Boolean {
		return callback != null && callback!!.isLoggedIn()
	}

	override fun onRequireLogin() {
		callback?.onRequireLogin()
	}

	override fun onMessageDialogActionClicked(isOk: Boolean, bundle: Bundle?) {}

	override fun onDialogDismissed() {}

	override fun onLogoutPressed() {
		callback?.onLogoutPressed()
	}

	override fun onThemeChanged() {
		callback?.onThemeChanged()
	}

	override fun onOpenSettings() {
		callback?.onOpenSettings()
	}

	override fun onScrollTop(index: Int) {}

	override fun onOpenUrlInBrowser() {
		callback?.onOpenUrlInBrowser()
	}

	protected fun isSafe(): Boolean {
		val activity = activity
		return view != null && activity != null && !activity.isFinishing
	}
}
