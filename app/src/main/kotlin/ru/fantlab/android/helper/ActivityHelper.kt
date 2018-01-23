package ru.fantlab.android.helper

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

object ActivityHelper {

	fun getVisibleFragment(manager: FragmentManager): Fragment? {
		val fragments = manager.fragments
		if (fragments != null && !fragments.isEmpty()) {
			fragments
					.filter { it != null && it.isVisible }
					.forEach { return it }
		}
		return null
	}
}