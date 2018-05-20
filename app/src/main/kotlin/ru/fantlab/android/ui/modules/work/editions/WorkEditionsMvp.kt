package ru.fantlab.android.ui.modules.work.editions

import android.os.Bundle
import ru.fantlab.android.data.dao.newmodel.EditionsBlocks
import ru.fantlab.android.data.dao.newmodel.EditionsInfo
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface WorkEditionsMvp {

	interface View : BaseMvp.View {

		fun onInitViews(editions: EditionsBlocks?, editionsInfo: EditionsInfo)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)
	}
}