package ru.fantlab.android.ui.widgets.recyclerview.scroll

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter

abstract class InfiniteScroll : RecyclerView.OnScrollListener() {

	private var visibleThreshold = 3
	private var currentPage = 0
	private var previousTotalItemCount = 0
	private var loading = true
	private val startingPageIndex = 0
	private var layoutManager: RecyclerView.LayoutManager? = null
	private var adapter: BaseRecyclerAdapter<*, *>? = null
	private var newlyAdded = true
	private var isUp = true
	private var menuShowed = false

	abstract fun onLoadMore(page: Int, totalItemsCount: Int): Boolean

	private var listener: OnScrollResumed? = null

	interface OnScrollResumed {
		fun onHideMenu()
		fun onScrolled(isUp: Boolean)
	}

	fun setOnScrollListener(listener: OnScrollResumed) {
		this.listener = listener
	}

	private fun initLayoutManager(layoutManager: RecyclerView.LayoutManager) {
		this.layoutManager = layoutManager
		if (layoutManager is GridLayoutManager) {
			visibleThreshold *= layoutManager.spanCount
		} else if (layoutManager is StaggeredGridLayoutManager) {
			visibleThreshold *= layoutManager.spanCount
		}
	}

	private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
		var maxSize = 0
		for (i in lastVisibleItemPositions.indices) {
			if (i == 0) {
				maxSize = lastVisibleItemPositions[i]
			} else if (lastVisibleItemPositions[i] > maxSize) {
				maxSize = lastVisibleItemPositions[i]
			}
		}
		return maxSize
	}

	override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
		if (newlyAdded) {
			newlyAdded = false
			return
		}
		listener?.onScrolled(dy > 0)
		if (isUp && menuShowed) {
			menuShowed = false
			listener?.onHideMenu()
		}

		if (layoutManager == null) {
			initLayoutManager(recyclerView!!.layoutManager)
		}
		if (adapter == null) {
			if (recyclerView!!.adapter is BaseRecyclerAdapter<*, *>) {
				adapter = recyclerView.adapter as BaseRecyclerAdapter<*, *>
			}
		}
		if (adapter != null && adapter!!.isProgressAdded()) return

		var lastVisibleItemPosition = 0
		val totalItemCount = layoutManager!!.itemCount
		when (layoutManager) {
			is StaggeredGridLayoutManager -> {
				val lastVisibleItemPositions = (layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
				lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
			}
			is GridLayoutManager -> lastVisibleItemPosition = (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
			is LinearLayoutManager -> lastVisibleItemPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
		}
		if (totalItemCount < previousTotalItemCount) {
			this.currentPage = this.startingPageIndex
			this.previousTotalItemCount = totalItemCount
			if (totalItemCount == 0) {
				this.loading = true
			}
		}
		if (loading && totalItemCount > previousTotalItemCount) {
			loading = false
			previousTotalItemCount = totalItemCount
		}
		if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
			currentPage++
			val isCallingApi = onLoadMore(currentPage, totalItemCount)
			loading = true
			if (isCallingApi) {
				adapter?.addProgress()
			}
		}
	}

	fun reset() {
		this.currentPage = this.startingPageIndex
		this.previousTotalItemCount = 0
		this.loading = true
	}

	fun initialize(page: Int, previousTotal: Int) {
		this.currentPage = page
		this.previousTotalItemCount = previousTotal
		this.loading = true
	}

	fun setMenuShowed(menuShowed: Boolean) {
		this.menuShowed = menuShowed
	}
}