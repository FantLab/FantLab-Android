package ru.fantlab.android.ui.modules.work.editions

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.EditionsBlocks
import ru.fantlab.android.data.dao.model.EditionsInfo
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface WorkEditionsMvp {

	interface View : BaseMvp.View,
            SwipeRefreshLayout.OnRefreshListener,
            android.view.View.OnClickListener {

		fun onInitViews(editions: EditionsBlocks?, editionsInfo: EditionsInfo)

        fun onNotifyAdapter()

        fun onSetTabCount(allCount: Int)

    }

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)

		fun getEditions(): ArrayList<EditionsBlocks.Edition>
	}
}