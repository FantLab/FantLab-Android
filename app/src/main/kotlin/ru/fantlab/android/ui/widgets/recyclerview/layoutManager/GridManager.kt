package ru.fantlab.android.ui.widgets.recyclerview.layoutManager

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

class GridManager : GridLayoutManager {

	var iconSize: Int = 0
		set(iconSize) {
			field = iconSize
			updateCount()
		}

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

	constructor(context: Context, spanCount: Int) : super(context, spanCount) {}

	constructor(context: Context, spanCount: Int, orientation: Int, reverseLayout: Boolean) : super(context, spanCount, orientation, reverseLayout) {}

	override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
		try {
			super.onLayoutChildren(recycler, state)
			updateCount()
		} catch (ignored: Exception) {
		}

	}

	override fun onMeasure(recycler: RecyclerView.Recycler?, state: RecyclerView.State?, widthSpec: Int, heightSpec: Int) {
		try {
			super.onMeasure(recycler, state, widthSpec, heightSpec)
		} catch (ignored: Exception) {
		}

	}

	private fun updateCount() {
		if (this.iconSize > 1) {
			var spanCount = Math.max(1, width / this.iconSize)
			if (spanCount < 1) {
				spanCount = 1
			}
			this.spanCount = spanCount
		}
	}
}