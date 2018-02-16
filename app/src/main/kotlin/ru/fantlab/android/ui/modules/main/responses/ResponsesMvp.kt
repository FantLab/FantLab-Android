package ru.fantlab.android.ui.modules.main.responses

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface ResponsesMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onNotifyAdapter(items: List<Response>?, page: Int)

		fun getLoadMore(): OnLoadMore<Any>
	}

	interface Presenter :
			BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<Response>,
			BaseMvp.PaginationListener<Any> {

		fun onFragmentCreated()

		fun onCallApi(page: Int): Boolean

		fun onWorkOffline()
	}
}