package ru.fantlab.android.ui.modules.plans

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.evernote.android.state.State
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.appbar_tabbed_elevation.*
import kotlinx.android.synthetic.main.tabbed_pager_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.FragmentPagerAdapterModel
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.FragmentsPagerAdapter
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.modules.plans.autplans.AutPlansFragment
import ru.fantlab.android.ui.modules.plans.pubnews.PubnewsFragment
import ru.fantlab.android.ui.modules.plans.pubplans.PubplansFragment

class PlansPagerActivity : BaseActivity<PlansPagerMvp.View, BasePresenter<PlansPagerMvp.View>>(),
		PlansPagerMvp.View {

	@State
	var index: Int = 0

	override fun layout(): Int = R.layout.tabbed_pager_layout

	override fun isTransparent(): Boolean = true

	override fun canBack(): Boolean = true

	override fun providePresenter(): BasePresenter<PlansPagerMvp.View> = BasePresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (savedInstanceState == null) {
			index = intent?.extras?.getInt(BundleConstant.EXTRA, -1) ?: -1
		}
		setTaskName(getString(R.string.plans))
		title = getString(R.string.plans)
		selectMenuItem(R.id.plans, true)
		val adapter = FragmentsPagerAdapter(
				supportFragmentManager,
				FragmentPagerAdapterModel.buildForPlans(this)
		)
		pager.adapter = adapter
		tabs.tabGravity = TabLayout.GRAVITY_FILL
		tabs.tabMode = TabLayout.MODE_SCROLLABLE
		tabs.setupWithViewPager(pager)
		if (savedInstanceState == null) {
			if (index != -1) {
				pager.currentItem = index
			}
		}
		tabs.addOnTabSelectedListener(object : TabLayout.ViewPagerOnTabSelectedListener(pager) {
			override fun onTabReselected(tab: TabLayout.Tab) {
				super.onTabReselected(tab)
				onScrollTop(tab.position)
			}
		})
		fab.setImageResource(R.drawable.ic_filter)
		fab.show()
		fab.setOnClickListener { onFabClicked() }
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.plans_menu, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.sort -> {
				val fragment = pager.adapter?.instantiateItem(pager, pager.currentItem)
				when (pager.currentItem) {
					0 -> {
						fragment as PubnewsFragment
						fragment.showSortDialog()
					}
					1 -> {
						fragment as PubplansFragment
						fragment.showSortDialog()
					}
					2 -> {
						fragment as AutPlansFragment
						fragment.showSortDialog()
					}
				}
				return true
			}
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onScrollTop(index: Int) {
		if (pager.adapter == null) return
		val fragment = pager.adapter?.instantiateItem(pager, index) as? BaseFragment<*, *>
		if (fragment is BaseFragment) {
			fragment.onScrollTop(index)
		}
	}

	private fun onFabClicked() {
		when (pager.currentItem) {
			0 -> {
				val fragment = pager.adapter?.instantiateItem(pager, pager.currentItem) as PubnewsFragment
				fragment.showFilterDialog()
			}
			1 -> {
				val fragment = pager.adapter?.instantiateItem(pager, pager.currentItem) as PubplansFragment
				fragment.showFilterDialog()
			}
			2 -> {
				val fragment = pager.adapter?.instantiateItem(pager, pager.currentItem) as AutPlansFragment
				fragment.showFilterDialog()
			}
		}
	}

	override fun onScrolled(isUp: Boolean) {
		if (isUp) {
			fab.hide()
		} else {
			fab.show()
		}
	}

	companion object {

		fun startActivity(context: Context, index: Int = -1) {
			val intent = Intent(context, PlansPagerActivity::class.java)
			intent.putExtras(Bundler.start()
					.put(BundleConstant.EXTRA, index)
					.end())
			if (context is Service || context is Application) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			}
			context.startActivity(intent)
		}
	}
}