package ru.fantlab.android.ui.modules.edition.overview

import android.os.Bundle
import ru.fantlab.android.data.dao.model.AdditionalImages
import ru.fantlab.android.data.dao.model.Edition
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ListDialogView

interface EditionOverviewMvp {

	interface View : BaseMvp.View,
			android.view.View.OnClickListener,
			ListDialogView.ListDialogViewActionCallback {

		fun onInitViews(edition: Edition, additionalImages: AdditionalImages?)

		fun onSetTitle(title: String)

		fun onShowErrorView(msgRes: String?)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle)
	}
}