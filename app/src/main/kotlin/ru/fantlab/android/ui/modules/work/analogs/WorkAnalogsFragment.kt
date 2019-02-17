package ru.fantlab.android.ui.modules.work.analogs

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import kotlinx.android.synthetic.main.analogs_list.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.WorkAnalog
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.AnalogsAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import ru.fantlab.android.ui.modules.work.WorkPagerMvp
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView

class WorkAnalogsFragment : BaseFragment<WorkAnalogsMvp.View, WorkAnalogsPresenter>(),
		WorkAnalogsMvp.View {

	private val adapter: AnalogsAdapter by lazy { AnalogsAdapter(arrayListOf()) }
	private var countCallback: WorkPagerMvp.View? = null

	override fun fragmentLayout() = R.layout.analogs_list

	override fun providePresenter() = WorkAnalogsPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		presenter.onFragmentCreated(arguments!!)
	}

	override fun onInitViews(analogs: ArrayList<WorkAnalog>) {
		if (analogs.isEmpty()) {
			fragmentManager?.beginTransaction()?.remove(this)?.commit()
			return
		}
		fragmentManager?.beginTransaction()?.show(this)?.commit()
		recycler.addKeyLineDivider()
		recycler.isNestedScrollingEnabled = false
		adapter.listener = presenter
		recycler.adapter = adapter
		adapter.addItems(analogs)
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
	}

	override fun hideProgress() {
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
		val TAG: String = WorkAnalogsFragment::class.java.simpleName

		fun newInstance(workId: Int): WorkAnalogsFragment {
			val view = WorkAnalogsFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, workId).end()
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

	override fun onSetTabCount(count: Int) {
	}

	override fun onRefresh() {
		presenter.getAnalogs(true)
	}

	override fun onClick(p0: View?) {
		onRefresh()
	}

	override fun onItemClicked(item: WorkAnalog) {
		WorkPagerActivity.startActivity(context!!, item.id, item.name, 0)
	}
}