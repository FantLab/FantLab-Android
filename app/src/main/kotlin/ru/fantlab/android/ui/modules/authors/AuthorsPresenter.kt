package ru.fantlab.android.ui.modules.authors

import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.AuthorInList
import ru.fantlab.android.data.dao.model.getAuthorsList
import ru.fantlab.android.data.dao.model.save
import ru.fantlab.android.helper.observe
import ru.fantlab.android.provider.rest.RestProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import timber.log.Timber

class AuthorsPresenter : BasePresenter<AuthorsMvp.View>(), AuthorsMvp.Presenter {

	private var authors: ArrayList<AuthorInList> = ArrayList()

	override fun getAuthors(): ArrayList<AuthorInList> = authors

	override fun onItemClick(position: Int, v: View?, item: AuthorInList) {
		view?.onItemClicked(item)
	}

	override fun onItemLongClick(position: Int, v: View?, item: AuthorInList?) {
	}

	override fun onError(throwable: Throwable) {
		onWorkOffline()
		super.onError(throwable)
	}

	override fun onWorkOffline() {
		manageDisposable(
				getAuthorsList().toObservable().observe().subscribe(
						{ authors ->
							authors?.let {
								sendToView { it.onNotifyAdapter(authors) }
							}
						},
						Timber::e
				)
		)
	}

	override fun onReload() {
		makeRestCall(RestProvider.getAuthorService().getList(), Consumer { authors ->
			authors.save()
			sendToView { it.onNotifyAdapter(authors) }
		})
	}
}