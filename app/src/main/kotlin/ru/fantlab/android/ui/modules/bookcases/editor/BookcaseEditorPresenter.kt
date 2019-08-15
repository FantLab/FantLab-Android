package ru.fantlab.android.ui.modules.bookcases.editor

import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.response.CreateBookcaseResponse
import ru.fantlab.android.data.dao.response.UpdateBookcaseResponse
import ru.fantlab.android.helper.InputHelper

class BookcaseEditorPresenter : BasePresenter<BookcaseEditorMvp.View>(), BookcaseEditorMvp.Presenter {

    override fun updateBookcase(bookcaseId: Int, bookcaseType: String, bookcaseName: String, isPublic: Boolean, bookcaseComment: String?) {
        val bookcaseNameIsEmpty = InputHelper.isEmpty(bookcaseName)
        sendToView { it.onEmptyBookcaseName(bookcaseNameIsEmpty) }
        if (!bookcaseNameIsEmpty) {
            makeRestCall(
                    DataManager.updateBookcase(bookcaseId, bookcaseType, bookcaseName, if (isPublic) "1" else "0", bookcaseComment).toObservable(),
                    Consumer { response ->
                        val result = UpdateBookcaseResponse.Parser().parse(response)
                        if (result != null) {
                            sendToView { it.onSuccessfullyUpdated() }
                        } else {
                            sendToView { it.showErrorMessage(response) }
                        }
                    }
            )
        }
    }

    override fun createBookcase(bookcaseType: String, bookcaseName: String, isPublic: Boolean, bookcaseComment: String?) {
        val bookcaseNameIsEmpty = InputHelper.isEmpty(bookcaseName)
        sendToView { it.onEmptyBookcaseName(bookcaseNameIsEmpty) }
        if (!bookcaseNameIsEmpty) {
            makeRestCall(
                    DataManager.createBookcase(bookcaseType, bookcaseName, if (isPublic) "1" else "0", bookcaseComment).toObservable(),
                    Consumer { response ->
                        val result = CreateBookcaseResponse.Parser().parse(response)
                        if (result != null) {
                            sendToView { it.onSuccessfullyCreated() }
                        } else {
                            sendToView { it.showErrorMessage(response) }
                        }
                    }
            )
        }
    }
}