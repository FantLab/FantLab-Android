package ru.fantlab.android.ui.modules.work.editions

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.EditionsBlocks
import ru.fantlab.android.data.dao.model.EditionsInfo
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface WorkEditionsMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onInitViews(editions: EditionsBlocks?, editionsInfo: EditionsInfo)

		fun onSetTabCount(allCount: Int)

		fun onItemClicked(item: EditionsBlocks.Edition)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<EditionsBlocks.Edition> {

		fun onFragmentCreated(bundle: Bundle)

		fun getEditions(force: Boolean)
	}
}