package ru.fantlab.android.ui.modules.bookcases.selector

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Bookcase
import ru.fantlab.android.data.dao.model.BookcaseSelection
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface BookcasesSelectorMvp {
    interface View : BaseMvp.View,
            SwipeRefreshLayout.OnRefreshListener,
            android.view.View.OnClickListener,
            ContextMenuDialogView.ListDialogViewActionCallback {

        fun onInitViews(items: ArrayList<Bookcase>?)

        fun onSetTabCount(allCount: Int)

        fun onItemClicked(item: BookcaseSelection, position: Int)
    }

    interface Presenter : BaseMvp.Presenter,
            BaseViewHolder.OnItemClickListener<BookcaseSelection> {

        fun onFragmentCreated(bundle: Bundle)

        fun getBookcases(userId: Int, force: Boolean)
    }

}