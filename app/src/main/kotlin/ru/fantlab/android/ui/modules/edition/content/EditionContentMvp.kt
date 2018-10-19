package ru.fantlab.android.ui.modules.edition.content

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.EditionContent
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface EditionContentMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onInitViews(content: ArrayList<EditionContent>)

		fun onNotifyAdapter()

		fun onSetTabCount(allCount: Int)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)

		fun getContent(): ArrayList<String>
	}
}