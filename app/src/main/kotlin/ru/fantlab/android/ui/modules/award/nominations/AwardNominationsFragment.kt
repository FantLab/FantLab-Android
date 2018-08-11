package ru.fantlab.android.ui.modules.award.nominations

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Award
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.AwardNominationsAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.award.AwardPagerMvp
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller

class AwardNominationsFragment : BaseFragment<AwardNominationsMvp.View, AwardNominationsPresenter>(),
		AwardNominationsMvp.View {

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list

	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
	@BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
	@BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller

    private val adapter: AwardNominationsAdapter by lazy { AwardNominationsAdapter(presenter.getAwardNominations()) }
    private var countCallback: AwardPagerMvp.View? = null
	private var awardId: Int? = null

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			hideProgress()
			awardId = arguments?.getInt(BundleConstant.EXTRA)
		}
		stateLayout.setEmptyText(R.string.no_nominations)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		recycler.addDivider()
		adapter.listener = presenter
		fastScroller.attachRecyclerView(recycler)
		recycler.adapter = adapter
		if (presenter.getAwardNominations().isEmpty() && !presenter.isApiCalled()) {
			onRefresh()
		}
	}

	override fun providePresenter() = AwardNominationsPresenter()

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

		fun newInstance(awardId: Int): AwardNominationsFragment {
			val view = AwardNominationsFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, awardId).end()
			return view
		}
	}

    override fun onRefresh() {
        presenter.onCallApi(awardId)
    }

    override fun onClick(v: View?) {
        onRefresh()
    }

    override fun onNotifyAdapter(items: List<Award.Nominations>?) {
		hideProgress()
		if (items != null){
			if (items.isEmpty()) {
				adapter.clear()
				return
			}
			adapter.addItems(items)
		}
	}

    override fun onSetTabCount(allCount: Int) {
        countCallback?.onSetBadge(2, allCount)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is AwardPagerMvp.View) {
            countCallback = context
        }
    }

    override fun onDetach() {
        countCallback = null
        super.onDetach()
    }

	override fun onItemClicked(item: Award.Nominations) {
	}
}