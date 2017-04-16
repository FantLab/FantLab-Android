package org.odddev.fantlab.autors

import com.arellomobile.mvp.MvpView

/**
 * @author kenrube
 * *
 * @since 11.12.16
 */

interface IAutorsView : MvpView {

	fun showAutors(autors: List<Autor>)

	fun showError(message: String)
}
