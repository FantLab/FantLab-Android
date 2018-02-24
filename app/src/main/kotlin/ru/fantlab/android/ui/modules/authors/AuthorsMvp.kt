package ru.fantlab.android.ui.modules.authors

import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface AuthorsMvp {

	interface View : BaseMvp.View, android.view.View.OnClickListener {

		fun onNotifyAdapter(items: List<AuthorInList>?, page: Int)

		fun onItemClicked(item: AuthorInList)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<AuthorInList> {

		fun getAuthors(): ArrayList<AuthorInList>
	}
}