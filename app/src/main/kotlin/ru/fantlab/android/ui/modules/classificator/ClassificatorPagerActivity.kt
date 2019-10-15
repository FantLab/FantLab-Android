package ru.fantlab.android.ui.modules.classificator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.View
import com.evernote.android.state.State
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.appbar_tabbed_elevation.*
import kotlinx.android.synthetic.main.tabbed_pager_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.FragmentPagerAdapterModel
import ru.fantlab.android.data.dao.TabsCountStateModel
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.FantlabHelper.classNeededToSet
import ru.fantlab.android.helper.ViewHelper
import ru.fantlab.android.ui.adapter.FragmentsPagerAdapter
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.base.BaseFragment
import java.util.*

class ClassificatorPagerActivity : BaseActivity<ClassificatorPagerMvp.View, ClassificatorPagerPresenter>(),
		ClassificatorPagerMvp.View {

	@State var index: Int = 0
	@State var workId: Int = 0
	var classificators: ArrayList<Int> = arrayListOf()
	@State var tabsCountSet = HashSet<TabsCountStateModel>()

	override fun layout(): Int = R.layout.tabbed_pager_layout

	override fun isTransparent(): Boolean = true

	override fun canBack(): Boolean = true

	override fun providePresenter() = ClassificatorPagerPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (savedInstanceState == null) {
			workId = intent?.extras?.getInt(BundleConstant.EXTRA, -1) ?: -1
		}
		if (workId == -1) {
			finish()
			return
		}
		setTaskName(null)
		title = getString(R.string.classificator)
		fab.setImageResource(R.drawable.ic_send)
		fab.setOnClickListener { onFabClicked() }
		selectMenuItem(R.id.mainView, false)
		val adapter = FragmentsPagerAdapter(
				supportFragmentManager,
				FragmentPagerAdapterModel.buildForClassificator(this, workId)
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
		markTabs()
	}

	private fun markTabs() {
		classNeededToSet.forEach {
			ViewHelper.getTabTextView(tabs, it).setTextColor(ContextCompat.getColor(this, R.color.material_red_700))
		}
	}

	override fun onScrollTop(index: Int) {
		if (pager.adapter == null) return
		val fragment = pager.adapter?.instantiateItem(pager, index) as? BaseFragment<*, *>
		if (fragment is BaseFragment) {
			fragment.onScrollTop(index)
		}
	}

	private fun hideShowFab() {
		fab.visibility = if (checkSelections()) View.VISIBLE else View.GONE
	}

	override fun onSelected(extra: Int, add: Boolean) {
		if (add) classificators.add(extra) else classificators.remove(extra)
		hideShowFab()
	}

	fun onFabClicked() {
		if (checkSelections()) {
			val resultQuery = StringBuilder()
			(0 until classificators.size - 1).forEach { item ->
				resultQuery.append("wg${classificators[item]}=on&")
			}
			resultQuery.append("wg${classificators.last()}=on")
			presenter.onSendClassification(workId, resultQuery.toString())
		} else showMessage("Error", getString(R.string.class_needed))
	}

	private fun checkSelections(): Boolean {
		var classed = 0
		classNeededToSet.forEach {
			val textView = ViewHelper.getTabTextView(tabs, it).text
			if (textView.contains("*")) {
				classed++
			}
		}
		return classed == classNeededToSet.size
	}

	override fun onClassSended() {
		setResult(RESULT_OK)
		showMessage("Success", getString(R.string.class_sended))
		finish()
	}

	override fun onSetBadge(tabIndex: Int, count: Int) {
		tabsCountSet.add(TabsCountStateModel(count = count, tabIndex = tabIndex))
		setupTab(count, tabIndex)
	}

	private fun setupTab(count: Int, index: Int) {
		val textView = ViewHelper.getTabTextView(tabs, index)
		val counter = if (count > 0) "*" else ""
		when (index) {
			0 -> textView.text = String.format("%s%s", getString(R.string.genre), counter)
			1 -> textView.text = String.format("%s%s", getString(R.string.characteristics), counter)
			2 -> textView.text = String.format("%s%s", getString(R.string.locate), counter)
			3 -> textView.text = String.format("%s%s", getString(R.string.time), counter)
			4 -> textView.text = String.format("%s%s", getString(R.string.story), counter)
			5 -> textView.text = String.format("%s%s", getString(R.string.linearity), counter)
			6 -> textView.text = String.format("%s%s", getString(R.string.age), counter)
		}
	}

	companion object {

		fun startActivity(activity: Activity, workId: Int) {
			val intent = Intent(activity, ClassificatorPagerActivity::class.java)
			intent.putExtras(Bundler.start()
					.put(BundleConstant.EXTRA, workId)
					.end())
			activity.startActivityForResult(intent, BundleConstant.CLASSIFICATOR_CODE)
		}
	}
}