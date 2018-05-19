package ru.fantlab.android.ui.modules.author.overview

import android.os.Bundle
import ru.fantlab.android.data.dao.newmodel.Author
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface AuthorOverviewMvp {

	interface View : BaseMvp.View {

		fun onInitViews(author: Author)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)
	}
}