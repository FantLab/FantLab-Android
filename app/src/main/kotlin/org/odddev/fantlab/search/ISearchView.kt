package org.odddev.fantlab.search

import com.arellomobile.mvp.MvpView

interface ISearchView : MvpView {

	fun showResults(results: SearchResult)

	fun showError(message: String)
}
