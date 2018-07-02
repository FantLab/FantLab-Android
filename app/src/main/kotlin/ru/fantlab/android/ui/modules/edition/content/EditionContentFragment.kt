package ru.fantlab.android.ui.modules.edition.content

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.EditionContentAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.edition.EditionPagerMvp
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller
import timber.log.Timber

class EditionContentFragment : BaseFragment<EditionContentMvp.View, EditionContentPresenter>(),
		EditionContentMvp.View {

	private var content: ArrayList<String>? = null
	private val adapter: EditionContentAdapter by lazy { EditionContentAdapter(presenter.getContent()) }

    override fun fragmentLayout() = R.layout.micro_grid_refresh_list
    @BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
    @BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
    @BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
    @BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller

    private var countCallback: EditionPagerMvp.View? = null

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			//content = savedInstanceState.getParcelableArrayList<String>("content")
			if (content != null) {
				onInitViews(content!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = EditionContentPresenter()

	override fun onInitViews(content: ArrayList<String>) {
		hideProgress()
		Timber.d("content: $content")
        stateLayout.setEmptyText(R.string.no_content)
        stateLayout.setOnReloadListener(this)
        refresh.setOnRefreshListener(this)
        recycler.setEmptyView(stateLayout, refresh)
        recycler.addDivider()
        fastScroller.attachRecyclerView(recycler)
        recycler.adapter = adapter
        adapter.addItems(content)
        onSetTabCount(adapter.itemCount)
    }

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		//outState.putParcelableArrayList("content", content)
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

		fun newInstance(editionId: Int): EditionContentFragment {
			val view = EditionContentFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, editionId).end()
			return view
		}
	}

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is EditionPagerMvp.View) {
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