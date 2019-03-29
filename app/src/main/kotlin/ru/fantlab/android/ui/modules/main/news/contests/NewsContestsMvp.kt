package ru.fantlab.android.ui.modules.main.news.contests

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Award
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface NewsContestsMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onInitViews(contests: List<Pair<String?, List<Award.ContestWork>>>?)

	}

	interface Presenter : BaseMvp.Presenter{

		fun onFragmentCreated(bundle: Bundle)

		fun getContests(force: Boolean)
	}
}