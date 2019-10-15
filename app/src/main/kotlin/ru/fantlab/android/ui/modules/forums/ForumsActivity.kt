package ru.fantlab.android.ui.modules.forums

import android.os.Bundle
import ru.fantlab.android.R
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.modules.forums.main.ForumsMainFragment

class ForumsActivity : BaseActivity<ForumsMvp.View, ForumsPresenter>(), ForumsMvp.View {

	override fun layout(): Int = R.layout.forums_main_layout

	override fun isTransparent(): Boolean = false

	override fun canBack(): Boolean = true

	override fun providePresenter(): ForumsPresenter = ForumsPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		hideShowShadow(true)
		selectMenuItem(R.id.forum, true)
		if (savedInstanceState == null) {
			val forumFragment = ForumsMainFragment()
			supportFragmentManager
					.beginTransaction()
					.add(R.id.container, forumFragment, ForumsMainFragment.TAG)
					.commit()
		}
	}

	override fun openForum(page: String, id: Int, title: String, forumId: Int) = presenter.openForum(supportFragmentManager, page, id, title, forumId)

	override fun setTitle(title: String) {
		this.title = title
		appbar?.setExpanded(true, false)
	}

	override fun onBackPressed() {
		val fm = supportFragmentManager
		when {
			fm.backStackEntryCount > 0 -> {
				fm.popBackStack()
				appbar?.setExpanded(true, false)
			}
			else -> super.onBackPressed()
		}
	}
}