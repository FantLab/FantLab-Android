package ru.fantlab.android.ui.modules.main.responses

import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.ContextMenuBuilder
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.ResponsesAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.editor.EditorActivity
import ru.fantlab.android.ui.modules.user.UserPagerActivity
import ru.fantlab.android.ui.modules.work.responses.overview.ResponseOverviewActivity
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller

class ResponsesFragment : BaseFragment<ResponsesMvp.View, ResponsesPresenter>(), ResponsesMvp.View {

	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
	@BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
	@BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller

	private val adapter: ResponsesAdapter by lazy { ResponsesAdapter(arrayListOf()) }
	private val onLoadMore: OnLoadMore<String> by lazy { OnLoadMore(presenter) }

	override fun fragmentLayout(): Int = R.layout.micro_grid_refresh_list

	override fun providePresenter(): ResponsesPresenter = ResponsesPresenter()

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
		presenter.onFragmentCreated()
	}

	override fun onDestroyView() {
		recycler.removeOnScrollListener(getLoadMore())
		super.onDestroyView()
	}


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

	override fun showErrorMessage(msgRes: String?) {
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
		ResponseOverviewActivity.startActivity(context!!, item)
	}

	override fun onItemLongClicked(position: Int, v: View?, item: Response) {
		val dialogView = ContextMenuDialogView()
		dialogView.initArguments("main", ContextMenuBuilder.buildForResponses(recycler.context), item, position)
		dialogView.show(childFragmentManager, "ContextMenuDialogView")
	}

	override fun onItemSelected(item: ContextMenus.MenuItem, listItem: Any, position: Int) {
		if (listItem is Response) when (item.id) {
			"vote" -> {
				if (isLoggedIn()) {
					if (PrefGetter.getLoggedUser()?.id != listItem.userId)
						presenter.onSendVote(listItem, position, if (item.title.contains("+")) "plus" else "minus")
					else
						showErrorMessage(getString(R.string.cannotvote))
				} else showErrorMessage(getString(R.string.unauthorized_user))
			}
			"profile" -> {
				UserPagerActivity.startActivity(context!!, listItem.userName, listItem.userId, 0)
			}
			"message" -> {
				startActivity(Intent(activity, EditorActivity::class.java)
						.putExtra(BundleConstant.EXTRA_TYPE, BundleConstant.EDITOR_NEW_MESSAGE)
						.putExtra(BundleConstant.ID, listItem.userId)
				)
			}
		}
	}

	override fun onSetVote(position: Int, votesCount: String) {
		hideProgress()
		adapter.getItem(position).voteCount = votesCount.toInt()
		adapter.notifyItemChanged(position)

	}

	override fun onOpenContextMenu(userItem: Response) {
		val dialogView = ContextMenuDialogView()
		dialogView.initArguments("main", ContextMenuBuilder.buildForProfile(recycler.context), userItem, 0)
		dialogView.show(childFragmentManager, "ContextMenuDialogView")
	}

	override fun onStart() {
		if (presenter != null) adapter.setOnContextMenuListener(this)
		super.onStart()
	}
}