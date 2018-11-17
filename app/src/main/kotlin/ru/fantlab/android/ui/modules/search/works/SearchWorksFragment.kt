package ru.fantlab.android.ui.modules.search.works

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import butterknife.BindView
import com.evernote.android.state.State
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.SearchWork
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.SearchWorksAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.search.SearchMvp
import ru.fantlab.android.ui.modules.work.CyclePagerActivity
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller

class SearchWorksFragment : BaseFragment<SearchWorksMvp.View, SearchWorksPresenter>(),
		SearchWorksMvp.View {

	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
	@BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
	@BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller

	@State var searchQuery = ""
	private val onLoadMore: OnLoadMore<String> by lazy { OnLoadMore(presenter, searchQuery) }
	private val adapter: SearchWorksAdapter by lazy { SearchWorksAdapter(presenter.getWorks()) }
	private var countCallback: SearchMvp.View? = null

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			stateLayout.hideProgress()
		}
		stateLayout.setEmptyText(R.string.no_search_results)
		getLoadMore().initialize(presenter.getCurrentPage() - 1, presenter.getPreviousTotal())
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		adapter.listener = presenter
		recycler.adapter = adapter
		recycler.addKeyLineDivider()
		recycler.addOnScrollListener(getLoadMore())
		if (!InputHelper.isEmpty(searchQuery) && presenter.getWorks().isEmpty() && !presenter.isApiCalled()) {
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

	override fun onDestroyView() {
		recycler.removeOnScrollListener(getLoadMore())
		super.onDestroyView()
	}

	override fun providePresenter(): SearchWorksPresenter = SearchWorksPresenter()

	override fun onNotifyAdapter(items: ArrayList<SearchWork>, page: Int) {
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

	override fun onSetTabCount(count: Int) {
		countCallback?.onSetCount(count, 1)
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
		onSetSearchQuery(query)
	}

	override fun getLoadMore(): OnLoadMore<String> {
		onLoadMore.parameter = searchQuery
		return onLoadMore
	}

	override fun onItemClicked(item: SearchWork) {
		if (item.typeName == PrefGetter.WorkType.cycle.name)
			CyclePagerActivity.startActivity(context!!, item.id, item.rusName, 0)
		else
			WorkPagerActivity.startActivity(context!!, item.id, item.rusName, 0)
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
}