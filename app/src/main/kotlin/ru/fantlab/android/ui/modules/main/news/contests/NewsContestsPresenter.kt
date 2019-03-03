package ru.fantlab.android.ui.modules.main.news.contests

import android.os.Bundle
import android.view.View
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Award
import ru.fantlab.android.data.dao.response.ContestResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getContestPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class NewsContestsPresenter : BasePresenter<NewsContestsMvp.View>(),
		NewsContestsMvp.Presenter {

	private var contestId = -1

	override fun onFragmentCreated(bundle: Bundle) {
		contestId = bundle.getInt(BundleConstant.EXTRA)
		getContests(false)
	}

	override fun getContests(force: Boolean) {
		makeRestCall(
				getContestsInternal(force).toObservable(),
				Consumer { contests -> sendToView { it.onInitViews(contests.toNullable()) } }
		)
	}

	private fun getContestsInternal(force: Boolean) =
			getContestsFromServer()
					.onErrorResumeNext { throwable ->
						if (!force) {
							getContestsFromDb()
						} else {
							throw throwable
						}
					}

	private fun getContestsFromServer(): Single<Optional<List<Pair<String, List<Award.ContestWork>>>>> =
			DataManager.getContest(contestId, true)
					.map { getContests(it) }

	private fun getContestsFromDb(): Single<Optional<List<Pair<String, List<Award.ContestWork>>>>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getContestPath(contestId, true))
					.map { it.response }
					.map { ContestResponse.Deserializer().deserialize(it) }
					.map { getContests(it) }

	private fun getContests(response: ContestResponse): Optional<List<Pair<String, List<Award.ContestWork>>>> =
			response.contest.toOptional()
}