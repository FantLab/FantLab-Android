package ru.fantlab.android.old.edition

import com.arellomobile.mvp.MvpView

interface IEditionView : MvpView {

	fun showEdition(edition: Edition)

	fun showError(message: String)
}
