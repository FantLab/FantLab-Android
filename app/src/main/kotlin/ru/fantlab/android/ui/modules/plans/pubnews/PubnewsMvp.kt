package ru.fantlab.android.ui.modules.plans.pubnews

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Pubnews
import ru.fantlab.android.helper.FantlabHelper
import ru.fantlab.android.provider.rest.PubnewsSortOption
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface PubnewsMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener,
			ContextMenuDialogView.ListDialogViewActionCallback {

		fun onNotifyAdapter(items: ArrayList<Pubnews.Object>, page: Int)

		fun getLoadMore(): OnLoadMore<Int>

		fun onItemClicked(item: Pubnews.Object)

		fun onItemLongClicked(position: Int, v: android.view.View?, item: Pubnews.Object)

	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<Pubnews.Object>,
			BaseMvp.PaginationListener<Int> {

		fun getPubnews(page: Int, force: Boolean)

		fun setCurrentSort(sortBy: PubnewsSortOption?, filterLang: String?, filterPublisher: String?)

		fun getCurrentSort(): FantlabHelper.PubnewsSort<PubnewsSortOption, Int, Int>

	}
}