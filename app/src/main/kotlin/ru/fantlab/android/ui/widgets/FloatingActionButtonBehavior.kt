package ru.fantlab.android.ui.widgets

import android.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FloatingActionButtonBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<FloatingActionButton>(context, attrs) {
	private val toolbarHeight: Int

	init {
		this.toolbarHeight = getToolbarHeight(context)
	}

	override fun layoutDependsOn(parent: CoordinatorLayout, fab: FloatingActionButton, dependency: View): Boolean {
		return dependency is AppBarLayout
	}

	override fun onDependentViewChanged(parent: CoordinatorLayout, fab: FloatingActionButton, dependency: View): Boolean {
		if (dependency is AppBarLayout) {
			val lp = fab.layoutParams as CoordinatorLayout.LayoutParams
			val fabBottomMargin = lp.bottomMargin
			val distanceToScroll = fab.height + fabBottomMargin
			val ratio = dependency.getY() / toolbarHeight.toFloat()
			fab.translationY = -distanceToScroll * ratio
		}
		return true
	}

	fun getToolbarHeight(context: Context): Int {
		val styledAttributes = context.theme.obtainStyledAttributes(
				intArrayOf(R.attr.actionBarSize))
		val toolbarHeight = styledAttributes.getDimension(0, 0f).toInt()
		styledAttributes.recycle()

		return toolbarHeight
	}
}