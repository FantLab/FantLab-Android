package ru.fantlab.android.ui.modules.main.news

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.News
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface NewsMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onNotifyAdapter(items: ArrayList<News>, page: Int)

		fun getLoadMore(): OnLoadMore<String>

		fun onItemClicked(item: News, view: android.view.View?)

		fun onItemLongClicked(position: Int, v: android.view.View?, item: News)


	}

	interface Presenter :
			BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<News>,
			BaseMvp.PaginationListener<String> {

		fun onFragmentCreated()

		fun onCallApi(page: Int): Boolean


	}
}