package ru.fantlab.android.ui.modules.editor.popup.linkimage

import ru.fantlab.android.data.dao.model.Smile
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface EditorLinkImageMvp {

	interface EditorLinkCallback {
		fun onAppendLink(title: String, link: String, isLink: Boolean)
		fun onSmileAdded(smile: Smile?)
	}

	interface View : BaseMvp.View {
		fun onUploaded(title: String, link: String)
	}

	interface Presenter
}