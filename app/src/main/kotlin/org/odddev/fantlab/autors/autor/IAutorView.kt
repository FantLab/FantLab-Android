package org.odddev.fantlab.autors.autor

import com.arellomobile.mvp.MvpView

interface IAutorView : MvpView {

	fun showAutor(autor: AutorFull)

	fun showError(message: String)
}
