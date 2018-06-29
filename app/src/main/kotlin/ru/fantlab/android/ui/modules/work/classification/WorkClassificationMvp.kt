package ru.fantlab.android.ui.modules.work.classification

import android.os.Bundle
import ru.fantlab.android.data.dao.model.ClassificationGenre
import ru.fantlab.android.data.dao.model.GenreGroup
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface WorkClassificationMvp {

	interface View : BaseMvp.View {

		fun onInitViews(classificatory: ArrayList<GenreGroup>)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)

		fun getResponses(): ArrayList<ClassificationGenre>
	}
}