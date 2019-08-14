package ru.fantlab.android.ui.modules.bookcases.editor

import ru.fantlab.android.ui.base.mvp.BaseMvp

interface BookcaseEditorMvp {

    interface View : BaseMvp.View {

        fun onEmptyBookcaseName(isEmpty: Boolean)

        fun onSuccessfullyUpdated()

        fun onSuccessfullyCreated()
    }

    interface Presenter : BaseMvp.Presenter {

        fun updateBookcase()

        fun createBookcase(bookcaseType: String, bookcaseName: String, isPublic: Boolean, bookcaseComment: String?)
    }
}