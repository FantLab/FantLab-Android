package ru.fantlab.android.ui.modules.editor

import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.modules.editor.popup.EditorLinkImageMvp
import ru.fantlab.android.ui.modules.editor.smiles.SmileMvp
import ru.fantlab.android.ui.widgets.markdown.MarkDownLayout

interface EditorMvp {

    interface View : BaseMvp.View, EditorLinkImageMvp.EditorLinkCallback,
            MarkDownLayout.MarkdownListener, SmileMvp.SmileCallback {
        fun onSendResultAndFinish(commentModel: Response, isNew: Boolean)

        fun onSendMarkDownResult()

        fun onSendReviewResultAndFinish(comment: Response, isNew: Boolean)

        fun onSendMessageResult(result: String)
    }

    interface Presenter : BaseMvp.Presenter {

        fun onEditorNewReview(id: Int?, savedText: CharSequence?, mode: String)

        fun onEditorNewMessage(id: Int?, savedText: CharSequence?, mode: String)

        fun onEditorNewComment(id: Int?, savedText: CharSequence?)

        fun onHandleSubmission(savedText: CharSequence?, extraType: String?,
							   itemId: Int?,
							   reviewComment: Response?)

		fun onShowErrorMessage(message: String)
    }
}
