package ru.fantlab.android.ui.modules.author.editions

import android.os.Bundle
import ru.fantlab.android.data.dao.response.AuthorEditionsResponse
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface AuthorEditionsMvp {

	interface View : BaseMvp.View {

		fun onInitViews(authorEditionsResponse: AuthorEditionsResponse)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)
	}
}