package ru.fantlab.android.ui.widgets.recyclerview

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.view.View
import ru.fantlab.android.R
import ru.fantlab.android.helper.ViewHelper
import ru.fantlab.android.ui.widgets.StateLayout

/**
 * Created by Kosh on 9/24/2015. copyrights are reserved
 *
 *
 * recyclerview which will showParentOrSelf/showParentOrSelf itself base on adapter
 */
class DynamicRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {

	private var emptyView: StateLayout? = null
	private var parentView: View? = null
	private var bottomPaddingDecoration: BottomPaddingDecoration? = null

	private val observer = object : RecyclerView.AdapterDataObserver() {
		override fun onChanged() {
			showEmptyView()
		}

		override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
			super.onItemRangeInserted(positionStart, itemCount)
			showEmptyView()
		}

		override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
			super.onItemRangeRemoved(positionStart, itemCount)
			showEmptyView()
		}
	}

	override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
		super.setAdapter(adapter)
		if (isInEditMode) return
		if (adapter != null) {
			adapter.registerAdapterDataObserver(observer)
			observer.onChanged()
		}
	}


	fun removeBottomDecoration() {
		bottomPaddingDecoration?.let {
			removeItemDecoration(it)
			bottomPaddingDecoration = null
		}
	}

	fun addDecoration() {
		bottomPaddingDecoration = BottomPaddingDecoration.with(context)
		addItemDecoration(bottomPaddingDecoration)
	}

	private fun showEmptyView() {
		val adapter = adapter
		if (adapter != null) {
			emptyView?.run {
				if (adapter.itemCount == 0) {
					showParentOrSelf(false)
				} else {
					showParentOrSelf(true)
				}
			}
		} else {
			emptyView?.run {
				showParentOrSelf(false)
			}
		}
	}

	private fun showParentOrSelf(showRecyclerView: Boolean) {
		parentView?.visibility = View.VISIBLE
		visibility = View.VISIBLE
		emptyView?.visibility = if (!showRecyclerView) View.VISIBLE else View.GONE
	}

	fun setEmptyView(emptyView: StateLayout, parentView: View? = null) {
		this.emptyView = emptyView
		this.parentView = parentView
		showEmptyView()
	}

	fun hideProgress(view: StateLayout) {
		view.hideProgress()
	}

	fun showProgress(view: StateLayout) {
		view.showProgress()
	}

	fun addKeyLineDivider() {
		if (canAddDivider()) {
			val resources = resources
			addItemDecoration(InsetDividerDecoration(resources.getDimensionPixelSize(R.dimen.divider_height),
					resources.getDimensionPixelSize(R.dimen.keyline_2), ViewHelper.getListDivider(context)))
		}
	}

	fun addDivider() {
		if (canAddDivider()) {
			val resources = resources
			addItemDecoration(InsetDividerDecoration(resources.getDimensionPixelSize(R.dimen.divider_height), 0,
					ViewHelper.getListDivider(context)))
		}
	}

	fun addNormalSpacingDivider() {
		addDivider()
	}

	fun addDivider(toDivide: Class<*>) {
		if (canAddDivider()) {
			val resources = resources
			addItemDecoration(InsetDividerDecoration(resources.getDimensionPixelSize(R.dimen.divider_height), 0,
					ViewHelper.getListDivider(context), toDivide))
		}
	}

	private fun canAddDivider(): Boolean {
		layoutManager?.let {
			return when (it) {
				is LinearLayoutManager -> true
				is GridLayoutManager -> (layoutManager as GridLayoutManager).spanCount == 1
				is StaggeredGridLayoutManager -> (layoutManager as StaggeredGridLayoutManager).spanCount == 1
				else -> false
			}
		}
		return false
	}
}
