package ru.fantlab.android.ui.modules.bookcases.selector

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.BookcaseSelection
import ru.fantlab.android.ui.adapter.viewholder.BookcaseSelectionViewHolder
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface BookcasesSelectorMvp {
    interface View : BaseMvp.View,
            SwipeRefreshLayout.OnRefreshListener,
            android.view.View.OnClickListener,
            ContextMenuDialogView.ListDialogViewActionCallback {

        fun onInitViews(items: ArrayList<BookcaseSelection>?)

        fun onSetTabCount(allCount: Int)

        fun onItemClicked(item: BookcaseSelection, position: Int)

        fun onItemSelected(item: BookcaseSelection, position: Int)

        fun onItemSelectionUpdated()
    }

    interface Presenter : BaseMvp.Presenter,
            BaseViewHolder.OnItemClickListener<BookcaseSelection>,
            BookcaseSelectionViewHolder.OnItemSelectListener<BookcaseSelection>{

        fun onFragmentCreated(bundle: Bundle)

        fun getBookcases(userId: Int, bookcaseType: String, entityId: Int, force: Boolean)

        fun includeItem(bookcaseId: Int, entityId: Int, include: Boolean)
    }

}