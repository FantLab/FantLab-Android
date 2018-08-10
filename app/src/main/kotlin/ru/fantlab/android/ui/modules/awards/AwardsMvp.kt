package ru.fantlab.android.ui.modules.awards

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.AwardInList
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.SortView
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface AwardsMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener,
			SortView.SortViewListener,
			ContextMenuDialogView.ListDialogViewActionCallback {

		fun onNotifyAdapter(items: ArrayList<AwardInList>)

		fun onItemClicked(item: AwardInList)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<AwardInList> {

		fun onReload()

		fun onWorkOffline()

		fun getAwards(): ArrayList<AwardInList>
	}
}