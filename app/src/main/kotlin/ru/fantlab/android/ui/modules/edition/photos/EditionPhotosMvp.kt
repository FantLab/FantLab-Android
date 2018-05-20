package ru.fantlab.android.ui.modules.edition.photos

import android.os.Bundle
import ru.fantlab.android.data.dao.newmodel.AdditionalImages
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface EditionPhotosMvp {

	interface View : BaseMvp.View {

		fun onInitViews(additionalImages: AdditionalImages?)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)
	}
}