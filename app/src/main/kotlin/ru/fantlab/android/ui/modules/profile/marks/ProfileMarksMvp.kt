package ru.fantlab.android.ui.modules.profile.marks

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.newmodel.Mark
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface ProfileMarksMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onNotifyAdapter(items: ArrayList<Mark>, page: Int)

		fun getLoadMore(): OnLoadMore<Int>

		fun onSetTabCount(count: Int)

		fun onItemClicked(item: Mark)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<Mark>,
			BaseMvp.PaginationListener<Int> {

		fun getMarks() : ArrayList<Mark>

		fun onWorkOffline(userId: Int)
	}
}