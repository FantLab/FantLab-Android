package ru.fantlab.android.ui.modules.author.editions

import android.os.Bundle
import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.EditionsBlocks
import ru.fantlab.android.data.dao.response.AuthorEditionsResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getAuthorEditionsPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class AuthorEditionsPresenter : BasePresenter<AuthorEditionsMvp.View>(),
		AuthorEditionsMvp.Presenter {

	private var authorId: Int = -1

	override fun onFragmentCreated(bundle: Bundle) {
		authorId = bundle.getInt(BundleConstant.EXTRA)
		getEditions(false)
	}

	override fun getEditions(force: Boolean) {
		makeRestCall(
				getEditionsInternal(force).toObservable(),
				Consumer { (editionsBlocks, count) ->
					sendToView { it.onInitViews(editionsBlocks, count) }
				}
		)
	}

	private fun getEditionsInternal(force: Boolean) =
			getEditionsFromServer()
					.onErrorResumeNext { throwable ->
						if (!force) {
							getEditionsFromDb()
						} else {
							throw throwable
						}
					}

	private fun getEditionsFromServer(): Single<Pair<ArrayList<EditionsBlocks.EditionsBlock>?, Int>> =
			DataManager.getAuthorEditions(authorId, true)
					.map { getEditions(it) }

	private fun getEditionsFromDb(): Single<Pair<ArrayList<EditionsBlocks.EditionsBlock>?, Int>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getAuthorEditionsPath(authorId, true))
					.map { it.response }
					.map { AuthorEditionsResponse.Deserializer().deserialize(it) }
					.map { getEditions(it) }

	private fun getEditions(response: AuthorEditionsResponse):
			Pair<ArrayList<EditionsBlocks.EditionsBlock>?, Int> =
			response.editions?.editionsBlocks to response.editionsInfo.allCount

	override fun onItemClick(position: Int, v: View?, item: EditionsBlocks.Edition) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: EditionsBlocks.Edition?) {
	}
}