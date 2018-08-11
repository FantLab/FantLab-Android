package ru.fantlab.android.ui.modules.awards

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.ContextMenuBuilder
import ru.fantlab.android.data.dao.model.AwardInList
import ru.fantlab.android.ui.adapter.AwardsAdapter
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.widgets.SortView
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller
import android.support.v7.widget.RecyclerView
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.ui.modules.award.AwardPagerActivity


class AwardsActivity : BaseActivity<AwardsMvp.View, AwardsPresenter>(), AwardsMvp.View {

	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
	@BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
	@BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller
	private val adapter: AwardsAdapter by lazy { AwardsAdapter(presenter.getAwards()) }
	lateinit var sortButton : Button
	@BindView(R.id.sortview) lateinit var sortView: SortView

	override fun layout(): Int = R.layout.awards_layout

	override fun isTransparent(): Boolean = false

	override fun canBack(): Boolean = true

	override fun providePresenter(): AwardsPresenter = AwardsPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
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
		if (presenter.getAwards().isEmpty() && !presenter.isApiCalled()) {
			presenter.onReload()
		}
		fastScroller.attachRecyclerView(recycler)


		val menuView = LayoutInflater.from(this).inflate(R.layout.sort_view, null)
		sortButton = menuView.findViewById(R.id.sortButton)
		sortButton.setOnClickListener {
			val dialogView = ContextMenuDialogView()
			dialogView.initArguments("main", ContextMenuBuilder.buildForAwardsSorting())
			dialogView.show(supportFragmentManager, "ContextMenuDialogView")
		}
		sortView.setHeaderView(menuView)

		refresh.isEnabled = false

		recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
				if (dy > 0) {
					sortView.closeMenu()
				}
				super.onScrolled(recyclerView, dx, dy)
			}
		})

		sortView.setOnSortViewListener(this)
	}

	override fun onMenuStateChanged(isOpened: Boolean) {
		refresh.isEnabled = isOpened
	}

	override fun onItemSelected(item: ContextMenus.MenuItem, listItem: Any, position: Int) {
			sortButton.text = StringBuilder()
					.append(getString(R.string.sort_mode))
					.append(" ")
					.append(item.title.toLowerCase())
			presenter.setCurrentSort(item.id)
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