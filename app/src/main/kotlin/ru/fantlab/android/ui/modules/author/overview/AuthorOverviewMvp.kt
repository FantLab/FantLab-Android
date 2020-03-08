package ru.fantlab.android.ui.modules.author.overview

import android.os.Bundle
import ru.fantlab.android.data.dao.model.*
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface AuthorOverviewMvp {

	interface View : BaseMvp.View {

		fun onInitViews(author: Author, biography: Biography?, classificatory: ArrayList<GenreGroup>, awards: ArrayList<Nomination>)

		fun onShowErrorView(msgRes: String?)

		fun onItemClicked(item: Nomination)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<Nomination> {

		fun onFragmentCreated(bundle: Bundle)
	}
}