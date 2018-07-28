package ru.fantlab.android.ui.modules.author.works

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.WorksBlocks
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface AuthorWorksMvp {

	interface View : BaseMvp.View,
            SwipeRefreshLayout.OnRefreshListener,
            android.view.View.OnClickListener {

		fun onInitViews(works: WorksBlocks)

		fun onSetTabCount(count: Int)

		fun onNotifyAdapter()

		fun onItemClicked(item: WorksBlocks.Work)
	}

	interface Presenter : BaseMvp.Presenter,
            BaseViewHolder.OnItemClickListener<WorksBlocks.Work> {

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)

        fun getWorks(): ArrayList<WorksBlocks.Work>
	}
}