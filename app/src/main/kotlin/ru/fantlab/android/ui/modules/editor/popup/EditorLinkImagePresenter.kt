package ru.fantlab.android.ui.modules.editor.popup

import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import java.io.File

class EditorLinkImagePresenter : BasePresenter<EditorLinkImageMvp.View>(), EditorLinkImageMvp.Presenter {
	override fun onSubmit(title: String?, file: File) {
		if (file.exists()) {/*
			val image = RequestBody.create(MediaType.parse("image/*"), file)
			makeRestCall(ImgurProvider.getImgurService().postImage(title, image),
					{ imgurReponseModel ->
						if (imgurReponseModel.getData() != null) {
							val imageResponse = imgurReponseModel.getData()
							sendToView { view ->
								view.onUploaded(title
										?: imageResponse.getTitle(), imageResponse.getLink())
							}
							return@makeRestCall
						}
						sendToView { view -> view.onUploaded(null, null) }
					}, false)*/*/
		} else {
			if (view != null) view!!.onUploaded("", "")
		}
	}
}