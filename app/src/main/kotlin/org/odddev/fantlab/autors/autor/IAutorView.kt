package org.odddev.fantlab.autors.autor

import com.arellomobile.mvp.MvpView

/**
 * @author kenrube
 * *
 * @since 11.12.16
 */

interface IAutorView : MvpView {

	fun showAutor(autor: AutorFull)

	fun showError(message: String)
}
