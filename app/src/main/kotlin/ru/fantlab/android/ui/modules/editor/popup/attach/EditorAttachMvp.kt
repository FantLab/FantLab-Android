package ru.fantlab.android.ui.modules.editor.popup.attach

import ru.fantlab.android.data.dao.AttachModel
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface EditorAttachMvp {

	interface EditorLinkCallback {
		fun onAppendAttach(filename: String, filepath: String)

		fun getAttachesList(): ArrayList<AttachModel>

		fun removeAttach(attach: AttachModel)

		fun onClearAttach()
	}

	interface View : BaseMvp.View

	interface Presenter
}