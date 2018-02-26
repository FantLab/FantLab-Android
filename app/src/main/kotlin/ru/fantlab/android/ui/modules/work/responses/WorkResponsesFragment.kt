package ru.fantlab.android.ui.modules.work.responses

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.ResponsesAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.work.WorkPagerMvp
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller

class WorkResponsesFragment : BaseFragment<WorkResponsesMvp.View, WorkResponsesPresenter>(),
		WorkResponsesMvp.View {

	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
	@BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
	@BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller
	private var workId: Int? = null
	private val onLoadMore: OnLoadMore<Int> by lazy { OnLoadMore(presenter, workId) }
	private val adapter: ResponsesAdapter by lazy { ResponsesAdapter(presenter.getResponses()) }
	private var countCallback: WorkPagerMvp.View? = null

	override fun fragmentLayout(): Int = R.layout.micro_grid_refresh_list

	override fun providePresenter(): WorkResponsesPresenter = WorkResponsesPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			stateLayout.hideProgress()
		}
		stateLayout.setEmptyText(R.string.no_responses)
		getLoadMore().initialize(presenter.getCurrentPage(), presenter.getPreviousTotal())
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		adapter.listener = presenter
		recycler.adapter = adapter
		recycler.addKeyLineDivider()
		if (savedInstanceState == null) {
			workId = arguments?.getInt(BundleConstant.EXTRA)
		} else {
			workId = savedInstanceState.getInt(BundleConstant.EXTRA)
			if (workId == null) {
				workId = arguments?.getInt(BundleConstant.EXTRA)
			}
		}
		recycler.addOnScrollListener(getLoadMore())
		if (presenter.getResponses().isEmpty() && !presenter.isApiCalled()) {
			onRefresh()
		}
		fastScroller.attachRecyclerView(recycler)
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		if (context is WorkPagerMvp.View) {
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

	override fun onNotifyAdapter(items: List<Response>?, page: Int) {
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

	override fun getLoadMore(): OnLoadMore<Int> {
		onLoadMore.parameter = workId
		return onLoadMore
	}

	override fun onSetTabCount(count: Int) {
		countCallback?.onSetBadge(2, count)
	}

	override fun onItemClicked(item: Response) {
		// todo переход на экран отзыва
		showMessage("Click", "Not implemented yet")
	}

	override fun onRefresh() {
		presenter.onCallApi(1, workId)
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

	companion object {

		fun newInstance(workId: Int): WorkResponsesFragment {
			val view = WorkResponsesFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, workId).end()
			return view
		}
	}
}