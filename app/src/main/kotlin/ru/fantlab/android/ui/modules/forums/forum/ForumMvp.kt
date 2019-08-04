package ru.fantlab.android.ui.modules.forums.forum

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.ClassificatorModel
import ru.fantlab.android.data.dao.model.Forum
import ru.fantlab.android.data.dao.model.Forums
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface ForumMvp {

	interface View : BaseMvp.View,
			android.view.View.OnClickListener,
			SwipeRefreshLayout.OnRefreshListener {

		fun onNotifyAdapter(items: ArrayList<Forum.Topic>, page: Int)

		fun getLoadMore(): OnLoadMore<Int>

		fun onItemClicked(item: Forum.Topic)

		fun onItemLongClicked(position: Int, v: android.view.View?, item: Forum.Topic)

	}

	interface Presenter : BaseMvp.Presenter,
			BaseMvp.PaginationListener<Int>,
			BaseViewHolder.OnItemClickListener<Forum.Topic> {

		fun getTopics(force: Boolean)
	}
}