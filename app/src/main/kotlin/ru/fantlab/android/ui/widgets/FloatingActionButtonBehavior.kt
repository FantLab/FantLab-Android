package ru.fantlab.android.ui.widgets

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import it.sephiroth.android.library.bottomnavigation.BottomNavigation
import it.sephiroth.android.library.bottomnavigation.MiscUtils

class FloatingActionButtonBehavior : CoordinatorLayout.Behavior<FloatingActionButton> {

	private var navigationBarHeight = 0

	constructor() : super()

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

	override fun onAttachedToLayoutParams(lp: CoordinatorLayout.LayoutParams) {
		// super.onAttachedToLayoutParams(lp);
	}

	override fun layoutDependsOn(parent: CoordinatorLayout?, child: FloatingActionButton?, dependency: View?): Boolean {
		if (BottomNavigation::class.java.isInstance(dependency)) {
			return true
		} else if (Snackbar.SnackbarLayout::class.java.isInstance(dependency)) {
			return true
		}
		return super.layoutDependsOn(parent, child, dependency)
	}

	override fun onDependentViewChanged(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View?): Boolean {
		MiscUtils.log(TAG, Log.INFO, "onDependentViewChanged: " + dependency!!)
		val list = parent.getDependencies(child)
		val params = child.layoutParams as ViewGroup.MarginLayoutParams
		val bottomMargin = params.bottomMargin + params.rightMargin - (params.topMargin + params.leftMargin)
		var t = 0f
		var t2 = 0f
		var result = false
		for (dep in list) {
			if (Snackbar.SnackbarLayout::class.java.isInstance(dep)) {
				t += dep.translationY - dep.height
				result = true
			} else if (BottomNavigation::class.java.isInstance(dep)) {
				val navigation = dep as BottomNavigation
				t2 = navigation.translationY - navigation.height + bottomMargin
				t += t2
				result = true

				if (navigationBarHeight > 0) {
					if (!navigation.isExpanded) {
						child.hide()
					} else {
						child.show()
					}
				}
			}
		}

		if (navigationBarHeight > 0 && t2 < 0) {
			t = Math.min(t2, t + navigationBarHeight)
		}

		child.translationY = t
		return result
	}

	fun setNavigationBarHeight(height: Int) {
		this.navigationBarHeight = height
	}

	companion object {
		private val TAG = FloatingActionButtonBehavior::class.java.simpleName
	}
}