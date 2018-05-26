package ru.fantlab.android.ui.modules.author.works

import android.os.Bundle
import ru.fantlab.android.data.dao.model.WorksBlocks
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface AuthorWorksMvp {

	interface View : BaseMvp.View {

		fun onInitViews(cycles: WorksBlocks?, works: WorksBlocks)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)
	}
}