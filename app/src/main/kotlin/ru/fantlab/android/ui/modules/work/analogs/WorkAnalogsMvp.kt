package ru.fantlab.android.ui.modules.work.analogs

import android.os.Bundle
import ru.fantlab.android.data.dao.newmodel.WorkAnalog
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface WorkAnalogsMvp {

	interface View : BaseMvp.View {

		fun onInitViews(analogs: ArrayList<WorkAnalog>)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)
	}
}