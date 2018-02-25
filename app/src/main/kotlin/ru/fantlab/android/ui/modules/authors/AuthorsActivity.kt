package ru.fantlab.android.ui.modules.authors

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.AuthorInList
import ru.fantlab.android.ui.adapter.AuthorsAdapter
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller

class AuthorsActivity : BaseActivity<AuthorsMvp.View, AuthorsPresenter>(), AuthorsMvp.View {

	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
	@BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
	@BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller
	private val adapter: AuthorsAdapter by lazy { AuthorsAdapter(presenter.getAuthors()) }

	override fun layout(): Int = R.layout.authors_layout

	override fun isTransparent(): Boolean = false

	override fun canBack(): Boolean = true

	override fun providePresenter(): AuthorsPresenter = AuthorsPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		hideShowShadow(true)
		selectMenuItem(R.id.bibliographies)
		if (savedInstanceState == null) {
			stateLayout.hideProgress()
		}
		stateLayout.setEmptyText(R.string.no_results)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		adapter.listener = presenter
		recycler.adapter = adapter
		recycler.addKeyLineDivider()
		if (presenter.getAuthors().isEmpty() && !presenter.isApiCalled()) {
			presenter.onReload()
		}
		fastScroller.attachRecyclerView(recycler)
	}

	override fun onNotifyAdapter(items: List<AuthorInList>?) {
		hideProgress()
		if (items == null || items.isEmpty()) {
			adapter.clear()
			return
		}
		adapter.insertItems(items)
	}

	override fun onItemClicked(item: AuthorInList) {
		// todo переход на экран автора
		showMessage("Click", "Not implemented yet")
	}

	override fun onRefresh() {
		presenter.onReload()
	}

	override fun onClick(v: View?) {
		onRefresh()
	}

	override fun hideProgress() {
		refresh.isRefreshing = false
		stateLayout.hideProgress()
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		refresh.isRefreshing = true
		stateLayout.showProgress()
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		showReload()
		super.showMessage(titleRes, msgRes)
	}

	private fun showReload() {
		hideProgress()
		stateLayout.showReload(adapter.itemCount)
	}
}