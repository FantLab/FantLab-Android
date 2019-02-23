package ru.fantlab.android.ui.modules.plans.pubplans

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Pubplans
import ru.fantlab.android.provider.rest.PubplansSortOption
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface PubplansMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener,
			ContextMenuDialogView.ListDialogViewActionCallback {

		fun onNotifyAdapter(items: ArrayList<Pubplans.Object>, page: Int)

		fun getLoadMore(): OnLoadMore<Int>

		fun onItemClicked(item: Pubplans.Object)

		fun onItemLongClicked(position: Int, v: android.view.View?, item: Pubplans.Object)

	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<Pubplans.Object>,
			BaseMvp.PaginationListener<Int> {

		fun getPubplans(page: Int, force: Boolean)

		fun setCurrentSort(sortBy: PubplansSortOption?, filterLang: String?, filterPublisher: String?)
	}
}