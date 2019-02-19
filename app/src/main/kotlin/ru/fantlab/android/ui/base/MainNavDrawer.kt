package ru.fantlab.android.ui.base

import android.content.Intent
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.transition.TransitionManager
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.drawer_header.view.*
import kotlinx.android.synthetic.main.nav_menu_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.User
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.ui.modules.about.AboutActivity
import ru.fantlab.android.ui.modules.authors.AuthorsActivity
import ru.fantlab.android.ui.modules.awards.AwardsActivity
import ru.fantlab.android.ui.modules.login.LoginActivity
import ru.fantlab.android.ui.modules.main.MainActivity
import ru.fantlab.android.ui.modules.user.UserPagerActivity
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

/**
 * Created by Kosh on 09 Jul 2017, 3:50 PM
 */
class MainNavDrawer(val view: BaseActivity<*, *>, private val extraNav: NavigationView?, private val accountsNav: NavigationView?)
	: BaseViewHolder.OnItemClickListener<User> {

	private var menusHolder: ViewGroup? = null
	private val userModel: User? = PrefGetter.getLoggedUser()

	init {
		menusHolder = view.menusHolder
	}

	fun setupViewDrawer() {
		extraNav?.let {
			val header = it.getHeaderView(0)
			setupView(header)
		}
	}

	private fun setupView(view: View) {
		if (userModel != null) {
			view.navAvatarLayout.setUrl("https://${userModel.avatar}")
			view.navUsername.text = userModel.login
			val navFullName = view.navFullName
			if (userModel.fio.isBlank()) {
				navFullName.visibility = View.GONE
			} else {
				navFullName.visibility = View.VISIBLE
				navFullName.text = userModel.fio
			}
			view.navAccHolder.setOnClickListener {
				if (extraNav != null && accountsNav != null) {
					TransitionManager.beginDelayedTransition(menusHolder ?: extraNav)
					accountsNav.visibility = if (accountsNav.visibility == View.VISIBLE) View.GONE else View.VISIBLE
					view.navToggle.rotation = if (accountsNav.visibility == View.VISIBLE) 180f else 0f
				}
			}
		} else {
			with(view) {
				// аватар Р.Букашки
				navAvatarLayout.setUrl("https://data.fantlab.ru/images/users/2_1")
				navFullName.text = view.context.getString(R.string.guest)
				navUsername.visibility = View.GONE
				navToggle.visibility = View.INVISIBLE
			}
		}
	}

	fun onMainNavItemClick(item: MenuItem) {
		if (item.isChecked) return
		Handler().postDelayed({
			if (!view.isFinishing()) {
				when (item.itemId) {
					R.id.sign_in -> {
						val intent = Intent(view, LoginActivity::class.java)
						intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
						view.startActivity(intent)
						view.finish()
					}
					R.id.mainView -> {
						val intent = Intent(view, MainActivity::class.java)
						intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
						view.startActivity(intent)
						view.finish()
					}
					R.id.settingsView -> {
						view.onOpenSettings()
					}
					R.id.aboutView -> {
						view.startActivity(Intent(view, AboutActivity::class.java))
					}
					R.id.profile -> userModel?.let {
						UserPagerActivity.startActivity(view, it.login, it.id, 0)
					}
					R.id.authors -> view.startActivity(Intent(view, AuthorsActivity::class.java))
					R.id.awards -> view.startActivity(Intent(view, AwardsActivity::class.java))
				}
			}
		}, 250)
	}

	override fun onItemLongClick(position: Int, v: View?, item: User) {
	}

	override fun onItemClick(position: Int, v: View?, item: User) {
	}
}