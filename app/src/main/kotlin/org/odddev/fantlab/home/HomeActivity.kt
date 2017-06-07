package org.odddev.fantlab.home

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.IntentCompat
import android.support.v4.view.GravityCompat
import android.text.TextUtils
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import org.odddev.fantlab.R
import org.odddev.fantlab.autors.autor.AutorFragment
import org.odddev.fantlab.autors.autor.biography.BiographyFragment
import org.odddev.fantlab.core.utils.FragmentUtils
import org.odddev.fantlab.databinding.HomeActivityBinding
import org.odddev.fantlab.databinding.NavDrawerHeaderBinding

class HomeActivity : MvpAppCompatActivity(), IHomeView, IActionsHandler {

	private lateinit var binding: HomeActivityBinding
	private lateinit var headerBinding: NavDrawerHeaderBinding
	private lateinit var router: NavDrawerRouter

	@NavDrawerRouter.NAV_DRAWER_ITEM
	private var selectedNavDrawerItemId: Int = R.id.nav_autors

	@InjectPresenter
	lateinit var presenter: HomePresenter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = DataBindingUtil.setContentView<HomeActivityBinding>(this, R.layout.home_activity)

		router = NavDrawerRouter(this, R.id.container)

		if (savedInstanceState != null)
			initNavigationDrawer(savedInstanceState.getInt(SELECTED_NAV_DRAWER_ITEM_ID_KEY))
		else
			initNavigationDrawer(selectedNavDrawerItemId)

		presenter.getUserName()
	}

	private fun initNavigationDrawer(itemId: Int) {
		headerBinding = NavDrawerHeaderBinding.inflate(layoutInflater,
				binding.navigationView, false)

		binding.navigationView.addHeaderView(headerBinding.root)

		binding.navigationView.menu.clear()

		val loggedIn = intent.getBooleanExtra(EXTRA_LOGGED_IN_KEY, false)

		binding.navigationView.inflateMenu(if (loggedIn)
			R.menu.nav_drawer_user
		else
			R.menu.nav_drawer_guest)

		binding.navigationView.setNavigationItemSelectedListener(
				{ item ->
					@NavDrawerRouter.NAV_DRAWER_ITEM val navDrawerItemId = item.itemId

					selectedNavDrawerItemId = navDrawerItemId

					binding.navigationView.setCheckedItem(navDrawerItemId)
					binding.drawerLayout.closeDrawer(GravityCompat.START)

					if (navDrawerItemId == R.id.nav_logout) presenter.clearCookie()

					router.routeToNavDrawerItem(navDrawerItemId)
					true
				})
		binding.navigationView.menu.performIdentifierAction(itemId, 0)
	}

	override fun onBackPressed() {
		if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
			binding.drawerLayout.closeDrawer(GravityCompat.START)
		} else {
			if (supportFragmentManager.backStackEntryCount > 0) {
				supportFragmentManager.popBackStack()
			} else {
				super.onBackPressed()
			}
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putInt(SELECTED_NAV_DRAWER_ITEM_ID_KEY, selectedNavDrawerItemId)
	}

	override fun showUserName(userName: String) {
		if (!TextUtils.isEmpty(userName)) {
			headerBinding.username.text = userName
			headerBinding.classProgress.setText(R.string.nav_drawer_user_class_progress)
		}
	}

	override fun openAutor(id: Int, name: String) {
		FragmentUtils.replaceFragment(this, R.id.container, AutorFragment(id, name), true)
	}

	override fun showBiography(bio: String) {
		FragmentUtils.replaceFragment(this, R.id.container, BiographyFragment(bio), true)
	}

	companion object {

		private val SELECTED_NAV_DRAWER_ITEM_ID_KEY = "SELECTED_NAV_DRAWER_ITEM_ID"
		private val EXTRA_LOGGED_IN_KEY = "LOGGED_IN"

		fun start(context: Context, loggedIn: Boolean) {
			start(context, Intent.FLAG_ACTIVITY_NEW_TASK or IntentCompat.FLAG_ACTIVITY_CLEAR_TASK,
					loggedIn)
		}

		fun start(context: Context, flags: Int, loggedIn: Boolean) {
			val intent = Intent(context, HomeActivity::class.java)
			intent.flags = intent.flags or flags
			intent.putExtra(EXTRA_LOGGED_IN_KEY, loggedIn)
			context.startActivity(intent)
		}
	}
}
