package ru.fantlab.android.ui.modules.author.works

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.WorksBlocks
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.WorkAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.author.AuthorPagerMvp
import ru.fantlab.android.ui.modules.author.overview.AuthorOverviewFragment
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller
import timber.log.Timber

class AuthorWorksFragment : BaseFragment<AuthorWorksMvp.View, AuthorWorksPresenter>(),
		AuthorWorksMvp.View {

	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
	@BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
	@BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller

	private val adapter: WorkAdapter by lazy { WorkAdapter(presenter.getWorks()) }
    private var countCallback: AuthorPagerMvp.View? = null

	private var works: WorksBlocks? = null

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			works = savedInstanceState.getParcelable("works")
			if (works != null) {
				onInitViews(works!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = AuthorWorksPresenter()

	override fun onInitViews(works: WorksBlocks) {
		hideProgress()
		Timber.d("works: $works")
        stateLayout.setEmptyText(R.string.no_works)
        stateLayout.setOnReloadListener(this)
        refresh.setOnRefreshListener(this)
        recycler.setEmptyView(stateLayout, refresh)
        recycler.addKeyLineDivider()
        fastScroller.attachRecyclerView(recycler)
        adapter.listener = presenter
        recycler.adapter = adapter
        works.worksBlocks.forEach{
			adapter.addItems(it.list)
		}
        onSetTabCount(adapter.itemCount)
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelable("works", works)
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

		fun newInstance(authorId: Int): AuthorWorksFragment {
			val view = AuthorWorksFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, authorId).end()
			return view
		}
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

    override fun onSetTabCount(count: Int) {
        countCallback?.onSetBadge(1, count)
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

    override fun onItemClicked(item: WorksBlocks.Work) {
        WorkPagerActivity.startActivity(context!!, item.id!!, item.name, 0)
    }
}