package org.odddev.fantlab.auth

import com.arellomobile.mvp.MvpView

interface IAuthView : MvpView {

	fun showAuthResult(loggedIn: Boolean)

	fun showError(error: String)

	fun showFieldsInvalid()
}