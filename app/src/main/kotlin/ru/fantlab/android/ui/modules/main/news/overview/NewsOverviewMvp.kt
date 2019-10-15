package ru.fantlab.android.ui.modules.main.news.overview

import ru.fantlab.android.data.dao.model.News
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface NewsOverviewMvp {

	interface View : BaseMvp.View {

		fun onInitViews(news: News)
	}

	interface Presenter : BaseMvp.Presenter
}