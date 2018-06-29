package ru.fantlab.android.ui.modules.main.responses

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.ResponsesAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller

class ResponsesFragment : BaseFragment<ResponsesMvp.View, ResponsesPresenter>(), ResponsesMvp.View {

	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
	@BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
	@BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller
	private val adapter: ResponsesAdapter by lazy { ResponsesAdapter(presenter.responses) }
	private val onLoadMore: OnLoadMore<String> by lazy { OnLoadMore(presenter) }

	override fun fragmentLayout(): Int = R.layout.micro_grid_refresh_list

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		stateLayout.setEmptyText(R.string.no_responses)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		adapter.listener = presenter
		recycler.adapter = adapter
		getLoadMore().initialize(presenter.getCurrentPage(), presenter.getPreviousTotal())
		recycler.addOnScrollListener(getLoadMore())
		fastScroller.attachRecyclerView(recycler)
		if (presenter.responses.isEmpty() && !presenter.isApiCalled()) {
			presenter.onFragmentCreated()
		}
	}

	override fun onDestroyView() {
		recycler.removeOnScrollListener(getLoadMore())
		super.onDestroyView()
	}

	override fun providePresenter(): ResponsesPresenter = ResponsesPresenter()

	override fun onNotifyAdapter(items: ArrayList<Response>, page: Int) {
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

	override fun onClick(v: View?) {
		onRefresh()
	}

	override fun onRefresh() {
		presenter.onCallApi(1)
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		refresh.isRefreshing = true
		stateLayout.showProgress()
	}

	override fun hideProgress() {
		refresh.isRefreshing = false
		stateLayout.hideProgress()
	}

	override fun showErrorMessage(msgRes: String) {
		showReload()
		super.showErrorMessage(msgRes)
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		showReload()
		super.showMessage(titleRes, msgRes)
	}

	private fun showReload() {
		hideProgress()
		stateLayout.showReload(adapter.itemCount)
	}

	override fun onScrollTop(index: Int) {
		super.onScrollTop(index)
		recycler.scrollToPosition(0)
	}

	companion object {

		val TAG: String = ResponsesFragment::class.java.simpleName
	}

	override fun onItemClicked(item: Response) {
		WorkPagerActivity.startActivity(context!!, item.workId, item.workName, 0)
	}
}