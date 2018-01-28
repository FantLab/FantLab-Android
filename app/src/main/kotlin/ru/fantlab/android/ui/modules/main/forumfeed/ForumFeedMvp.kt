package ru.fantlab.android.ui.modules.main.forumfeed

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.ForumMessage
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

interface ForumFeedMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onNotifyAdapter(items: List<ForumMessage>?, page: Int)

		fun getLoadMore(): OnLoadMore<Any>
	}

	interface Presenter :
			BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<ForumMessage>,
			BaseMvp.PaginationListener<Any> {

		fun onFragmentCreated()

		fun onCallApi(page: Int): Boolean

		fun getMessages(): ArrayList<ForumMessage>

		fun onWorkOffline()
	}
}