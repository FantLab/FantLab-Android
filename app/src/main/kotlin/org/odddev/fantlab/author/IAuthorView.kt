package org.odddev.fantlab.author

import com.arellomobile.mvp.MvpView

interface IAuthorView : MvpView {

	fun showAuthor(author: AuthorFull)

	fun showError(message: String)
}
