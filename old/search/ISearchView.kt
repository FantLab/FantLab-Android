package ru.fantlab.android.old.search

import com.arellomobile.mvp.MvpView

interface ISearchView : MvpView {

	fun showResults(results: SearchResult)

	fun showError(message: String)
}
