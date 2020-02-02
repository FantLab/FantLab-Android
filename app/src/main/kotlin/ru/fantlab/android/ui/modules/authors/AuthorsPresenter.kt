package ru.fantlab.android.ui.modules.authors

import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.AuthorInList
import ru.fantlab.android.data.dao.response.AuthorsResponse
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getAuthorsPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class AuthorsPresenter : BasePresenter<AuthorsMvp.View>(), AuthorsMvp.Presenter {

	var sort: Int = 192
	var authors: ArrayList<AuthorInList> = ArrayList()
		private set

	override fun onReload() {
		makeRestCall(
				getAuthorsInternal().toObservable(),
				Consumer { authors ->
					this.authors = authors
					sendToView { it.onNotifyAdapter(authors) }
				}
		)
	}

	private fun getAuthorsInternal() =
			getAuthorsFromServer()
					.onErrorResumeNext {
						getAuthorsFromDb()
					}
					.doOnError { err -> sendToView { it.onShowErrorView(err.message) } }

	private fun getAuthorsFromServer(): Single<ArrayList<AuthorInList>> =
			DataManager.getAuthors(sort)
					.map { getAuthors(it) }

	private fun getAuthorsFromDb(): Single<ArrayList<AuthorInList>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getAuthorsPath(sort))
					.map { it.response }
					.map { AuthorsResponse.Deserializer().deserialize(it) }
					.map { getAuthors(it) }

	private fun getAuthors(response: AuthorsResponse): ArrayList<AuthorInList> =
			response.authors

	override fun setCurrentSort(sortValue: Int) {
		sort = sortValue
		onReload()
	}

	override fun getCurrentSort() = sort

	override fun onItemClick(position: Int, v: View?, item: AuthorInList) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: AuthorInList) {
	}
}