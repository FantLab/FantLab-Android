package ru.fantlab.android.ui.modules.communities

import android.os.Bundle
import ru.fantlab.android.R
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.modules.communities.main.CommunitiesMainFragment

class CommunitiesActivity : BaseActivity<CommunitiesMvp.View, CommunitiesPresenter>(), CommunitiesMvp.View {

	override fun layout(): Int = R.layout.communities_main_layout

	override fun isTransparent(): Boolean = false

	override fun canBack(): Boolean = true

	override fun providePresenter(): CommunitiesPresenter = CommunitiesPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		hideShowShadow(true)
		selectMenuItem(R.id.communities, true)
		if (savedInstanceState == null) {
			val communitiesFragment = CommunitiesMainFragment()
			supportFragmentManager
					.beginTransaction()
					.add(R.id.container, communitiesFragment, CommunitiesMainFragment.TAG)
					.commit()
		}
	}

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