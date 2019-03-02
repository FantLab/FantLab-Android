package ru.fantlab.android.ui.modules.publishers

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.publishers_layout.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.ContextMenuBuilder
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.data.dao.model.Publishers
import ru.fantlab.android.provider.rest.PublishersSortOption
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.PublishersAdapter
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView


class PublishersActivity : BaseActivity<PublishersMvp.View, PublishersPresenter>(), PublishersMvp.View {

	private val adapter: PublishersAdapter by lazy { PublishersAdapter(arrayListOf()) }

	private val onLoadMore: OnLoadMore<Int> by lazy { OnLoadMore(presenter) }

	override fun layout(): Int = R.layout.publishers_layout

	override fun isTransparent(): Boolean = false

	override fun canBack(): Boolean = true

	override fun providePresenter(): PublishersPresenter = PublishersPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		title = getString(R.string.publishers)
		hideShowShadow(true)
		selectMenuItem(R.id.publishers, true)
		if (savedInstanceState == null) {
			stateLayout.hideProgress()
		}
		stateLayout.setEmptyText(R.string.no_publishers)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		adapter.listener = presenter
		recycler.adapter = adapter
		recycler.addDivider()
		presenter.onCallApi(1, null)
		fastScroller.attachRecyclerView(recycler)
		recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				onScrolled(dy > 0)
			}
		})
		fab.setImageResource(R.drawable.ic_filter)
		fab.show()
		fab.setOnClickListener { onFabClicked() }
	}

	fun onScrolled(isUp: Boolean) {
		if (isUp) {
			fab.hide()
		} else {
			fab.show()
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.publishers_menu, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.sort -> {
				showSortDialog()
			}
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onItemSelected(parent: String, item: ContextMenus.MenuItem, position: Int, listItem: Any) {
		recycler?.scrollToPosition(0)
		when (item.id) {
			"sort" -> {
				presenter.setCurrentSort(PublishersSortOption.values()[position], null, null)
			}
			else -> {
				when (parent) {
					"countries" -> {
						presenter.setCurrentSort(null, item.id.toInt(), null)
					}
					"category" -> {
						presenter.setCurrentSort(null, null, item.id.toInt())
					}
				}
			}
		}
	}

	override fun onNotifyAdapter(items: ArrayList<Publishers.Publisher>, page: Int) {
		hideProgress()
		if (items.isEmpty()) {
			adapter.clear()
			return
		}
		if (page <= 1) {
			adapter.insertItems(items)
		} else {
			adapter.addItems(items)
		}
	}

	override fun getLoadMore() = onLoadMore

	override fun onItemClicked(item: Publishers.Publisher) {
		showErrorMessage("Not implemented currently")
	}

	override fun onItemLongClicked(position: Int, v: View?, item: Publishers.Publisher) {
	}

	override fun onRefresh() {
		presenter.getPublishers(1, true)
	}

	override fun onClick(v: View?) {
		onRefresh()
	}

	private fun onFabClicked() {
		showFilterDialog()
	}

	fun showSortDialog() {
		val dialogView = ContextMenuDialogView()
		val sort = presenter.getCurrentSort()
		dialogView.initArguments("sort", ContextMenuBuilder.buildForPublishersSorting(recycler.context, sort.sortBy))
		dialogView.show(supportFragmentManager, "ContextMenuDialogView")
	}

	fun showFilterDialog() {
		val dialogView = ContextMenuDialogView()
		val sort = presenter.getCurrentSort()
		dialogView.initArguments("filter", ContextMenuBuilder.buildForPublishersFilter(recycler.context, sort.filterCategory, sort.filterCountry))
		dialogView.show(supportFragmentManager, "ContextMenuDialogView")
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