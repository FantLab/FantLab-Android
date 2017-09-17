package org.odddev.fantlab.launch

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.odddev.fantlab.auth.AuthActivity
import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.storage.StorageManager
import org.odddev.fantlab.home.HomeActivity
import javax.inject.Inject

class LaunchActivity : AppCompatActivity() {

	// todo оформить запрос к SharedPreferences через Rx Observables
	@Inject
	lateinit var storageManager: StorageManager

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val loggedIn = !storageManager.loadCookie().isNullOrEmpty()
		if (loggedIn)
			run {
				val intent = Intent(this, HomeActivity::class.java)
				intent.putExtra("LOGGED_IN", true)
				startActivity(intent)
			}
		else
			startActivity(Intent(this, AuthActivity::class.java))
		finish()
	}
}