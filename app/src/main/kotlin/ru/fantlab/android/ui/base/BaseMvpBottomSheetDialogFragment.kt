package ru.fantlab.android.ui.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.DialogFragment
import com.evernote.android.state.StateSaver
import com.google.android.material.bottomsheet.BottomSheetDialog
import net.grandcentrix.thirtyinch.TiDialogFragment
import ru.fantlab.android.R
import ru.fantlab.android.helper.AppHelper
import ru.fantlab.android.helper.ViewHelper
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

/**
 * Created by Kosh on 27 May 2017, 1:51 PM
 */

abstract class BaseMvpBottomSheetDialogFragment<V : BaseMvp.View, P : BasePresenter<V>> : TiDialogFragment<P, V>(), BaseMvp.View {

	protected var callback: BaseMvp.View? = null

	@LayoutRes
	protected abstract fun fragmentLayout(): Int

	override fun onAttach(context: Context) {
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
		setStyle(DialogFragment.STYLE_NO_TITLE, if (AppHelper.isNightMode(resources)) R.style.DialogThemeDark else R.style.DialogThemeLight)
		if (savedInstanceState != null && !savedInstanceState.isEmpty) {
			StateSaver.restoreInstanceState(this, savedInstanceState)
			presenter.onRestoreInstanceState(savedInstanceState)
		}
	}

	@SuppressLint("RestrictedApi")
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		if (fragmentLayout() != 0) {
			val contextThemeWrapper = ContextThemeWrapper(context, context!!.theme)
			val themeAwareInflater = inflater.cloneInContext(contextThemeWrapper)
			return themeAwareInflater.inflate(fragmentLayout(), container, false)
		}
		return super.onCreateView(inflater, container, savedInstanceState)
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

	override fun showMessage(titleRes: String, msgRes: String?) {
		callback?.showMessage(titleRes, msgRes)
	}

	override fun showErrorMessage(msgRes: String?) {
		callback?.showErrorMessage(msgRes)
	}

	override fun onMessageDialogActionClicked(isOk: Boolean, bundle: Bundle?) {
	}

	override fun onDialogDismissed() {
	}

	override fun isLoggedIn(): Boolean {
		return callback != null && callback!!.isLoggedIn()
	}

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

	override fun onScrollTop(index: Int) {
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val dialog = BottomSheetDialog(context!!, theme)
		dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		dialog.setOnShowListener {
			if (ViewHelper.isTablet(activity)) {
				dialog.window?.let {
					with(it) {
						setLayout(
								ViewGroup.LayoutParams.WRAP_CONTENT,
								ViewGroup.LayoutParams.MATCH_PARENT)
					}
				}
			}
		}
		return dialog
	}

	override fun onOpenUrlInBrowser(url: String) {
		callback?.onOpenUrlInBrowser(url)
	}
}
