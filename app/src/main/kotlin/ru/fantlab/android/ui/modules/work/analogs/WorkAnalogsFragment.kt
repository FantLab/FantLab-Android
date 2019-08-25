package ru.fantlab.android.ui.modules.work.analogs

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.WorkAnalog
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.AnalogsAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import ru.fantlab.android.ui.modules.work.WorkPagerMvp

class WorkAnalogsFragment : BaseFragment<WorkAnalogsMvp.View, WorkAnalogsPresenter>(),
		WorkAnalogsMvp.View {

	private val adapter: AnalogsAdapter by lazy { AnalogsAdapter(arrayListOf()) }
	private var pagerCallback: WorkPagerMvp.View? = null

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list

	override fun providePresenter() = WorkAnalogsPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			stateLayout.hideProgress()
		}
		stateLayout.setEmptyText(R.string.no_analogs)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		adapter.listener = presenter
		recycler.adapter = adapter
		fastScroller.attachRecyclerView(recycler)
		presenter.onFragmentCreated(arguments)
	}

	override fun onInitViews(analogs: ArrayList<WorkAnalog>) {
		hideProgress()
		adapter.addItems(analogs)
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		refresh.isRefreshing = true
		stateLayout.showProgress()
	}

	override fun hideProgress() {
		refresh.isRefreshing = false
		stateLayout.hideProgress()
	}

	companion object {
		val TAG: String = WorkAnalogsFragment::class.java.simpleName

		fun newInstance(workId: Int): WorkAnalogsFragment {
			val view = WorkAnalogsFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, workId).end()
			return view
		}
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (context is WorkPagerMvp.View) {
			pagerCallback = context
		}
	}

	override fun onDetach() {
		pagerCallback = null
		super.onDetach()
	}

	override fun onSetTabCount(count: Int) {
		pagerCallback?.onSetBadge(2, count)
	}

	override fun onRefresh() {
		presenter.getAnalogs(true)
	}

	override fun onNotifyAdapter() {
		hideProgress()
		adapter.notifyDataSetChanged()
	}

	override fun onClick(p0: View?) {
		onRefresh()
	}

	override fun onItemClicked(item: WorkAnalog) {
		WorkPagerActivity.startActivity(context!!, item.id, item.name, 0)
	}
}