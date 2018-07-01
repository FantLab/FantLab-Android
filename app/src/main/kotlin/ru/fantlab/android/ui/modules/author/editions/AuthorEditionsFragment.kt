package ru.fantlab.android.ui.modules.author.editions

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Author
import ru.fantlab.android.data.dao.response.AuthorEditionsResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.EditionsAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.author.AuthorPagerMvp
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller
import timber.log.Timber

class AuthorEditionsFragment : BaseFragment<AuthorEditionsMvp.View, AuthorEditionsPresenter>(),
		AuthorEditionsMvp.View {

	private var author: Author? = null

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list
	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
	@BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
	@BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller

    private val adapter: EditionsAdapter by lazy { EditionsAdapter(presenter.getEditions()) }
    private var countCallback: AuthorPagerMvp.View? = null

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			author = savedInstanceState.getParcelable("author")
			if (author != null) {
				//onInitViews(author!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = AuthorEditionsPresenter()

	override fun onInitViews(authorEditionsResponse: AuthorEditionsResponse) {
		hideProgress()
		Timber.d("author editions response: $authorEditionsResponse")
		onSetTabCount(authorEditionsResponse.editionsInfo.allCount)
		stateLayout.setEmptyText(R.string.no_editions)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		recycler.addKeyLineDivider()
		fastScroller.attachRecyclerView(recycler)
        authorEditionsResponse.editions?.editionsBlocks?.let {
			it.forEach {
				adapter.addItems(it.list)
			} }
		recycler.adapter = adapter
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelable("author", author)
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

		fun newInstance(authorId: Int): AuthorEditionsFragment {
			val view = AuthorEditionsFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, authorId).end()
			return view
		}
	}

    override fun onRefresh() {
        presenter.onCallApi()
    }

    override fun onClick(v: View?) {
        onRefresh()
    }

    override fun onNotifyAdapter() {
        hideProgress()
        adapter.notifyDataSetChanged()
    }

    override fun onSetTabCount(allCount: Int) {
        countCallback?.onSetBadge(2, allCount)
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
}