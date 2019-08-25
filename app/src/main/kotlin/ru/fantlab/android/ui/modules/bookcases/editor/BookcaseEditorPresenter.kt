package ru.fantlab.android.ui.modules.bookcases.editor

import io.reactivex.Single
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Bookcase
import ru.fantlab.android.data.dao.response.BookcaseInformationResponse
import ru.fantlab.android.data.dao.response.CreateBookcaseResponse
import ru.fantlab.android.data.dao.response.UpdateBookcaseResponse
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.rest.getPersonalBookcaseInformationPath
import ru.fantlab.android.provider.storage.DbProvider

class BookcaseEditorPresenter : BasePresenter<BookcaseEditorMvp.View>(), BookcaseEditorMvp.Presenter {

    override fun updateBookcase(bookcaseId: Int, bookcaseType: String, bookcaseName: String, isPublic: Boolean, bookcaseComment: String?) {
        val bookcaseNameIsEmpty = InputHelper.isEmpty(bookcaseName)
        sendToView { it.onEmptyBookcaseName(bookcaseNameIsEmpty) }
        if (!bookcaseNameIsEmpty) {
            makeRestCall(
                    DataManager.updateBookcase(bookcaseId, bookcaseType, bookcaseName, if (isPublic) "1" else "0", bookcaseComment).toObservable(),
                    Consumer { response ->
                        val result = UpdateBookcaseResponse.Parser().parse(response)
                        if (result.resCode != null) {
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
                        if (result.bookcaseId != null) {
                            sendToView { it.onSuccessfullyCreated() }
                        } else {
                            sendToView { it.showErrorMessage(response) }
                        }
                    }
            )
        }
    }

    override fun retrieveBookcaseInfo(bookcaseId: Int) {
        makeRestCall(
                retrieveBookcaseInfoInternal(bookcaseId).toObservable(),
                Consumer { bookcase ->
                    sendToView { it.onBookcaseInformationRetrieved(bookcase) }
                }
        )
    }

    private fun retrieveBookcaseInfoInternal(bookcaseId: Int) =
            retrieveBookcaseInfoFromServer(bookcaseId)
                    .onErrorResumeNext { retrieveBookcaseInfoFromDb(bookcaseId) }

    private fun retrieveBookcaseInfoFromServer(bookcaseId: Int): Single<Bookcase> =
            DataManager.getPersonalBookcaseInformation(bookcaseId)
                    .map { getBookcase(it) }

    private fun retrieveBookcaseInfoFromDb(bookcaseId: Int): Single<Bookcase> =
            DbProvider.mainDatabase
                    .responseDao()
                    .get(getPersonalBookcaseInformationPath(bookcaseId))
                    .map { it.response }
                    .map { BookcaseInformationResponse.Deserializer().deserialize(it) }
                    .map { getBookcase(it) }

    private fun getBookcase(response: BookcaseInformationResponse): Bookcase =
            response.bookcase

}