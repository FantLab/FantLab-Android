package ru.fantlab.android.ui.modules.plans.autplans

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.ContextMenuBuilder
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.data.dao.model.Autplans
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.rest.AutplansSortOption
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.AutplansAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.plans.PlansPagerMvp
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView

class AutPlansFragment : BaseFragment<AutPlansMvp.View, AutPlansPresenter>(),
		AutPlansMvp.View {

	private val onLoadMore: OnLoadMore<Int> by lazy { OnLoadMore(presenter) }
	private val adapter: AutplansAdapter by lazy { AutplansAdapter(arrayListOf()) }
	private var publisherCallback: PlansPagerMvp.View? = null

	override fun fragmentLayout(): Int = R.layout.micro_grid_refresh_list

	override fun providePresenter(): AutPlansPresenter = AutPlansPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			stateLayout.hideProgress()
		}
		stateLayout.setEmptyText(R.string.no_plans)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		adapter.listener = presenter
		recycler.adapter = adapter
		getLoadMore().initialize(presenter.getCurrentPage() - 1, presenter.getPreviousTotal())
		recycler.addOnScrollListener(getLoadMore())
		presenter.onCallApi(1, null)
		fastScroller.attachRecyclerView(recycler)
		recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				publisherCallback?.onScrolled(dy > 0)
			}
		})
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (context is PlansPagerMvp.View) {
			publisherCallback = context
		}
	}

	override fun onDetach() {
		publisherCallback = null
		super.onDetach()
	}

	override fun onDestroyView() {
		recycler.removeOnScrollListener(getLoadMore())
		super.onDestroyView()
	}

	override fun onNotifyAdapter(items: ArrayList<Autplans.Object>, page: Int) {
		hideProgress()
		if (items.isEmpty()) {
			adapter.clear()
			stateLayout.showEmptyState()
			return
		}
		if (page <= 1) {
			adapter.insertItems(items)
		} else {
			adapter.addItems(items)
		}
	}

	override fun getLoadMore() = onLoadMore

	override fun onItemClicked(item: Autplans.Object) {
		WorkPagerActivity.startActivity(context!!, item.workId.toInt(), if (!InputHelper.isEmpty(item.rusname)) item.rusname else item.name, 0)
	}

	override fun onItemLongClicked(position: Int, v: View?, item: Autplans.Object) {
	}

	override fun onRefresh() {
		presenter.getAutPlans(1, true)
	}

	override fun onClick(v: View?) {
		onRefresh()
	}

	override fun hideProgress() {
		refresh.isRefreshing = false
		stateLayout.hideProgress()
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		refresh.isRefreshing = true
		stateLayout.showProgress()
	}

	override fun showErrorMessage(msgRes: String?) {
		callback?.showErrorMessage(msgRes)
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		showReload()
		super.showMessage(titleRes, msgRes)
	}

	private fun showReload() {
		hideProgress()
		stateLayout.showReload(adapter.itemCount)
	}

	override fun onItemSelected(parent: String, item: ContextMenus.MenuItem, position: Int, listItem: Any) {
		recycler?.scrollToPosition(0)
		when (item.id) {
			"sort" -> {
				presenter.setCurrentSort(AutplansSortOption.values()[position], null)
			}
			else -> {
				when (parent) {
					"lang" -> {
						presenter.setCurrentSort(null, item.id)
					}
				}
			}
		}
	}

	fun showSortDialog() {
		val dialogView = ContextMenuDialogView()
		val sort = presenter.getCurrentSort()
		dialogView.initArguments("sort", ContextMenuBuilder.buildForAutplansSorting(recycler.context, sort.sortBy))
		dialogView.show(childFragmentManager, "ContextMenuDialogView")
	}

	fun showFilterDialog() {
		val dialogView = ContextMenuDialogView()
		val sort = presenter.getCurrentSort()
		dialogView.initArguments("filter", ContextMenuBuilder.buildForAutplansFilter(recycler.context, sort.filterLang))
		dialogView.show(childFragmentManager, "ContextMenuDialogView")
	}

	companion object {

		fun newInstance(): AutPlansFragment {
			val view = AutPlansFragment()
			return view
		}
	}
}