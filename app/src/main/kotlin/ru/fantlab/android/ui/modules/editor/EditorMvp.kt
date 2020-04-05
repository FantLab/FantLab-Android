package ru.fantlab.android.ui.modules.editor

import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.data.dao.model.TopicMessage
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.modules.editor.popup.EditorLinkImageMvp
import ru.fantlab.android.ui.modules.editor.smiles.SmileMvp
import ru.fantlab.android.ui.widgets.editor.EditorLayout

interface EditorMvp {

	interface View : BaseMvp.View, EditorLinkImageMvp.EditorLinkCallback,
			EditorLayout.EditorListener, SmileMvp.SmileCallback {
		fun onSendResultAndFinish(commentModel: Response, isNew: Boolean)

		fun onSendEditorResult()

		fun onSendReviewResultAndFinish(comment: Response, isNew: Boolean)

		fun onSendMessageResult(result: String)

		fun onSendNewTopicMessage(message: TopicMessage)

		fun onEditTopicMessage(messageId: Int, messageText: String)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onEditorNewResponse(id: Int, savedText: CharSequence?, mode: String)

		fun onEditorEditResponse(workId: Int, commentId: Int, newText: CharSequence?)

		fun onEditorNewMessage(id: Int, savedText: CharSequence?, mode: String)

		fun onEditorNewTopicMessage(topicId: Int, savedText: CharSequence?, mode: String)

		fun onEditorEditTopicMessage(messageId: Int, savedText: CharSequence?)

		fun onEditorNewComment(id: Int, savedText: CharSequence?)

		fun onHandleSubmission(savedText: CharSequence?, extraType: String?,
							   itemId: Int?,
							   reviewComment: Response?,
							   mode: String)
	}
}
