package ru.fantlab.android.ui.modules.author.editions

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.EditionsBlocks
import ru.fantlab.android.data.dao.response.AuthorEditionsResponse
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface AuthorEditionsMvp {

	interface View : BaseMvp.View,
            SwipeRefreshLayout.OnRefreshListener,
            android.view.View.OnClickListener{

		fun onInitViews(authorEditionsResponse: AuthorEditionsResponse)

		fun onNotifyAdapter()

		fun onSetTabCount(allCount: Int)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)

        fun getEditions(): ArrayList<EditionsBlocks.Edition>
	}
}