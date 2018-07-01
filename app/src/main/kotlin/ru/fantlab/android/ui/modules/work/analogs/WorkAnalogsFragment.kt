package ru.fantlab.android.ui.modules.work.analogs

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import butterknife.BindView
import com.google.gson.GsonBuilder
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.WorkAnalog
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.AnalogsAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.work.WorkPagerMvp
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller
import timber.log.Timber

class WorkAnalogsFragment : BaseFragment<WorkAnalogsMvp.View, WorkAnalogsPresenter>(),
		WorkAnalogsMvp.View {

    @BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
    @BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
    @BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
    @BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller

	private var workAnalogs: ArrayList<WorkAnalog>? = null
    private val adapter: AnalogsAdapter by lazy { AnalogsAdapter(presenter.getAnalogs()) }
    private var countCallback: WorkPagerMvp.View? = null

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			workAnalogs = savedInstanceState.getParcelableArrayList("workAnalogs")
			if (workAnalogs != null) {
				onInitViews(workAnalogs!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = WorkAnalogsPresenter()

	override fun onInitViews(analogs: ArrayList<WorkAnalog>) {
		hideProgress()
		Timber.d("analogs: ${GsonBuilder().setPrettyPrinting().create().toJson(analogs)}")
		stateLayout.setEmptyText(R.string.no_analogs)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		recycler.addKeyLineDivider()
        fastScroller.attachRecyclerView(recycler)
        recycler.adapter = adapter
        adapter.addItems(analogs)
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelableArrayList("workAnalogs", workAnalogs)
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
		hideProgress()
		super.showErrorMessage(msgRes)
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		hideProgress()
		super.showMessage(titleRes, msgRes)
	}

	companion object {

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
        countCallback?.onSetBadge(4, count)
    }

    override fun onRefresh() {
        presenter.onCallApi()
    }

    override fun onNotifyAdapter() {
        hideProgress()
        adapter.notifyDataSetChanged()
    }

    override fun onClick(p0: View?) {
        onRefresh()
    }
}