package org.odddev.fantlab.auth.reg

import com.arellomobile.mvp.MvpView

/**
 * @author kenrube
 * *
 * @since 18.09.16
 */

interface IRegView : MvpView {

	fun showRegResult(registered: Boolean)

	fun showError(error: String)
}
