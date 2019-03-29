package ru.fantlab.android.ui.modules.plans.pubplans

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
import ru.fantlab.android.data.dao.model.Pubplans
import ru.fantlab.android.provider.rest.PubplansSortOption
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.PlansAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.edition.EditionPagerActivity
import ru.fantlab.android.ui.modules.plans.PlansPagerMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView

class PubplansFragment : BaseFragment<PubplansMvp.View, PubplansPresenter>(),
		PubplansMvp.View {

	private val onLoadMore: OnLoadMore<Int> by lazy { OnLoadMore(presenter) }
	private val adapter: PlansAdapter by lazy { PlansAdapter(arrayListOf()) }
	private var publisherCallback: PlansPagerMvp.View? = null

	override fun fragmentLayout(): Int = R.layout.micro_grid_refresh_list

	override fun providePresenter(): PubplansPresenter = PubplansPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			stateLayout.hideProgress()
		}
		stateLayout.setEmptyText(R.string.no_responses)
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

	override fun onNotifyAdapter(items: ArrayList<Pubplans.Object>, page: Int) {
		hideProgress()
		if (items.isEmpty()) {
			adapter.clear()
			return
		}
		if (page <= 1) {
			adapter.insertItems(items)
		} else {
			adapter.addItems(items)
		}
	}

	override fun getLoadMore() = onLoadMore

	override fun onItemClicked(item: Pubplans.Object) {
		EditionPagerActivity.startActivity(context!!, item.editionId.toInt(), item.name, 0)
	}

	override fun onItemLongClicked(position: Int, v: View?, item: Pubplans.Object) {
	}

	override fun onRefresh() {
		presenter.getPubplans(1, true)
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
				presenter.setCurrentSort(PubplansSortOption.values()[position], null, null)
			}
			else -> {
				when (parent) {
					"lang" -> {
						presenter.setCurrentSort(null, item.id, null)
					}
					"publisher" -> {
						presenter.setCurrentSort(null, null, item.id)
					}
				}
			}
		}
	}

	fun showSortDialog() {
		val dialogView = ContextMenuDialogView()
		val sort = presenter.getCurrentSort()
		dialogView.initArguments("sort", ContextMenuBuilder.buildForPubplansSorting(recycler.context, sort.sortBy))
		dialogView.show(childFragmentManager, "ContextMenuDialogView")
	}

	fun showFilterDialog() {
		val dialogView = ContextMenuDialogView()
		val sort = presenter.getCurrentSort()
		dialogView.initArguments("filter", ContextMenuBuilder.buildForPubplansFilter(recycler.context, presenter.publishers, sort.filterLang, sort.filterPublisher))
		dialogView.show(childFragmentManager, "ContextMenuDialogView")
	}

	companion object {

		fun newInstance(): PubplansFragment {
			val view = PubplansFragment()
			return view
		}
	}
}