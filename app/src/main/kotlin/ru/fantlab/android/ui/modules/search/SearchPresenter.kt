package ru.fantlab.android.ui.modules.search

import android.support.v4.view.ViewPager
import android.widget.AutoCompleteTextView
import android.widget.EditText
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.AbstractSearchHistory
import ru.fantlab.android.data.dao.model.SearchHistory
import ru.fantlab.android.helper.AppHelper
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import java.util.*

class SearchPresenter : BasePresenter<SearchMvp.View>(), SearchMvp.Presenter {

	private val hints = ArrayList<SearchHistory>()

	override fun onAttachView(view: SearchMvp.View) {
		super.onAttachView(view)
		if (hints.isEmpty()) {
			manageDisposable(AbstractSearchHistory.getHistory()
					.subscribe({ strings ->
						hints.clear()
						if (strings != null) hints.addAll(strings)
						view.onNotifyAdapter(null)
					}))
		}
	}

	override fun getHints(): ArrayList<SearchHistory> = hints

	override fun onSearchClicked(viewPager: ViewPager, editText: AutoCompleteTextView) {
		val isEmpty = InputHelper.isEmpty(editText) || InputHelper.toString(editText as EditText).length < 2
		editText.error = if (isEmpty) editText.resources.getString(R.string.minimum_three_chars) else null
		if (!isEmpty) {
			editText.dismissDropDown()
			AppHelper.hideKeyboard(editText)
			val query = InputHelper.toString(editText)
			/*val repos = viewPager.adapter?.instantiateItem(viewPager, 0) as SearchAuthorsFragment
			val users = viewPager.adapter?.instantiateItem(viewPager, 1) as SearchWorksFragment
			val issues = viewPager.adapter?.instantiateItem(viewPager, 2) as SearchEditionsFragment
			val code = viewPager.adapter?.instantiateItem(viewPager, 3) as SearchAwardsFragment
			repos.onQueueSearch(query)
			users.onQueueSearch(query)
			issues.onQueueSearch(query)
			code.onQueueSearch(query, true)*/
			val noneMatch = hints.none { value -> value.text.equals(query, ignoreCase = true) }
			if (noneMatch) {
				val searchHistory = SearchHistory()
				searchHistory.text = query
				manageObservable(searchHistory.save(searchHistory).toObservable())
				sendToView { view -> view.onNotifyAdapter(searchHistory) }
			}
		}
	}
}