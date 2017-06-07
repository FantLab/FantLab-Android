package org.odddev.fantlab.launch

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.odddev.fantlab.home.HomeActivity

class LaunchActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		startActivity(Intent(this, HomeActivity::class.java).putExtra("LOGGED_IN", false))
		finish()
	}
}
