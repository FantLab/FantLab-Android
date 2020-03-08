package ru.fantlab.android.ui.modules.awards.item

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Awards
import ru.fantlab.android.data.dao.model.Nomination
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface ItemAwardsMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onInitViews(awards: Awards)
	}

	interface Presenter : BaseMvp.Presenter{

		fun getWorkAwards(workId: Int)
		fun getAuthorAwards(authorId: Int)
	}
}