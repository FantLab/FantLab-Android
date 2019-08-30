package ru.fantlab.android.ui.modules.work.awards

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Awards
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface WorkAwardsMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onInitViews(awards: Awards)
	}

	interface Presenter : BaseMvp.Presenter {
		fun onCallApi(workId: Int)
	}
}