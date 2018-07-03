package ru.fantlab.android.ui.base

import android.content.Intent
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.transition.TransitionManager
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.User
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.ui.modules.authors.AuthorsActivity
import ru.fantlab.android.ui.modules.login.LoginActivity
import ru.fantlab.android.ui.modules.main.MainActivity
import ru.fantlab.android.ui.modules.user.UserPagerActivity
import ru.fantlab.android.ui.widgets.AvatarLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

/**
 * Created by Kosh on 09 Jul 2017, 3:50 PM
 */
class MainNavDrawer(val view: BaseActivity<*, *>, private val extraNav: NavigationView?, private val accountsNav: NavigationView?)
	: BaseViewHolder.OnItemClickListener<User> {

	private var menusHolder: ViewGroup? = null
	private val userModel: User? = PrefGetter.getLoggedUser()

	init {
		menusHolder = view.findViewById(R.id.menusHolder)
	}

	fun setupViewDrawer() {
		extraNav?.let {
			val header = it.getHeaderView(0)
			setupView(header)
		}
	}

	private fun setupView(view: View) {
		if (userModel != null) {
			view.findViewById<AvatarLayout>(R.id.navAvatarLayout).setUrl("https://${userModel.avatar}")
			view.findViewById<FontTextView>(R.id.navUsername).text = userModel.login
			val navFullName = view.findViewById<FontTextView>(R.id.navFullName)
			if (userModel.fio.isBlank()) {
				navFullName.visibility = View.GONE
			} else {
				navFullName.visibility = View.VISIBLE
				navFullName.text = userModel.fio
			}
			view.findViewById<View>(R.id.navAccHolder).setOnClickListener {
				if (extraNav != null && accountsNav != null) {
					TransitionManager.beginDelayedTransition(menusHolder ?: extraNav)
					accountsNav.visibility = if (accountsNav.visibility == View.VISIBLE) View.GONE else View.VISIBLE
					view.findViewById<View>(R.id.navToggle).rotation = if (accountsNav.visibility == View.VISIBLE) 180f else 0f
				}
			}
		} else {
			with(view) {
				// аватар Р.Букашки
				findViewById<AvatarLayout>(R.id.navAvatarLayout).setUrl("https://data.fantlab.ru/images/users/2_1")
				findViewById<FontTextView>(R.id.navFullName).text = view.context.getString(R.string.guest)
				findViewById<View>(R.id.navUsername).visibility = View.GONE
				findViewById<View>(R.id.navToggle).visibility = View.INVISIBLE
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
					R.id.profile -> userModel?.let {
						UserPagerActivity.startActivity(view, it.login, it.id, 0)
					}
					R.id.bibliographies -> view.startActivity(Intent(view, AuthorsActivity::class.java))
				//item.itemId == R.id.about -> view.startActivity(Intent(view, AboutActivity::class.java))
				}
			}
		}, 250)
	}

	override fun onItemLongClick(position: Int, v: View?, item: User) {
	}

	override fun onItemClick(position: Int, v: View, item: User) {
	}
}