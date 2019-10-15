package ru.fantlab.android.ui.widgets.recyclerview.scroll

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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
	private var isPageCounter = false
	private var totalPagesCount = 0

	abstract fun onLoadMore(page: Int, totalItemsCount: Int): Boolean

	private var listener: OnScrollResumed? = null

	interface OnScrollResumed {
		fun onScrolled(isUp: Boolean)
	}

	private fun initLayoutManager(layoutManager: RecyclerView.LayoutManager) {
		this.layoutManager = layoutManager
		if (layoutManager is GridLayoutManager) {
			visibleThreshold *= layoutManager.spanCount
		} else if (layoutManager is StaggeredGridLayoutManager) {
			visibleThreshold *= layoutManager.spanCount
		}
	}

	override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
		super.onScrollStateChanged(recyclerView, newState)
		if (newState == RecyclerView.SCROLL_STATE_IDLE) {
			val canBottomScroll = recyclerView.canScrollVertically(1)
			if (!canBottomScroll) { onScrolled(recyclerView, 0, 1) }
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

	override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
		if (newlyAdded) {
			newlyAdded = false
			return
		}
		listener?.onScrolled(dy > 0)

		if (layoutManager == null) {
			initLayoutManager(recyclerView.layoutManager!!)
		}
		if (adapter == null) {
			if (recyclerView.adapter is BaseRecyclerAdapter<*, *>) {
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
		println("loading: $loading, s1: ${lastVisibleItemPosition + visibleThreshold}, s2: $totalItemCount")
		if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {

			if (isPageCounter) {
				if (currentPage+1 == totalPagesCount) {
					return
				}
			}
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
		this.isPageCounter = false
		this.currentPage = page
		this.previousTotalItemCount = previousTotal
		this.loading = true
	}

	fun setTotalPagesCount(totalPagesCount: Int) {
		this.isPageCounter = true
		this.totalPagesCount = totalPagesCount
	}

}