package ru.fantlab.android.ui.modules.edition.content

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.EditionContent
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface EditionContentMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onInitViews(content: ArrayList<EditionContent>)

		fun onSetTabCount(allCount: Int)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle)

		fun getContent(force: Boolean)
	}
}