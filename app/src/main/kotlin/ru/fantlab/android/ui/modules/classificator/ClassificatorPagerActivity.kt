package ru.fantlab.android.ui.modules.classificator

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.view.View
import butterknife.BindView
import butterknife.OnClick
import com.evernote.android.state.State
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
import ru.fantlab.android.ui.widgets.ViewPagerView
import java.util.*

class ClassificatorPagerActivity : BaseActivity<ClassificatorPagerMvp.View, ClassificatorPagerPresenter>(),
		ClassificatorPagerMvp.View {

	@BindView(R.id.tabs) lateinit var tabs: TabLayout
	@BindView(R.id.tabbedPager) lateinit var pager: ViewPagerView
	@BindView(R.id.fab) lateinit var fab: FloatingActionButton

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

	@OnClick(R.id.fab)
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

		fun startActivity(context: Context, workId: Int) {
			val intent = Intent(context, ClassificatorPagerActivity::class.java)
			intent.putExtras(Bundler.start()
					.put(BundleConstant.EXTRA, workId)
					.end())
			if (context is Service || context is Application) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			}
			context.startActivity(intent)
		}
	}
}