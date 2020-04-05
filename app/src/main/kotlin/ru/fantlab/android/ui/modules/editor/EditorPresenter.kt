package ru.fantlab.android.ui.modules.editor

import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.helper.BundleConstant.EDITOR_EDIT_RESPONSE
import ru.fantlab.android.helper.BundleConstant.EDITOR_EDIT_TOPIC_MESSAGE
import ru.fantlab.android.helper.BundleConstant.EDITOR_NEW_COMMENT
import ru.fantlab.android.helper.BundleConstant.EDITOR_NEW_MESSAGE
import ru.fantlab.android.helper.BundleConstant.EDITOR_NEW_RESPONSE
import ru.fantlab.android.helper.BundleConstant.EDITOR_NEW_TOPIC_MESSAGE
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
				onEditorNewResponse(itemId, savedText, mode)
			}
			EDITOR_EDIT_RESPONSE -> {
				val extraId = view?.getExtraIds()
				if (extraId != null) onEditorEditResponse(itemId, extraId, savedText)
			}
			EDITOR_NEW_MESSAGE -> {
				if (itemId == PrefGetter.getLoggedUser()?.id) {
					sendToView { it.showErrorMessage("Ошибка") }
					return
				}
				onEditorNewMessage(itemId, savedText, mode)
			}
			EDITOR_NEW_TOPIC_MESSAGE -> {
				if (itemId < 1) {
					sendToView { it.showErrorMessage("Ошибка") }
					return
				}
				onEditorNewTopicMessage(itemId, savedText, mode)
			}
			EDITOR_EDIT_TOPIC_MESSAGE -> {
				if (itemId < 1) {
					sendToView { it.showErrorMessage("Ошибка") }
					return
				}
				onEditorEditTopicMessage(itemId, savedText)
			}
			EDITOR_NEW_COMMENT -> {
				onEditorNewComment(itemId, savedText)
			}
		}
	}

	override fun onEditorNewResponse(id: Int, savedText: CharSequence?, mode: String) {
		if (!InputHelper.isEmpty(savedText)) {
			makeRestCall(
					DataManager.sendResponse(id, savedText, mode).toObservable(),
					Consumer { result -> sendToView { it.onSendMessageResult(result) } }
			)
		}
	}

	override fun onEditorEditResponse(workId: Int, commentId: Int, newText: CharSequence?) {
		if (!InputHelper.isEmpty(newText)) {
			makeRestCall(
					DataManager.editResponse(workId, commentId, newText).toObservable(),
					Consumer { result -> sendToView { it.onSendMessageResult(result) } }
			)
		}
	}

	override fun onEditorNewMessage(id: Int, savedText: CharSequence?, mode: String) {
		if (!InputHelper.isEmpty(savedText)) {
			makeRestCall(
					DataManager.sendMessage(id, savedText, mode).toObservable(),
					Consumer { result -> sendToView { it.onSendMessageResult(result) } }
			)
		}
	}

	override fun onEditorNewTopicMessage(topicId: Int, savedText: CharSequence?, mode: String) {
		if (!InputHelper.isEmpty(savedText)) {
			makeRestCall(
					DataManager.sendTopicMessage(topicId, savedText).toObservable(),
					Consumer { result -> sendToView { it.onSendNewTopicMessage(result.message) } }
			)
		}
	}

	override fun onEditorEditTopicMessage(messageId: Int, savedText: CharSequence?) {
		if (!InputHelper.isEmpty(savedText)) {
			makeRestCall(
					DataManager.editTopicMessage(messageId, savedText).toObservable(),
					Consumer { _ -> sendToView { it.onEditTopicMessage(messageId, savedText.toString()) } }
			)
		}
	}

	override fun onEditorNewComment(id: Int, savedText: CharSequence?) {
	}
}
