package ru.fantlab.android.ui.modules.editor

import android.annotation.SuppressLint
import com.github.kittinunf.fuel.core.ProgressCallback
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.data.dao.model.TopicMessage
import ru.fantlab.android.data.dao.model.TopicMessageDraft
import ru.fantlab.android.helper.BundleConstant.EDITOR_DRAFT_TYPE
import ru.fantlab.android.helper.BundleConstant.EDITOR_EDIT_RESPONSE
import ru.fantlab.android.helper.BundleConstant.EDITOR_EDIT_TOPIC_MESSAGE
import ru.fantlab.android.helper.BundleConstant.EDITOR_MESSAGE_TYPE
import ru.fantlab.android.helper.BundleConstant.EDITOR_NEW_COMMENT
import ru.fantlab.android.helper.BundleConstant.EDITOR_NEW_MESSAGE
import ru.fantlab.android.helper.BundleConstant.EDITOR_NEW_RESPONSE
import ru.fantlab.android.helper.BundleConstant.EDITOR_NEW_TOPIC_DRAFT_MESSAGE
import ru.fantlab.android.helper.BundleConstant.EDITOR_NEW_TOPIC_MESSAGE
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.helper.implementError
import ru.fantlab.android.helper.observe
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.DataManager.getTopicAttachUrl
import ru.fantlab.android.provider.rest.DataManager.getTopicDraftAttachUrl
import ru.fantlab.android.provider.rest.DataManager.sendTopicAttach
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import java.io.File

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
			when (mode) {
				EDITOR_MESSAGE_TYPE -> makeRestCall(
						DataManager.sendTopicMessage(topicId, savedText).toObservable(),
						Consumer { result ->
							if (view?.getAttachesList()?.isNullOrEmpty() == true)
								sendToView { it.onSendNewTopicMessage(result.message) }
							else
								onUploadAttach(EDITOR_NEW_TOPIC_MESSAGE, result.message)
						}
				)
				EDITOR_DRAFT_TYPE -> makeRestCall(
						DataManager.sendTopicMessageDraft(view?.getExtraIds()
								?: -1, savedText).toObservable(),
						Consumer { result ->
							if (view?.getAttachesList()?.isNullOrEmpty() == true)
								sendToView { it.onSendNewTopicMessageDraft(result.message) }
							else
								onUploadAttach(EDITOR_NEW_TOPIC_DRAFT_MESSAGE, result.message)
						}
				)
			}
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

	override fun onEditorNewComment(id: Int, savedText: CharSequence?) {}

	@SuppressLint("CheckResult")
	override fun <T> onUploadAttach(type: String, message: T) {
		sendToView { it.initAttachUpload() }
		Observable.fromIterable(view?.getAttachesList())
				.flatMap { attach ->
					sendToView { it.onPrepareUploadFile(attach) }
					when (type) {
						EDITOR_NEW_TOPIC_MESSAGE -> getTopicAttachUrl((message as TopicMessage).message.id, attach.filename, attach.filepath).toObservable()
						EDITOR_NEW_TOPIC_DRAFT_MESSAGE -> getTopicDraftAttachUrl(view?.getExtraIds() ?: -1, attach.filename, attach.filepath).toObservable()
						else -> null
					}
				}
				.doOnNext { attach ->
					sendToView { it.onNotifyUploadAttach(attach.third, attach.second, 0) }
				}
				.flatMap { attach ->
					var lastUpdate = 0L
					val handler: ProgressCallback = { readBytes, totalBytes ->
						if (System.currentTimeMillis() - lastUpdate > 800) {
							lastUpdate = System.currentTimeMillis()
							val progress = readBytes.toFloat() / totalBytes.toFloat() * 100
							sendToView { it.onNotifyUploadAttach(attach.third, attach.second, progress.toInt()) }
						}
					}
					when (type) {
						EDITOR_NEW_TOPIC_MESSAGE -> {
							sendTopicAttach(attach.first, attach.second, File(attach.third), handler)
						}
						else -> sendTopicAttach(attach.first, attach.second, File(attach.third), handler)
					}.toObservable()
				}
				.observe()
				.doOnComplete {
					when (type) {
						EDITOR_NEW_TOPIC_MESSAGE -> sendToView { it.onSendNewTopicMessage(message as TopicMessage) }
						EDITOR_NEW_TOPIC_DRAFT_MESSAGE -> sendToView { it.onSendNewTopicMessageDraft(message as TopicMessageDraft) }
					}
				}
				.subscribe({ result ->
					if (result.first == 200)
						sendToView { it.onAttachUploaded(result.second) }
					else
						sendToView { it.showMessage(R.string.error, R.string.request_error) }
				}, { throwable ->
					throwable.implementError(view)
					when (type) {
						EDITOR_NEW_TOPIC_MESSAGE -> sendToView { it.onSendNewTopicMessage(message as TopicMessage) }
						EDITOR_NEW_TOPIC_DRAFT_MESSAGE -> sendToView { it.onSendNewTopicMessageDraft(message as TopicMessageDraft) }
					}
				})
	}
}
