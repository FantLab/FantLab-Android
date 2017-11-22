package org.odddev.fantlab.authors

import com.arellomobile.mvp.MvpView

interface IAuthorsView : MvpView {

	fun showAuthors(authors: List<Author>, scrollToTop: Boolean)

	fun showError(message: String)
}
