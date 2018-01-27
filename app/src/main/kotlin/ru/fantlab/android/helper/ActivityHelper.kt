package ru.fantlab.android.helper

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

object ActivityHelper {

	fun getActivity(content: Context?): Activity? {
		return when (content) {
			null -> null
			is Activity -> content
			is ContextWrapper -> getActivity(content.baseContext)
			else -> null
		}
	}

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