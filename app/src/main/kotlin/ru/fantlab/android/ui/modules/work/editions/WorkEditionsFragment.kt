package ru.fantlab.android.ui.modules.work.editions

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.EditionsBlocks
import ru.fantlab.android.data.dao.model.EditionsInfo
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.EditionsAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.edition.EditionPagerActivity
import ru.fantlab.android.ui.modules.work.WorkPagerMvp
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller

class WorkEditionsFragment : BaseFragment<WorkEditionsMvp.View, WorkEditionsPresenter>(),
		WorkEditionsMvp.View {

	private val adapter: EditionsAdapter by lazy { EditionsAdapter(arrayListOf()) }
	private var countCallback: WorkPagerMvp.View? = null

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list

	override fun providePresenter() = WorkEditionsPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			stateLayout.hideProgress()
		}
		stateLayout.setEmptyText(R.string.no_editions)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		adapter.listener = presenter
		recycler.adapter = adapter
		presenter.onFragmentCreated(arguments!!)
		fastScroller.attachRecyclerView(recycler)
	}

	override fun onInitViews(editions: EditionsBlocks?, editionsInfo: EditionsInfo) {
		hideProgress()
		onSetTabCount(editionsInfo.allCount)
		adapter.clear()
		editions?.editionsBlocks?.let {
			it.forEach {
				adapter.addItems(it.list)
			}
		}
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

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		refresh.isRefreshing = true
		stateLayout.showProgress()
	}

	override fun hideProgress() {
		refresh.isRefreshing = false
		stateLayout.showReload(adapter.itemCount)
	}

	override fun showErrorMessage(msgRes: String?) {
		hideProgress()
		super.showErrorMessage(msgRes)
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		hideProgress()
		super.showMessage(titleRes, msgRes)
	}

	companion object {

		fun newInstance(workId: Int): WorkEditionsFragment {
			val view = WorkEditionsFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, workId).end()
			return view
		}
	}

	override fun onRefresh() {
		presenter.getEditions(true)
	}

	override fun onItemClicked(item: EditionsBlocks.Edition) {
		EditionPagerActivity.startActivity(context!!, item.editionId, item.name, 0)
	}

	override fun onClick(v: View?) {
		onRefresh()
	}

	override fun onSetTabCount(allCount: Int) {
		countCallback?.onSetBadge(3, allCount)
	}
}