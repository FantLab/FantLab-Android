package ru.fantlab.android.ui.modules.authors

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.AuthorInList
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface AuthorsMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onNotifyAdapter(items: ArrayList<AuthorInList>)

		fun onItemClicked(item: AuthorInList)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<AuthorInList> {

		fun onReload()

		fun onWorkOffline()

		fun getAuthors(): ArrayList<AuthorInList>
	}
}