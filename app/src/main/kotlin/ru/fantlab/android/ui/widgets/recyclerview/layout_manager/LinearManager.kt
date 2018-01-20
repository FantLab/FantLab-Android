package ru.fantlab.android.ui.widgets.recyclerview.layout_manager

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

/**
 * Created by Kosh on 17 May 2016, 10:02 PM
 */
class LinearManager : LinearLayoutManager {

	constructor(context: Context) : super(context)

	constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

	override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
		try {
			super.onLayoutChildren(recycler, state)
		} catch (ignored: Exception) {
		}

	}

	override fun onMeasure(recycler: RecyclerView.Recycler?, state: RecyclerView.State?, widthSpec: Int, heightSpec: Int) {
		try {
			super.onMeasure(recycler, state, widthSpec, heightSpec)
		} catch (ignored: Exception) {
		}
	}
}
