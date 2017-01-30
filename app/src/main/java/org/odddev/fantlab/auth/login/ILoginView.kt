package org.odddev.fantlab.auth.login

import com.arellomobile.mvp.MvpView

/**
 * @author kenrube
 * *
 * @since 30.08.16
 */

interface ILoginView : MvpView {

	fun showLoginResult(loggedIn: Boolean)

	fun showError(error: String)

	fun showFieldsInvalid()
}
