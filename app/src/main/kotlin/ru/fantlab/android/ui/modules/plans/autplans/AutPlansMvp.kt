package ru.fantlab.android.ui.modules.plans.autplans

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Autplans
import ru.fantlab.android.helper.FantlabHelper
import ru.fantlab.android.provider.rest.AutplansSortOption
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface AutPlansMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener,
			ContextMenuDialogView.ListDialogViewActionCallback {

		fun onNotifyAdapter(items: ArrayList<Autplans.Object>, page: Int)

		fun getLoadMore(): OnLoadMore<Int>

		fun onItemClicked(item: Autplans.Object)

		fun onItemLongClicked(position: Int, v: android.view.View?, item: Autplans.Object)

	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<Autplans.Object>,
			BaseMvp.PaginationListener<Int> {

		fun getAutPlans(page: Int, force: Boolean)

		fun setCurrentSort(sortBy: AutplansSortOption?, filterLang: String?)

		fun getCurrentSort(): FantlabHelper.AutPlansSort<AutplansSortOption, Int>

	}
}