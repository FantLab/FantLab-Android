package ru.fantlab.android.ui.modules.bookcases.editor

import ru.fantlab.android.data.dao.model.Bookcase
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface BookcaseEditorMvp {

    interface View : BaseMvp.View {

        fun onEmptyBookcaseName(isEmpty: Boolean)

        fun onSuccessfullyUpdated()

        fun onSuccessfullyCreated()

        fun onBookcaseInformationRetrieved(bookcase: Bookcase)
    }

    interface Presenter : BaseMvp.Presenter {

        fun updateBookcase(bookcaseId: Int, bookcaseType: String, bookcaseName: String, isPublic: Boolean, bookcaseComment: String?)

        fun createBookcase(bookcaseType: String, bookcaseName: String, isPublic: Boolean, bookcaseComment: String?)

        fun retrieveBookcaseInfo(bookcaseId: Int)
    }
}