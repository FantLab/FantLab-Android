package ru.fantlab.android.ui.modules.profile.bookcases

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Bookcase
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface ProfileBookcasesMvp {
    interface View : BaseMvp.View,
            SwipeRefreshLayout.OnRefreshListener,
            android.view.View.OnClickListener,
            ContextMenuDialogView.ListDialogViewActionCallback {

        fun onNotifyAdapter(items: ArrayList<Bookcase>, page: Int)

        fun getLoadMore(): OnLoadMore<Int>

        fun onSetTabCount(count: Int)

        fun onItemClicked(item: Bookcase)
    }

    interface Presenter : BaseMvp.Presenter,
            BaseViewHolder.OnItemClickListener<Bookcase>,
            BaseMvp.PaginationListener<Int> {

    }

}