package ru.fantlab.android.ui.modules.search

import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.viewpager.widget.ViewPager
import io.reactivex.Single
import ru.fantlab.android.R
import ru.fantlab.android.data.db.response.Search
import ru.fantlab.android.helper.AppHelper
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.helper.single
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.modules.search.authors.SearchAuthorsFragment
import ru.fantlab.android.ui.modules.search.awards.SearchAwardsFragment
import ru.fantlab.android.ui.modules.search.editions.SearchEditionsFragment
import ru.fantlab.android.ui.modules.search.works.SearchWorksFragment

class SearchPresenter : BasePresenter<SearchMvp.View>(), SearchMvp.Presenter {

	private val hints = ArrayList<String>()
	private val userId = PrefGetter.getLoggedUser()?.id ?: -1

	override fun onAttachView(view: SearchMvp.View) {
		super.onAttachView(view)
		if (hints.isEmpty()) {
			manageDisposable(
					DbProvider.mainDatabase
							.searchDao()
							.get(userId)
							.single()
							.subscribe { list ->
								hints.clear()
								hints.addAll(list)
								view.onNotifyAdapter(null)
							}
			)
		}
	}

	override fun getHints(): ArrayList<String> = hints

	override fun onSearchClicked(viewPager: ViewPager, editText: AutoCompleteTextView, isIsbn: Boolean) {
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
			editions.onQueueSearch(query, isIsbn)
			awards.onQueueSearch(query)
			if (!isIsbn) {
				val noneMatch = hints.none { it.equals(query, ignoreCase = true) }
				if (noneMatch) {
					manageDisposable(
							Single.fromCallable {
								DbProvider.mainDatabase
										.searchDao()
										.save(Search(query, userId))
							}.single()
									.subscribe()
					)
					sendToView { it.onNotifyAdapter(query) }
				}
			}
		}
	}
}