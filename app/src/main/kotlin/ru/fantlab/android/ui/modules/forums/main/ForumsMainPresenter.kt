package ru.fantlab.android.ui.modules.forums.main

import android.os.Bundle
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Forums
import ru.fantlab.android.data.dao.response.ForumsResponse
import ru.fantlab.android.helper.FantlabHelper
import ru.fantlab.android.helper.single
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getForumsPath
import ru.fantlab.android.provider.storage.ClassificatoriesProvider
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.modules.forums.main.ForumsMainMvp

class ForumsMainPresenter : BasePresenter<ForumsMainMvp.View>(),
		ForumsMainMvp.Presenter {

	override fun getForum(force: Boolean) {
		makeRestCall(
				getForumInternal(force).toObservable(),
				Consumer { forum -> sendToView { it.onInitViews(forum) } }
		)
	}

	private fun getForumInternal(force: Boolean) =
			getForumFromServer()
					.onErrorResumeNext { throwable ->
						if (!force) {
							getForumFromDb()
						} else {
							throw throwable
						}
					}

	private fun getForumFromServer(): Single<ArrayList<Forums.ForumBlock>> =
			DataManager.getForums()
					.map { getForum(it) }

	private fun getForumFromDb(): Single<ArrayList<Forums.ForumBlock>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getForumsPath())
					.map { it.response }
					.map { ForumsResponse.Deserializer().deserialize(it) }
					.map { getForum(it) }

	private fun getForum(response: ForumsResponse): ArrayList<Forums.ForumBlock> = response.forums
}