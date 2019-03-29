package ru.fantlab.android.ui.modules.work.analogs

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.WorkAnalog
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface WorkAnalogsMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onInitViews(analogs: ArrayList<WorkAnalog>)

		fun onSetTabCount(count: Int)

		fun onItemClicked(item: WorkAnalog)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<WorkAnalog> {

		fun onFragmentCreated(bundle: Bundle)

		fun getAnalogs(force: Boolean)
	}
}