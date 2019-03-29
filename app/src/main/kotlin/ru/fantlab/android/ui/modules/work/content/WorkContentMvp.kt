package ru.fantlab.android.ui.modules.work.content

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.ChildWork
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface WorkContentMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onInitViews(content: ArrayList<ChildWork>)

		fun onSetTabCount(allCount: Int)

		fun onItemClicked(item: ChildWork)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<ChildWork> {

		fun onFragmentCreated(bundle: Bundle)

		fun getContent(force: Boolean)
	}
}