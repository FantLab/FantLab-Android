package ru.fantlab.android.ui.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.app.DialogFragment
import android.support.v7.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.evernote.android.state.StateSaver
import net.grandcentrix.thirtyinch.TiDialogFragment
import ru.fantlab.android.R
import ru.fantlab.android.helper.AnimHelper
import ru.fantlab.android.helper.AppHelper
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.widgets.dialog.ProgressDialogFragment

abstract class BaseDialogFragment<V : BaseMvp.View, P : BasePresenter<V>>
	: TiDialogFragment<P, V>(), BaseMvp.View {

	protected var callback: BaseMvp.View? = null
	protected var suppressAnimation = false
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
		if (AppHelper.isNightMode(resources)) {
			setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogThemeDark)
		} else {
			setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogThemeLight)
		}
		if (savedInstanceState != null && !savedInstanceState.isEmpty) {
			StateSaver.restoreInstanceState(this, savedInstanceState)
			presenter.onRestoreInstanceState(savedInstanceState)
		}
	}

	override fun dismiss() {
		if (suppressAnimation) {
			super.dismiss()
			return
		}
		if (PrefGetter.isAppAnimationDisabled()) {
			super.dismiss()
		} else {
			AnimHelper.dismissDialog(
					this,
					resources.getInteger(android.R.integer.config_shortAnimTime),
					object : AnimatorListenerAdapter() {
						override fun onAnimationEnd(animation: Animator) {
							super.onAnimationEnd(animation)
							super@BaseDialogFragment.dismiss()
						}
					}
			)
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

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val dialog = super.onCreateDialog(savedInstanceState)
		if (!PrefGetter.isAppAnimationDisabled() && this !is ProgressDialogFragment && !suppressAnimation) {
			dialog.setOnShowListener({
				AnimHelper.revealDialog(dialog, resources.getInteger(android.R.integer.config_longAnimTime))
			})
		}
		return dialog
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		onFragmentCreated(view, savedInstanceState)
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

	override fun isLoggedIn(): Boolean = callback?.isLoggedIn() ?: false

	override fun onRequireLogin() {
		callback?.onRequireLogin()
	}

	override fun onLogoutPressed() {
		callback?.onLogoutPressed()
	}

	override fun onThemeChanged() {
		callback?.onThemeChanged()
	}

	override fun onOpenSettings() {
		callback?.onOpenSettings()
	}

	override fun onOpenUrlInBrowser() {
		callback?.onOpenUrlInBrowser()
	}

	override fun onScrollTop(index: Int) {
	}
}