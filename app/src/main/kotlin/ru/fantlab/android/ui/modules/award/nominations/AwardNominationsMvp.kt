package ru.fantlab.android.ui.modules.award.nominations

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Award
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface AwardNominationsMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onNotifyAdapter(items: List<Award.Nominations>?)

		fun onSetTabCount(allCount: Int)

		fun onItemClicked(item: Award.Nominations)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<Award.Nominations> {

		fun onFragmentCreated(bundle: Bundle)

		fun getNominations(force: Boolean)
	}
}