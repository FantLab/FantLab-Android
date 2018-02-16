package ru.fantlab.android.ui.base

import android.content.Intent
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.transition.TransitionManager
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Login
import ru.fantlab.android.data.dao.model.getLoggedUser
import ru.fantlab.android.ui.modules.main.MainActivity
import ru.fantlab.android.ui.modules.user.UserPagerActivity
import ru.fantlab.android.ui.widgets.AvatarLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

/**
 * Created by Kosh on 09 Jul 2017, 3:50 PM
 */
class MainNavDrawer(val view: BaseActivity<*, *>, private val extraNav: NavigationView?, private val accountsNav: NavigationView?)
	: BaseViewHolder.OnItemClickListener<Login> {

	private var menusHolder: ViewGroup? = null
	private val userModel: Login? = getLoggedUser()

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
		userModel?.let {
			(view.findViewById<View>(R.id.navAvatarLayout) as AvatarLayout).setUrl("https://${it.avatar}")
			(view.findViewById<View>(R.id.navUsername) as TextView).text = it.login
			val navFullName = view.findViewById<FontTextView>(R.id.navFullName)
			when (it.fio.isNullOrBlank()) {
				true -> navFullName.visibility = View.GONE
				else -> {
					navFullName.visibility = View.VISIBLE
					navFullName.text = it.fio
				}
			}
			view.findViewById<View>(R.id.navAccHolder).setOnClickListener {
				if (extraNav != null && accountsNav != null) {
					TransitionManager.beginDelayedTransition(menusHolder ?: extraNav)
					accountsNav.visibility = if (accountsNav.visibility == View.VISIBLE) View.GONE else View.VISIBLE
					view.findViewById<View>(R.id.navToggle).rotation = if (accountsNav.visibility == View.VISIBLE) 180f else 0f
				}
			}
		}
	}

	fun onMainNavItemClick(item: MenuItem) {
		if (item.isChecked) return
		Handler().postDelayed({
			if (!view.isFinishing()) {
				when {
					item.itemId == R.id.mainView -> {
						val intent = Intent(view, MainActivity::class.java)
						intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
						view.startActivity(intent)
						view.finish()
					}
					item.itemId == R.id.profile -> userModel?.let {
						UserPagerActivity.startActivity(view, it.login!!, it.userId!!, 0)
					}
				//item.itemId == R.id.settings -> view.onOpenSettings()
				//item.itemId == R.id.about -> view.startActivity(Intent(view, FantlabAboutActivity::class.java))
				//item.itemId == R.id.reportBug -> view.startActivity(CreateIssueActivity.startForResult(view))
				}
			}
		}, 250)
	}

	override fun onItemLongClick(position: Int, v: View?, item: Login) {
	}

	override fun onItemClick(position: Int, v: View, item: Login) {
	}
}