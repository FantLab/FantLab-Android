package org.odddev.fantlab.auth

import android.support.annotation.IdRes

import org.odddev.fantlab.auth.login.LoginFragment
import org.odddev.fantlab.auth.reg.RegFragment
import org.odddev.fantlab.core.layers.router.Router
import org.odddev.fantlab.core.utils.FragmentUtils
import org.odddev.fantlab.home.HomeActivity

/**
 * @author kenrube
 * *
 * @since 28.09.16
 */

class AuthRouter(activity: AuthActivity, @IdRes containerId: Int) : Router<AuthActivity>(activity, containerId) {

	fun routeToLogin() {
		FragmentUtils.replaceFragment(activity, containerId, LoginFragment(), true)
	}

	fun routeToReg() {
		FragmentUtils.replaceFragment(activity, containerId, RegFragment(), true)
	}

	fun routeToHome(loggedIn: Boolean) {
		HomeActivity.start(activity, loggedIn)
		activity.overridePendingTransition(0, 0)
	}
}
