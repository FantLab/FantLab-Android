package ru.fantlab.android.ui.modules.profile.marks

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.UserMark
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface ProfileMarksMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onNotifyAdapter(items: List<UserMark>?, page: Int)

		fun getLoadMore(): OnLoadMore<Int>

		fun onItemClicked(item: UserMark)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<UserMark>,
			BaseMvp.PaginationListener<Int> {

		fun getMarks() : ArrayList<UserMark>

		fun onWorkOffline(userId: Int)
	}
}