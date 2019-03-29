package ru.fantlab.android.ui.modules.award.contests

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Award
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface AwardContestsMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onInitViews(contests: List<Award.Contest>?)

		fun onSetTabCount(allCount: Int)

		fun onItemClicked(item: Award.Contest, position: Int)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<Award.Contest> {

		fun onFragmentCreated(bundle: Bundle)

		fun getContests(force: Boolean)
	}
}