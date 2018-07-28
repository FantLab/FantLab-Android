package ru.fantlab.android.ui.modules.author.bibliography

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.EditionsBlocks
import ru.fantlab.android.data.dao.model.WorksBlocks
import ru.fantlab.android.data.dao.response.AuthorResponse
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface AuthorBibliographyMvp {

	interface View : BaseMvp.View,
            SwipeRefreshLayout.OnRefreshListener,
            android.view.View.OnClickListener{

		fun onInitViews(authorBibliographyResponse: WorksBlocks?)

	}

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)

        fun getBibliography(): WorksBlocks?
	}
}