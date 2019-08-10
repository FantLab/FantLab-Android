package ru.fantlab.android.ui.modules.communities.main

import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Community
import ru.fantlab.android.data.dao.response.CommunitiesResponse
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getCommunitiesPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class CommunitiesMainPresenter : BasePresenter<CommunitiesMainMvp.View>(),
		CommunitiesMainMvp.Presenter {

	override fun getCommunity(force: Boolean) {
		makeRestCall(
				getCommunitiesInternal(force).toObservable(),
				Consumer { (main, additional) -> sendToView { it.onInitViews(main, additional) } }
		)
	}

	private fun getCommunitiesInternal(force: Boolean) =
			getForumFromServer()
					.onErrorResumeNext { throwable ->
						if (!force) {
							getForumFromDb()
						} else {
							throw throwable
						}
					}

	private fun getForumFromServer(): Single<Pair<ArrayList<Community.Main>, ArrayList<Community.Additional>> > =
			DataManager.getCommunities()
					.map { getCommunities(it) }

	private fun getForumFromDb(): Single<Pair<ArrayList<Community.Main>, ArrayList<Community.Additional>> > =
			DbProvider.mainDatabase
					.responseDao()
					.get(getCommunitiesPath())
					.map { it.response }
					.map { CommunitiesResponse.Deserializer().deserialize(it) }
					.map { getCommunities(it) }

	private fun getCommunities(response: CommunitiesResponse): Pair<ArrayList<Community.Main>, ArrayList<Community.Additional>> = response.communitiesMain to response.communitiesAdditional
}