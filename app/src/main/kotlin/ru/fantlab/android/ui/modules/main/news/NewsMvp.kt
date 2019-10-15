package ru.fantlab.android.ui.modules.main.news

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.News
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.viewholder.NewsViewHolder
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface NewsMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener,
			ContextMenuDialogView.ListDialogViewActionCallback,
			NewsViewHolder.OnOpenContextMenu {

		fun onNotifyAdapter(items: ArrayList<News>, page: Int)

		fun getLoadMore(): OnLoadMore<String>

		fun onItemClicked(item: News, view: android.view.View?)

		fun onItemLongClicked(position: Int, v: android.view.View?, item: News)

		override fun onOpenContextMenu(news: News)

	}

	interface Presenter :
			BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<News>,
			BaseMvp.PaginationListener<String> {

		fun onFragmentCreated()

		fun onCallApi(page: Int): Boolean


	}
}