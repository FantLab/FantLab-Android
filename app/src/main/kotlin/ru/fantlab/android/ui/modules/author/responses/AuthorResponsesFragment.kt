package ru.fantlab.android.ui.modules.author.responses

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
import ru.fantlab.android.ui.modules.author.AuthorPagerMvp
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller

class AuthorResponsesFragment : BaseFragment<AuthorResponsesMvp.View, AuthorResponsesPresenter>(),
		AuthorResponsesMvp.View {

	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
	@BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
	@BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller
	private var authorId: Int? = null
	private val onLoadMore: OnLoadMore<Int> by lazy { OnLoadMore(presenter, authorId) }
	private val adapter: ResponsesAdapter by lazy { ResponsesAdapter(presenter.getResponses()) }
	private var countCallback: AuthorPagerMvp.View? = null

	override fun fragmentLayout(): Int = R.layout.micro_grid_refresh_list

	override fun providePresenter(): AuthorResponsesPresenter = AuthorResponsesPresenter()

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
			authorId = arguments?.getInt(BundleConstant.EXTRA)
		} else {
			authorId = savedInstanceState.getInt(BundleConstant.EXTRA)
			if (authorId == null) {
				authorId = arguments?.getInt(BundleConstant.EXTRA)
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
		if (context is AuthorPagerMvp.View) {
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
		onLoadMore.parameter = authorId
		return onLoadMore
	}

	override fun onSetTabCount(count: Int) {
		countCallback?.onSetBadge(4, count)
	}

	override fun onItemClicked(item: Response) {
		// todo переход на экран отзыва
		showMessage("Click", "Not implemented yet")
	}

	override fun onRefresh() {
		presenter.onCallApi(1, authorId)
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

		fun newInstance(authorId: Int): AuthorResponsesFragment {
			val view = AuthorResponsesFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, authorId).end()
			return view
		}
	}
}