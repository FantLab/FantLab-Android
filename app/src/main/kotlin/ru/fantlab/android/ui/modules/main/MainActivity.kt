package ru.fantlab.android.ui.modules.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.view.Menu
import android.view.MenuItem
import butterknife.BindView
import com.evernote.android.state.State
import it.sephiroth.android.library.bottomnavigation.BottomNavigation
import ru.fantlab.android.R
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.TypeFaceHelper
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.modules.main.news.NewsFragment

class MainActivity : BaseActivity<MainMvp.View, MainPresenter>(), MainMvp.View {

	@BindView(R.id.bottomNavigation)
	lateinit var bottomNavigation: BottomNavigation

	@State
	var navType = MainMvp.NavigationType.NEWS

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		hideShowShadow(true)
		setToolbarIcon(R.drawable.ic_menu)
		onInit(savedInstanceState)
		onNewIntent(intent)
	}

	override fun onNewIntent(intent: Intent?) {
		super.onNewIntent(intent)
		intent?.extras?.let {
			val recreate = it.getBoolean(BundleConstant.YES_NO_EXTRA)
			if (recreate) recreate()
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.search_menu, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when {
			item.itemId == android.R.id.home -> {
				drawer.openDrawer(GravityCompat.START)
				true
			}
			item.itemId == R.id.search -> {
				//startActivity(Intent(this, SearchActivity::class.java))
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

	override fun layout(): Int = R.layout.activity_main_view

	override fun isTransparent(): Boolean = true

	override fun canBack(): Boolean = false

	override fun isSecured(): Boolean = false

	override fun providePresenter(): MainPresenter = MainPresenter()

	override fun onNavigationChanged(navType: MainMvp.NavigationType) {
		this.navType = navType
		if (bottomNavigation.selectedIndex != navType.ordinal) {
			bottomNavigation.setSelectedIndex(navType.ordinal, true)
		}
		hideShowShadow(true)
		presenter.onModuleChanged(supportFragmentManager, navType)
	}

	private fun onInit(savedInstanceState: Bundle?) {
		if (isLoggedIn()) {
			if (savedInstanceState == null) {
				hideShowShadow(true)
				supportFragmentManager
						.beginTransaction()
						.replace(R.id.container, NewsFragment(), NewsFragment.TAG)
						.commit()
			}
			val myTypeface = TypeFaceHelper.typeface
			bottomNavigation.setDefaultTypeface(myTypeface)
			bottomNavigation.setOnMenuItemClickListener(presenter)
		}
	}
}