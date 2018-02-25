package ru.fantlab.android.ui.modules.profile.overview

import android.os.Bundle
import ru.fantlab.android.data.dao.model.User
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface ProfileOverviewMvp {

	interface View : BaseMvp.View {

		fun onInitViews(user: User?)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)
	}
}