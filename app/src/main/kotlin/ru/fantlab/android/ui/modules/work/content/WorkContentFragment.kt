package ru.fantlab.android.ui.modules.work.content

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ChildWork
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.ui.adapter.WorkContentAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import ru.fantlab.android.ui.modules.work.WorkPagerMvp

class WorkContentFragment : BaseFragment<WorkContentMvp.View, WorkContentPresenter>(),
		WorkContentMvp.View {

	private var countCallback: WorkPagerMvp.View? = null
	private val adapter: WorkContentAdapter by lazy { WorkContentAdapter(arrayListOf()) }

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list

	override fun providePresenter() = WorkContentPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		stateLayout.setEmptyText(R.string.no_content)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		adapter.listener = presenter
		recycler.adapter = adapter
		presenter.onFragmentCreated(arguments!!)
		fastScroller.attachRecyclerView(recycler)
	}

	override fun onInitViews(content: ArrayList<ChildWork>) {
		hideProgress()
		adapter.insertItems(content)
		onSetTabCount(adapter.itemCount)
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

		fun newInstance(editionId: Int): WorkContentFragment {
			val view = WorkContentFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, editionId).end()
			return view
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

	override fun onSetTabCount(allCount: Int) {
		countCallback?.onSetBadge(1, allCount)
	}

	override fun onRefresh() {
		presenter.getContent(true)
	}

	override fun onClick(p0: View?) {
		onRefresh()
	}

	override fun onItemClicked(item: ChildWork) {
		if (item.id != null) {
			WorkPagerActivity.startActivity(
					context!!,
					item.id,
					if (!InputHelper.isEmpty(item.name)) item.name else item.nameOrig,
					0
			)
		}
	}
}