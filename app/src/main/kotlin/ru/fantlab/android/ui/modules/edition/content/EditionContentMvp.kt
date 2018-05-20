package ru.fantlab.android.ui.modules.edition.content

import android.os.Bundle
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface EditionContentMvp {

	interface View : BaseMvp.View {

		fun onInitViews(content: ArrayList<String>)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)
	}
}