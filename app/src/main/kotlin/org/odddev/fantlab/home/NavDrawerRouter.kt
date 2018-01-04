package org.odddev.fantlab.home

import android.annotation.SuppressLint
import android.content.Intent
import android.support.annotation.IdRes
import android.support.annotation.IntDef
import org.odddev.fantlab.R
import org.odddev.fantlab.auth.AuthActivity
import org.odddev.fantlab.authors.AuthorsFragment
import org.odddev.fantlab.core.layers.router.Router
import org.odddev.fantlab.core.utils.FragmentUtils
import org.odddev.fantlab.search.SearchFragment

internal class NavDrawerRouter(activity: HomeActivity, @IdRes containerId: Int) : Router<HomeActivity>(activity, containerId) {

	@IntDef(R.id.nav_authors.toLong()/*, R.id.nav_awards.toLong()*/, R.id.nav_search.toLong(),
			/*R.id.nav_profile.toLong(),*/ R.id.nav_logout.toLong(), R.id.nav_login.toLong())
	@Retention(AnnotationRetention.SOURCE)
	internal annotation class NAV_DRAWER_ITEM

	@SuppressLint("SwitchIntDef")
	fun routeToNavDrawerItem(@NAV_DRAWER_ITEM item: Int) {
		when (item) {
			R.id.nav_authors -> FragmentUtils.replaceFragment(activity, containerId, AuthorsFragment(), false)
			R.id.nav_search -> FragmentUtils.replaceFragment(activity, containerId, SearchFragment(), false)
			//R.id.nav_awards -> FragmentUtils.replaceFragment(activity, containerId, AwardsFragment(), false)
			R.id.nav_login ->
				run {
					activity.startActivity(Intent(activity, AuthActivity::class.java))
					activity.finish()
				}
			R.id.nav_logout ->
				run {
					activity.startActivity(Intent(activity, AuthActivity::class.java))
					activity.finish()
				}
		}
	}
}
