package ru.fantlab.android.ui.modules.work.classification

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.GenreGroup
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface WorkClassificationMvp {

	interface View : BaseMvp.View,
			android.view.View.OnClickListener,
			SwipeRefreshLayout.OnRefreshListener {

		fun onInitViews(classificatory: ArrayList<GenreGroup>)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle)

		fun getClassificatory(force: Boolean)
	}
}