package ru.fantlab.android.ui.modules.communities.main

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Community
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter

interface CommunitiesMainMvp {

	interface View : BaseMvp.View,
			android.view.View.OnClickListener,
			SwipeRefreshLayout.OnRefreshListener,
			TreeViewAdapter.OnTreeNodeListener {

		fun onInitViews(main: ArrayList<Community.Main>, additional: ArrayList<Community.Additional>)

	}

	interface Presenter : BaseMvp.Presenter {

		fun getCommunity(force: Boolean)
	}
}