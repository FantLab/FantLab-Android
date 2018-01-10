package ru.fantlab.android.old.launch

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.fantlab.android.old.auth.AuthActivity
import ru.fantlab.android.old.core.di.Injector
import ru.fantlab.android.old.core.storage.StorageManager
import ru.fantlab.android.old.home.HomeActivity
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
		val anonymous = storageManager.loadAnonymous()
		when {
			loggedIn -> {
				val intent = Intent(this, HomeActivity::class.java)
				intent.putExtra("LOGGED_IN", true)
				startActivity(intent)
			}
			anonymous -> {
				val intent = Intent(this, HomeActivity::class.java)
				intent.putExtra("LOGGED_IN", false)
				startActivity(intent)
			}
			else -> startActivity(Intent(this, AuthActivity::class.java))
		}
		finish()
	}
}
