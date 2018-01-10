package ru.fantlab.android.old.core.network

import android.content.Context
import android.net.ConnectivityManager

import ru.fantlab.android.old.core.di.Injector

import javax.inject.Inject

class NetworkChecker : INetworkChecker {

	@Inject
	lateinit var context: Context

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun isConnected(): Boolean {
		val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val ni = cm.activeNetworkInfo

		return ni?.isConnected ?: false
	}
}
