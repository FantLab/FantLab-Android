package org.odddev.fantlab.home

import android.support.annotation.IdRes
import android.support.annotation.IntDef

import org.odddev.fantlab.R
import org.odddev.fantlab.auth.AuthActivity
import org.odddev.fantlab.award.AwardsFragment
import org.odddev.fantlab.catalog.CatalogFragment
import org.odddev.fantlab.core.layers.router.Router
import org.odddev.fantlab.core.utils.FragmentUtils
import org.odddev.fantlab.profile.ProfileFragment
import org.odddev.fantlab.search.SearchFragment

import kotlin.annotation.Retention

/**
 * @author kenrube
 * *
 * @since 11.10.16
 */

internal class NavDrawerRouter(activity: HomeActivity, @IdRes containerId: Int) : Router<HomeActivity>(activity, containerId) {

	@IntDef(R.id.nav_autors.toLong(), R.id.nav_awards.toLong(), R.id.nav_search.toLong(), R.id.nav_profile.toLong(), R.id.nav_logout.toLong(), R.id.nav_login.toLong())
	@Retention(AnnotationRetention.SOURCE)
	internal annotation class NAV_DRAWER_ITEM

	fun routeToNavDrawerItem(@NAV_DRAWER_ITEM item: Int) {
		when (item) {
			R.id.nav_autors -> FragmentUtils.replaceFragment(activity, containerId, CatalogFragment(), false)
			R.id.nav_awards -> FragmentUtils.replaceFragment(activity, containerId, AwardsFragment(), false)
			R.id.nav_search -> FragmentUtils.replaceFragment(activity, containerId, SearchFragment(), false)
			R.id.nav_profile -> FragmentUtils.replaceFragment(activity, containerId, ProfileFragment(), false)
			R.id.nav_logout, R.id.nav_login -> {
				AuthActivity.start(activity)
				activity.overridePendingTransition(0, 0)
			}
		}
	}
}
