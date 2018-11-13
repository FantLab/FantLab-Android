package ru.fantlab.android.ui.modules.editor

import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.helper.BundleConstant.EDITOR_NEW_COMMENT
import ru.fantlab.android.helper.BundleConstant.EDITOR_NEW_MESSAGE
import ru.fantlab.android.helper.BundleConstant.EDITOR_NEW_RESPONSE
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class EditorPresenter : BasePresenter<EditorMvp.View>(), EditorMvp.Presenter {

	override fun onHandleSubmission(savedText: CharSequence?, extraType: String?,
									itemId: Int?, reviewComment: Response?, mode: String) {
		if (extraType == null || itemId == null) {
			throw NullPointerException("not have required parameters")
		}
		when (extraType) {
			EDITOR_NEW_RESPONSE -> {
				onEditorNewResponse(itemId, prepareHTML(savedText), mode)
			}
			EDITOR_NEW_MESSAGE -> {
				if (itemId == PrefGetter.getLoggedUser()?.id) {
					onShowErrorMessage("Ошибка")
					return
				}
				onEditorNewMessage(itemId, prepareHTML(savedText), mode)
			}
			EDITOR_NEW_COMMENT -> {
				onEditorNewComment(itemId, prepareHTML(savedText))
			}
		}
	}

	private fun prepareHTML(savedText: CharSequence?): CharSequence? {
		val REGEX_TAGS = "<(.*?)>".toRegex()
		return savedText?.replace(REGEX_TAGS, "[$1]")?.replace("[li]", "*")
	}

	override fun onEditorNewResponse(id: Int?, savedText: CharSequence?, mode: String) {
		if (!InputHelper.isEmpty(savedText)) {
			id?.let {
				makeRestCall(
						DataManager.sendResponse(id, savedText, mode)
								.map { it.get() }
								.toObservable(),
						Consumer { result ->
							sendToView { it.onSendMessageResult(result) }
						}
				)
			}
		}
	}

	override fun onEditorNewMessage(id: Int?, savedText: CharSequence?, mode: String) {
		if (!InputHelper.isEmpty(savedText)) {
			id?.let {
				makeRestCall(
						DataManager.sendMessage(id, savedText, mode)
								.map { it.get() }
								.toObservable(),
						Consumer { result ->
							sendToView { it.onSendMessageResult(result) }
						}
				)
			}
		}
	}

	override fun onEditorNewComment(id: Int?, savedText: CharSequence?) {
	}

	override fun onShowErrorMessage(message: String) {
		sendToView { it.showErrorMessage(message) }
	}
}
