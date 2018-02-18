package ru.fantlab.android.ui.modules.search

import android.support.v4.view.ViewPager
import android.widget.AutoCompleteTextView
import android.widget.EditText
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.SearchHistory
import ru.fantlab.android.data.dao.model.getSearchHistory
import ru.fantlab.android.data.dao.model.save
import ru.fantlab.android.helper.AppHelper
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.modules.search.authors.SearchAuthorsFragment
import ru.fantlab.android.ui.modules.search.awards.SearchAwardsFragment
import ru.fantlab.android.ui.modules.search.editions.SearchEditionsFragment
import ru.fantlab.android.ui.modules.search.works.SearchWorksFragment

class SearchPresenter : BasePresenter<SearchMvp.View>(), SearchMvp.Presenter {

	private val hints = ArrayList<String>()

	override fun onAttachView(view: SearchMvp.View) {
		super.onAttachView(view)
		if (hints.isEmpty()) {
			manageDisposable(getSearchHistory()
					.subscribe { histories ->
						hints.clear()
						val strings = ArrayList<String>()
						histories.mapTo(strings) { it.text ?: "" }
						hints.addAll(strings)
						view.onNotifyAdapter(null)
					}
			)
		}
	}

	override fun getHints(): ArrayList<String> = hints

	override fun onSearchClicked(viewPager: ViewPager, editText: AutoCompleteTextView) {
		val isEmpty = InputHelper.isEmpty(editText) || InputHelper.toString(editText as EditText).length < 2
		editText.error = if (isEmpty) editText.resources.getString(R.string.minimum_three_chars) else null
		if (!isEmpty) {
			editText.dismissDropDown()
			AppHelper.hideKeyboard(editText)
			val query = InputHelper.toString(editText)
			val authors = viewPager.adapter?.instantiateItem(viewPager, 0) as SearchAuthorsFragment
			val works = viewPager.adapter?.instantiateItem(viewPager, 1) as SearchWorksFragment
			val editions = viewPager.adapter?.instantiateItem(viewPager, 2) as SearchEditionsFragment
			val awards = viewPager.adapter?.instantiateItem(viewPager, 3) as SearchAwardsFragment
			authors.onQueueSearch(query)
			works.onQueueSearch(query)
			editions.onQueueSearch(query)
			awards.onQueueSearch(query)
			val noneMatch = hints.none { it.equals(query, ignoreCase = true) }
			if (noneMatch) {
				val searchHistory = SearchHistory()
				searchHistory.text = query
				manageObservable(searchHistory.save().toObservable())
				sendToView { it.onNotifyAdapter(query) }
			}
		}
	}
}