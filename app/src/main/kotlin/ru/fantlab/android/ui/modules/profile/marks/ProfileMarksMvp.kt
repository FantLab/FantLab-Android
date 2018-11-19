package ru.fantlab.android.ui.modules.profile.marks

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Mark
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.dialog.RatingDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface ProfileMarksMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener,
			ContextMenuDialogView.ListDialogViewActionCallback,
			RatingDialogView.RatingDialogViewActionCallback {

		fun onNotifyAdapter(items: ArrayList<Mark>, page: Int)

		fun onSetMark(position: Int, mark: Int)

		fun getLoadMore(): OnLoadMore<Int>

		fun onSetTabCount(count: Int)

		fun onItemClicked(item: Mark)

		fun onItemLongClicked(position: Int, v: android.view.View?, item: Mark)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<Mark>,
			BaseMvp.PaginationListener<Int> {

		fun getMarks(userId: Int, force: Boolean)

		fun onSendMark(item: Mark, mark: Int, position: Int)
	}
}