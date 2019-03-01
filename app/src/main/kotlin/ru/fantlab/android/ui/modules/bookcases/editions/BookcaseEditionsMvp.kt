package ru.fantlab.android.ui.modules.bookcases.editions

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.BookcaseEdition
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface BookcaseEditionsMvp {

    interface View : BaseMvp.View,
            SwipeRefreshLayout.OnRefreshListener,
            android.view.View.OnClickListener,
            ContextMenuDialogView.ListDialogViewActionCallback {

        fun onNotifyAdapter(items: ArrayList<BookcaseEdition>)

        fun onItemClicked(item: BookcaseEdition)
    }

    interface Presenter : BaseMvp.Presenter,
            BaseViewHolder.OnItemClickListener<BookcaseEdition> {

        fun getEditions(force: Boolean, bookcaseId: Int)
    }
}