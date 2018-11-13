package ru.fantlab.android.ui.widgets

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.View
import it.sephiroth.android.library.bottomnavigation.BottomNavigation
import it.sephiroth.android.library.bottomnavigation.VerticalScrollingBehavior

class TabletBehavior : VerticalScrollingBehavior<BottomNavigation> {

	constructor() : super()

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

	fun setLayoutValues(bottomNavWidth: Int, topInset: Int, translucentStatus: Boolean) {}

	override fun layoutDependsOn(parent: CoordinatorLayout?, child: BottomNavigation?, dependency: View?): Boolean {
		return AppBarLayout::class.java.isInstance(dependency) || Toolbar::class.java.isInstance(dependency)
	}

	override fun onDependentViewChanged(parent: CoordinatorLayout?, child: BottomNavigation?, dependency: View?): Boolean {
		return true
	}

	override fun onNestedVerticalOverScroll(coordinatorLayout: CoordinatorLayout, child: BottomNavigation, direction: Int, currentOverScroll: Int, totalOverScroll: Int) {}

	override fun onDirectionNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: BottomNavigation, target: View, dx: Int, dy: Int, consumed: IntArray, scrollDirection: Int) {}

	override fun onNestedDirectionFling(coordinatorLayout: CoordinatorLayout, child: BottomNavigation, target: View, velocityX: Float, velocityY: Float, scrollDirection: Int): Boolean {
		return false
	}
}