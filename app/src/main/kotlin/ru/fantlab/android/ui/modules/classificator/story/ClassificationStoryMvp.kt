package ru.fantlab.android.ui.modules.classificator.story

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import ru.fantlab.android.data.dao.model.AdditionalImages
import ru.fantlab.android.data.dao.model.ClassificatorModel
import ru.fantlab.android.data.dao.model.Edition
import ru.fantlab.android.data.dao.response.ClassificatoriesResponse
import ru.fantlab.android.ui.adapter.viewholder.ClassificatorViewHolder
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ListDialogView

interface ClassificationStoryMvp {

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