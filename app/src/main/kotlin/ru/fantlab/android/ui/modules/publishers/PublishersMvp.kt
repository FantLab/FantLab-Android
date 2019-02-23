package ru.fantlab.android.ui.modules.publishers

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Publishers
import ru.fantlab.android.provider.rest.PublishersSortOption
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface PublishersMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener,
			ContextMenuDialogView.ListDialogViewActionCallback {

		fun onNotifyAdapter(items: ArrayList<Publishers.Publisher>, page: Int)

		fun getLoadMore(): OnLoadMore<Int>

		fun onItemClicked(item: Publishers.Publisher)

		fun onItemLongClicked(position: Int, v: android.view.View?, item: Publishers.Publisher)

	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<Publishers.Publisher>,
			BaseMvp.PaginationListener<Int> {

		fun getPublishers(page: Int, force: Boolean)

		fun setCurrentSort(sortBy: PublishersSortOption?, filterCountry: String?, filterCategory: String?)
	}
}