package ru.fantlab.android.ui.modules.classificator.age

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.ClassificatorModel
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface ClassificationAgeMvp {

	interface View : BaseMvp.View,
			android.view.View.OnClickListener,
			SwipeRefreshLayout.OnRefreshListener {

		fun onInitViews(classificators: ArrayList<ClassificatorModel>)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)
	}
}