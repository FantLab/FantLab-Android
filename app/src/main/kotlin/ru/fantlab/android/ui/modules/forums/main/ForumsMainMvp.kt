package ru.fantlab.android.ui.modules.forums.main

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.ClassificatorModel
import ru.fantlab.android.data.dao.model.Forums
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter

interface ForumsMainMvp {

	interface View : BaseMvp.View,
			android.view.View.OnClickListener,
			SwipeRefreshLayout.OnRefreshListener,
			TreeViewAdapter.OnTreeNodeListener {

		fun onInitViews(forums: ArrayList<Forums.ForumBlock>)

	}

	interface Presenter : BaseMvp.Presenter {

		fun getForum(force: Boolean)
	}
}