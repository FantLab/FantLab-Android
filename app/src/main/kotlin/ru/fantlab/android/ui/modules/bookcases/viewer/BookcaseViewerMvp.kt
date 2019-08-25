package ru.fantlab.android.ui.modules.bookcases.viewer

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.BookcaseEdition
import ru.fantlab.android.data.dao.model.BookcaseFilm
import ru.fantlab.android.data.dao.model.BookcaseWork
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.viewholder.BookcaseEditionViewHolder
import ru.fantlab.android.ui.adapter.viewholder.BookcaseFilmViewHolder
import ru.fantlab.android.ui.adapter.viewholder.BookcaseWorkViewHolder
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface BookcaseViewerMvp {

    interface View : BaseMvp.View,
            SwipeRefreshLayout.OnRefreshListener,
            android.view.View.OnClickListener,
            ContextMenuDialogView.ListDialogViewActionCallback {

        fun getLoadMore(): OnLoadMore<Int>

        fun onNotifyEditionsAdapter(items: ArrayList<BookcaseEdition>, page: Int)

        fun onNotifyWorksAdapter(items: ArrayList<BookcaseWork>, page: Int)

        fun onNotifyFilmsAdapter(items: ArrayList<BookcaseFilm>, page: Int)

        fun onSuccessfullyDeleted()

        fun onDeleteItemFromBookcase(itemId: Int)

        fun onUpdateItemComment(itemId: Int, itemComment: String?)
    }

    interface Presenter : BaseMvp.Presenter,
            BookcaseWorkViewHolder.onUpdateItemListener,
            BookcaseEditionViewHolder.onUpdateItemListener,
            BookcaseFilmViewHolder.onUpdateItemListener,
            BaseMvp.PaginationListener<Int> {

        fun setBookcaseType(type: String)

        fun getEditions(force: Boolean, bookcaseId: Int, page: Int)

        fun getWorks(force: Boolean, bookcaseId: Int, page: Int)

        fun getFilms(force: Boolean, bookcaseId: Int, page: Int)

        fun deleteBookcase(bookcaseId: Int)

        fun excludeItem(bookcaseId: Int, entityId: Int)

        fun updateComment(bookcaseId: Int, entityId: Int, comment: String?)
    }
}