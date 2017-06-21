package org.odddev.fantlab.autors

import com.arellomobile.mvp.MvpView

interface IAutorsView : MvpView {

	fun showAutors(autors: List<Autor>)

	fun showError(message: String)
}
