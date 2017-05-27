package org.odddev.fantlab.core.utils

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/**
 * @author kenrube
 * *
 * @since 30.08.16
 */

object FragmentUtils {

	fun replaceFragment(activity: FragmentActivity, @IdRes containerId: Int,
						fragment: Fragment, backStack: Boolean) {
		try {
			val fragmentManager = activity.supportFragmentManager
			val fragmentTransaction = fragmentManager.beginTransaction()
			fragmentTransaction.replace(containerId, fragment, fragment.javaClass.simpleName)
			if (backStack) {
				fragmentTransaction.addToBackStack(null)
			}
			fragmentTransaction.commit()
			fragmentManager.executePendingTransactions()
		} catch (e: IllegalStateException) {
			e.printStackTrace()
		}
	}
}
