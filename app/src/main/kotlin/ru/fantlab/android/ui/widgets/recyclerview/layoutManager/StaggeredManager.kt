package ru.fantlab.android.ui.widgets.recyclerview.layoutManager

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet

/**
 * Created by Kosh on 17 May 2016, 10:02 PM
 */
class StaggeredManager : StaggeredGridLayoutManager {

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

	constructor(spanCount: Int, orientation: Int) : super(spanCount, orientation)

	override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
		try {
			super.onLayoutChildren(recycler, state)
		} catch (ignored: Exception) {
			// do nothing
		}
	}

	override fun onMeasure(recycler: RecyclerView.Recycler?, state: RecyclerView.State?, widthSpec: Int, heightSpec: Int) {
		try {
			super.onMeasure(recycler, state, widthSpec, heightSpec)
		} catch (ignored: Exception) {
			// do nothing
		}
	}
}
