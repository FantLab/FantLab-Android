package ru.fantlab.android.ui.base

import android.content.Intent
import android.support.annotation.LayoutRes
import android.widget.Toast
import com.evernote.android.state.State
import es.dmoral.toasty.Toasty
import net.grandcentrix.thirtyinch.TiActivity
import ru.fantlab.android.App
import ru.fantlab.android.R
import ru.fantlab.android.helper.AppHelper
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.modules.main.MainActivity
import ru.fantlab.android.ui.widgets.dialog.ProgressDialogFragment

/**
 * Created by Kosh on 24 May 2016, 8:48 PM
 */
abstract class BaseActivity<V : BaseMvp.View, P : BasePresenter<V>> : TiActivity<P, V>(), BaseMvp.View {

	@State
	var isProgressShowing: Boolean = false

	private var toast: Toast? = null

	@LayoutRes
	protected abstract fun layout(): Int

	protected abstract fun isTransparent(): Boolean

	protected abstract fun canBack(): Boolean

	protected abstract fun isSecured(): Boolean

	override fun showProgress(resId: Int, cancelable: Boolean) {
		var msg = getString(R.string.in_progress)
		if (resId != 0) {
			msg = getString(resId)
		}
		if (!isProgressShowing && !isFinishing) {
			var fragment = AppHelper.getFragmentByTag(supportFragmentManager, ProgressDialogFragment.TAG)
							as ProgressDialogFragment?
			if (fragment == null) {
				isProgressShowing = true
				fragment = ProgressDialogFragment.newInstance(msg, cancelable)
				fragment.show(supportFragmentManager, ProgressDialogFragment.TAG)
			}
		}
	}

	override fun hideProgress() {
		val fragment = AppHelper.getFragmentByTag(supportFragmentManager, ProgressDialogFragment.TAG)
				as ProgressDialogFragment?
		fragment?.let {
			isProgressShowing = false
			it.dismiss()
		}
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		showMessage(getString(titleRes), getString(msgRes))
	}

	override fun showMessage(titleRes: String, msgRes: String) {
		hideProgress()
		toast?.cancel()
		val context = App.instance
		toast = if (titleRes == context.getString(R.string.error))
			Toasty.error(context, msgRes, Toast.LENGTH_LONG)
		else
			Toasty.info(context, msgRes, Toast.LENGTH_LONG)
		toast?.show()
	}

	override fun showErrorMessage(msgRes: String) {
		showMessage(getString(R.string.error), msgRes)
	}

	override fun isLoggedIn(): Boolean {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun onRequireLogin() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun onLogoutPressed() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun onThemeChanged() {
		if (this is MainActivity) {
			recreate()
		} else {
			val intent = Intent(this, MainActivity::class.java)
			with(intent) {
				putExtras(Bundler.start().put(BundleConstant.YES_NO_EXTRA, true).end())
				addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
			}
			startActivity(intent)
			finish()
		}
	}

	override fun onOpenSettings() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun onOpenUrlInBrowser() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun onScrollTop(index: Int) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}