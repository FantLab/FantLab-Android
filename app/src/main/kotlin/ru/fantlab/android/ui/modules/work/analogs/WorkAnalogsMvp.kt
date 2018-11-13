package ru.fantlab.android.ui.modules.work.analogs

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.WorkAnalog
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface WorkAnalogsMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onInitViews(analogs: ArrayList<WorkAnalog>)

		fun onSetTabCount(count: Int)

		fun onNotifyAdapter()

		fun onItemClicked(item: WorkAnalog)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<WorkAnalog> {

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)

		fun getAnalogs(): ArrayList<WorkAnalog>
	}
}