package ru.fantlab.android.ui.modules.editor.popup

import ru.fantlab.android.data.dao.model.Smile
import ru.fantlab.android.ui.base.mvp.BaseMvp
import java.io.File

interface EditorLinkImageMvp {

	interface EditorLinkCallback {
		fun onAppendLink(title: String, link: String, isLink: Boolean)
		fun onSmileAdded(smile: Smile?)
	}

	interface View : BaseMvp.View {
		fun onUploaded(title: String, link: String)
	}

	interface Presenter {
		fun onSubmit(title: String?, file: File)
	}
}