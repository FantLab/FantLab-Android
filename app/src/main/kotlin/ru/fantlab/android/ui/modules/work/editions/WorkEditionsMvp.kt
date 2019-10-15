package ru.fantlab.android.ui.modules.work.editions

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.EditionsBlocks
import ru.fantlab.android.data.dao.model.EditionsInfo
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface WorkEditionsMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener,
			ContextMenuDialogView.ListDialogViewActionCallback {

		fun onInitViews(editions: EditionsBlocks?, editionsInfo: EditionsInfo)

		fun onItemClicked(item: EditionsBlocks.Edition)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<EditionsBlocks.Edition>
	{
		fun onCallApi(workId: Int)

		fun getEditions(force: Boolean)
	}
}