package org.odddev.fantlab.edition

import com.arellomobile.mvp.MvpView

interface IEditionView : MvpView {

	fun showEdition(edition: Edition)

	fun showError(message: String)
}
