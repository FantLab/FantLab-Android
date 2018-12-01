package ru.fantlab.android.ui.modules.awards

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import android.view.MenuItem
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.ContextMenuBuilder
import ru.fantlab.android.data.dao.model.AwardInList
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.provider.rest.AwardsSortOption
import ru.fantlab.android.ui.adapter.AwardsAdapter
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.modules.award.AwardPagerActivity
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller


class AwardsActivity : BaseActivity<AwardsMvp.View, AwardsPresenter>(), AwardsMvp.View {

	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
	@BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
	@BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller

	private val adapter: AwardsAdapter by lazy { AwardsAdapter(arrayListOf()) }

	private lateinit var toolbarMenu: Menu

	override fun layout(): Int = R.layout.awards_layout

	override fun isTransparent(): Boolean = false

	override fun canBack(): Boolean = true

	override fun providePresenter(): AwardsPresenter = AwardsPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		title = getString(R.string.awards_list)
		hideShowShadow(true)
		selectMenuItem(R.id.awards, true)
		if (savedInstanceState == null) {
			stateLayout.hideProgress()
		}
		stateLayout.setEmptyText(R.string.no_results)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		adapter.listener = presenter
		recycler.adapter = adapter
		recycler.addDivider()
		presenter.getAwards(false)
		fastScroller.attachRecyclerView(recycler)
	}

	fun showSortDialog() {
		val dialogView = ContextMenuDialogView()
		dialogView.initArguments("main", ContextMenuBuilder.buildForAwardsSorting(recycler.context))
		dialogView.show(supportFragmentManager, "ContextMenuDialogView")
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.awards_menu, menu)
		toolbarMenu = menu
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

	override fun onItemSelected(item: ContextMenus.MenuItem, listItem: Any, position: Int) {
		presenter.sort = AwardsSortOption.valueOf(item.id)
	}

	override fun onNotifyAdapter(items: ArrayList<AwardInList>) {
		hideProgress()
		if (items.isEmpty()) {
			adapter.clear()
			return
		}
		adapter.insertItems(items)
	}

	override fun onItemClicked(item: AwardInList) {
		val title = if (!item.nameRus.isEmpty()) {
			if (!item.nameOrig.isEmpty()) {
				String.format("%s / %s", item.nameRus, item.nameOrig)
			} else {
				item.nameRus
			}
		} else {
			item.nameOrig
		}
		AwardPagerActivity.startActivity(this, item.id, title, 0)
	}

	override fun onRefresh() {
		presenter.getAwards(true)
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