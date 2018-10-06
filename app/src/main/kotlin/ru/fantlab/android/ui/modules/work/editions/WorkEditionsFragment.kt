package ru.fantlab.android.ui.modules.work.editions

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import butterknife.BindView
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
import timber.log.Timber

class WorkEditionsFragment : BaseFragment<WorkEditionsMvp.View, WorkEditionsPresenter>(),
		WorkEditionsMvp.View {

    private var workEditions: EditionsBlocks? = null
	private var workEditionsInfo: EditionsInfo? = null

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list
	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
    @BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
    @BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
    @BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller

    private val adapter: EditionsAdapter by lazy { EditionsAdapter(presenter.getEditions()) }
    private var countCallback: WorkPagerMvp.View? = null

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			workEditions = savedInstanceState.getParcelable("workEditions")
			workEditionsInfo = savedInstanceState.getParcelable("workEditionsInfo")
			if (workEditions != null && workEditionsInfo != null) {
				onInitViews(workEditions, workEditionsInfo!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = WorkEditionsPresenter()

	override fun onInitViews(editions: EditionsBlocks?, editionsInfo: EditionsInfo) {
		hideProgress()
        onSetTabCount(editionsInfo.allCount)
        stateLayout.setEmptyText(R.string.no_editions)
        stateLayout.setOnReloadListener(this)
        refresh.setOnRefreshListener(this)
        recycler.setEmptyView(stateLayout, refresh)
        recycler.addKeyLineDivider()
        adapter.listener = presenter
        fastScroller.attachRecyclerView(recycler)
        editions?.editionsBlocks?.let {
            it.forEach {
            adapter.addItems(it.list)
        } }
        recycler.adapter = adapter
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


	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelable("workEditions", workEditions)
		outState.putParcelable("workEditionsInfo", workEditionsInfo)
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

		fun newInstance(workId: Int): WorkEditionsFragment {
			val view = WorkEditionsFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, workId).end()
			return view
		}
	}

    override fun onRefresh() {
        presenter.onCallApi()
    }

    override fun onItemClicked(item: EditionsBlocks.Edition) {
        EditionPagerActivity.startActivity(context!!, item.editionId, item.name, 0)
    }

    override fun onClick(v: View?) {
        onRefresh()
    }

    override fun onNotifyAdapter() {
        hideProgress()
        adapter.notifyDataSetChanged()
    }

    override fun onSetTabCount(allCount: Int) {
        countCallback?.onSetBadge(3, allCount)
    }
}