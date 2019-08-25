package ru.fantlab.android.ui.modules.bookcases.overview

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Bookcase
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface BookcasesOverviewMvp {
    interface View : BaseMvp.View,
            SwipeRefreshLayout.OnRefreshListener,
            android.view.View.OnClickListener,
            ContextMenuDialogView.ListDialogViewActionCallback {

        fun onInitViews(items: ArrayList<Bookcase>?)

        fun onSetTabCount(allCount: Int)

        fun onItemClicked(item: Bookcase, position: Int)
    }

    interface Presenter : BaseMvp.Presenter,
            BaseViewHolder.OnItemClickListener<Bookcase> {

        fun onFragmentCreated()

        fun getBookcases(force: Boolean)
    }

}