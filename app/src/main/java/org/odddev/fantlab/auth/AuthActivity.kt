package org.odddev.fantlab.auth

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.content.IntentCompat

import org.odddev.fantlab.R

/**
 * @author kenrube
 * *
 * @since 23.08.16
 */

class AuthActivity : FragmentActivity() {

	private lateinit var router: AuthRouter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		DataBindingUtil.setContentView<ViewDataBinding>(this, R.layout.auth_activity)

		router = AuthRouter(this, R.id.container)
		router.routeToLogin()
	}

	override fun onBackPressed() {
		finish()
	}

	companion object {

		fun start(context: Context, flags: Int = Intent.FLAG_ACTIVITY_NEW_TASK or IntentCompat.FLAG_ACTIVITY_CLEAR_TASK) {
			val intent = Intent(context, AuthActivity::class.java)
			intent.flags = intent.flags or flags
			context.startActivity(intent)
		}
	}
}
