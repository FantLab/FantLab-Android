package ru.fantlab.android.ui.modules.work.responses

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.ContextMenuBuilder
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.WorkResponsesAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.editor.EditorActivity
import ru.fantlab.android.ui.modules.user.UserPagerActivity
import ru.fantlab.android.ui.modules.work.WorkPagerMvp
import ru.fantlab.android.ui.modules.work.responses.overview.ResponseOverviewActivity
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView

class WorkResponsesFragment : BaseFragment<WorkResponsesMvp.View, WorkResponsesPresenter>(),
		WorkResponsesMvp.View {

	private var workId = -1
	private val onLoadMore: OnLoadMore<Int> by lazy { OnLoadMore(presenter, workId) }
	private val adapter: WorkResponsesAdapter by lazy { WorkResponsesAdapter(arrayListOf()) }
	private var workCallback: WorkPagerMvp.View? = null

	override fun fragmentLayout(): Int = R.layout.micro_grid_refresh_list

	override fun providePresenter(): WorkResponsesPresenter = WorkResponsesPresenter()

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
		recycler.addNormalSpacingDivider()
		workId = arguments!!.getInt(BundleConstant.EXTRA)
		getLoadMore().initialize(presenter.getCurrentPage() - 1, presenter.getPreviousTotal())
		recycler.addOnScrollListener(getLoadMore())
		presenter.onCallApi(1, workId)
		fastScroller.attachRecyclerView(recycler)
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		if (context is WorkPagerMvp.View) {
			workCallback = context
		}
	}

	override fun onDetach() {
		workCallback = null
		super.onDetach()
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

	override fun onSetTabCount(count: Int) {
		workCallback?.onSetBadge(2, count)
	}

	override fun onItemClicked(item: Response) {
		ResponseOverviewActivity.startActivity(context!!, item)
	}

	override fun onItemLongClicked(position: Int, v: View?, item: Response) {
	}

	override fun onItemSelected(item: ContextMenus.MenuItem, listItem: Any, position: Int) {
		if (listItem is Response) when (item.id) {
			"vote" -> {
				presenter.onSendVote(listItem, position, if (item.title.contains("+")) "plus" else "minus")
			}
			"profile" -> {
				UserPagerActivity.startActivity(recycler.context, listItem.userName, listItem.userId, 0)
			}
			"message" -> {
				startActivity(Intent(activity, EditorActivity::class.java)
						.putExtra(BundleConstant.EXTRA_TYPE, BundleConstant.EDITOR_NEW_MESSAGE)
						.putExtra(BundleConstant.ID, listItem.userId)
				)
			}
		} else {
			presenter.setCurrentSort(item.id)
		}
	}

	override fun onRefresh() {
		presenter.getResponses(1, true)
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

	fun showSortDialog() {
		val dialogView = ContextMenuDialogView()
		dialogView.initArguments("main", ContextMenuBuilder.buildForResponseSorting(recycler.context))
		dialogView.show(childFragmentManager, "ContextMenuDialogView")
	}

	companion object {

		fun newInstance(workId: Int): WorkResponsesFragment {
			val view = WorkResponsesFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, workId).end()
			return view
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
}