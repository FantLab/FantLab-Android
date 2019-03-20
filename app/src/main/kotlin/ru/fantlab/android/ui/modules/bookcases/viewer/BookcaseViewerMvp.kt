package ru.fantlab.android.ui.modules.bookcases.viewer

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.BookcaseEdition
import ru.fantlab.android.data.dao.model.BookcaseFilm
import ru.fantlab.android.data.dao.model.BookcaseWork
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface BookcaseViewerMvp {

    interface View : BaseMvp.View,
            SwipeRefreshLayout.OnRefreshListener,
            android.view.View.OnClickListener,
            ContextMenuDialogView.ListDialogViewActionCallback {

        fun onNotifyEditionsAdapter(items: ArrayList<BookcaseEdition>)

        fun onNotifyWorksAdapter(items: ArrayList<BookcaseWork>)

        fun onNotifyFilmsAdapter(items: ArrayList<BookcaseFilm>)

        fun onSuccessfullyDeleted()
    }

    interface Presenter : BaseMvp.Presenter {

        fun getEditions(force: Boolean, bookcaseId: Int)

        fun getWorks(force: Boolean, bookcaseId: Int)

        fun getFilms(force: Boolean, bookcaseId: Int)

        fun deleteBookcase(bookcaseId: Int, userId: Int)
    }
}