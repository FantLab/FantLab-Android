package ru.fantlab.android.ui.modules.search.editions

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import butterknife.BindView
import com.evernote.android.state.State
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.SearchEditionModel
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.SearchEditionsAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.search.SearchMvp
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller

class SearchEditionsFragment : BaseFragment<SearchEditionsMvp.View, SearchEditionsPresenter>(),
		SearchEditionsMvp.View {

	@BindView(R.id.recycler)
	lateinit var recycler: DynamicRecyclerView

	@BindView(R.id.refresh)
	lateinit var refresh: SwipeRefreshLayout

	@BindView(R.id.stateLayout)
	lateinit var stateLayout: StateLayout

	@BindView(R.id.fastScroller)
	lateinit var fastScroller: RecyclerViewFastScroller

	@State
	var searchQuery = ""

	private val onLoadMore: OnLoadMore<String> by lazy { OnLoadMore(presenter, searchQuery) }
	private val adapter: SearchEditionsAdapter by lazy { SearchEditionsAdapter(presenter.getEditions()) }
	private var countCallback: SearchMvp.View? = null

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			stateLayout.hideProgress()
		}
		stateLayout.setEmptyText(R.string.no_search_results)
		getLoadMore().initialize(presenter.getCurrentPage(), presenter.getPreviousTotal())
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		adapter.listener = presenter
		recycler.adapter = adapter
		recycler.addKeyLineDivider()
		if (!InputHelper.isEmpty(searchQuery) && presenter.getEditions().isEmpty() && !presenter.isApiCalled()) {
			onRefresh()
		}
		if (InputHelper.isEmpty(searchQuery)) {
			stateLayout.showEmptyState()
		}
		fastScroller.attachRecyclerView(recycler)
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		if (context is SearchMvp.View) {
			countCallback = context
		}
	}

	override fun onDetach() {
		countCallback = null
		super.onDetach()
	}

	override fun providePresenter(): SearchEditionsPresenter = SearchEditionsPresenter()

	override fun onNotifyAdapter(items: List<SearchEditionModel>?, page: Int) {
		hideProgress()
		if (items == null || items.isEmpty()) {
			adapter.clear()
			return
		}
		if (page <= 1) {
			adapter.insertItems(items)
		} else {
			adapter.addItems(items)
		}
	}

	override fun onSetTabCount(count: Int) {
		countCallback?.onSetCount(count, 2)
	}

	override fun onSetSearchQuery(query: String) {
		this.searchQuery = query
		getLoadMore().reset()
		adapter.clear()
		if (!InputHelper.isEmpty(query)) {
			recycler.removeOnScrollListener(getLoadMore())
			recycler.addOnScrollListener(getLoadMore())
			onRefresh()
		}
	}

	override fun onQueueSearch(query: String) {
		this.searchQuery = query
		view?.let {
			onSetSearchQuery(query)
		}
	}

	override fun getLoadMore(): OnLoadMore<String> {
		onLoadMore.parameter = searchQuery
		return onLoadMore
	}

	override fun onItemClicked(item: SearchEditionModel) {
		// todo переход на экран издания
		showMessage("Clicked", item.name)
	}

	override fun fragmentLayout(): Int = R.layout.micro_grid_refresh_list

	override fun onRefresh() {
		if (searchQuery.isEmpty()) {
			refresh.isRefreshing = false
			return
		}
		presenter.onCallApi(1, searchQuery)
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

	override fun showErrorMessage(msgRes: String) {
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
}