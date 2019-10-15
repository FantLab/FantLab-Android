package ru.fantlab.android.ui.modules.work

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.evernote.android.state.State
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.appbar_tabbed_elevation.*
import kotlinx.android.synthetic.main.tabbed_pager_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.FragmentPagerAdapterModel
import ru.fantlab.android.data.dao.TabsCountStateModel
import ru.fantlab.android.helper.ActivityHelper
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.ViewHelper
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.adapter.FragmentsPagerAdapter
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.modules.bookcases.editor.BookcaseEditorActivty
import ru.fantlab.android.ui.modules.bookcases.selector.BookcaseSelectorFragment
import ru.fantlab.android.ui.modules.editor.EditorActivity
import ru.fantlab.android.ui.modules.work.responses.WorkResponsesFragment
import java.util.*


class WorkPagerActivity : BaseActivity<WorkPagerMvp.View, BasePresenter<WorkPagerMvp.View>>(),
		WorkPagerMvp.View {

	@State var index: Int = 0
	@State var workId: Int = 0
	@State var workName: String = ""
	@State var mark: Int = 0
	@State var tabsCountSet = HashSet<TabsCountStateModel>()
	private lateinit var toolbarMenu: Menu
	private var isError = false
	private var adapter: FragmentsPagerAdapter? = null
	override fun layout(): Int = R.layout.tabbed_pager_layout

	override fun isTransparent(): Boolean = true

	override fun canBack(): Boolean = true

	override fun providePresenter(): BasePresenter<WorkPagerMvp.View> = BasePresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (savedInstanceState == null) {
			workId = intent?.extras?.getInt(BundleConstant.EXTRA, -1) ?: -1
			workName = intent?.extras?.getString(BundleConstant.EXTRA_TWO) ?: ""
			index = intent?.extras?.getInt(BundleConstant.EXTRA_THREE, -1) ?: -1
		}
		if (workId == -1) {
			finish()
			return
		}
		setTaskName(workName)
		title = workName
		selectMenuItem(R.id.mainView, false)
		adapter = FragmentsPagerAdapter(
				supportFragmentManager,
				FragmentPagerAdapterModel.buildForWork(this, workId)
		)
		pager.adapter = adapter
		tabs.tabGravity = TabLayout.GRAVITY_FILL
		tabs.tabMode = TabLayout.MODE_SCROLLABLE
		tabs.setupWithViewPager(pager)
		invalidateTabs(adapter)

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
		pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
			override fun onPageSelected(position: Int) {
				super.onPageSelected(position)
				hideShowFab(position)
				hideShowToolbar(position)
			}
		})
		if (savedInstanceState != null && !tabsCountSet.isEmpty()) {
			tabsCountSet.forEach { setupTab(count = it.count, index = it.tabIndex) }
		}
		hideShowFab(pager.currentItem)
		fab.setOnClickListener { onFabClicked() }
	}

	private fun invalidateTabs(adapter: FragmentsPagerAdapter?) {
		for (i in 0 until tabs.tabCount) {
			val tab = tabs.getTabAt(i)
			if (tab != null) {
				val custom = tab.customView
				if (custom == null) tab.customView = adapter?.getCustomTabView(this)
				setupTab(0, i)
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.work_menu, menu)
		toolbarMenu = menu
		hideShowToolbar(pager.currentItem)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.share -> {
				ActivityHelper.shareUrl(this, Uri.Builder().scheme(LinkParserHelper.PROTOCOL_HTTPS)
						.authority(LinkParserHelper.HOST_DEFAULT)
						.appendPath("work$workId")
						.toString())
				return true
			}
			R.id.sort -> {
				val fragment = pager.adapter?.instantiateItem(pager, 1) as? WorkResponsesFragment
				fragment?.showSortDialog()
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

	override fun onSetBadge(tabIndex: Int, count: Int) {
		tabsCountSet.add(TabsCountStateModel(count = count, tabIndex = tabIndex))
		setupTab(count, tabIndex)
	}

	override fun onSetTitle(title: String) {
		this.title = title
	}

	private fun hideShowFab(position: Int) {
		if (isError) {
			fab.hide()
			return
		}
		when (adapter?.getItemKey(position)) {
			getString(R.string.responses) -> {
				if (isLoggedIn()) {
					fab.setImageResource(R.drawable.ic_response)
					fab.show()
				}
			}
			else -> fab.hide()
		}
	}

	private fun hideShowToolbar(position: Int) {
		if (::toolbarMenu.isInitialized) {
			when (adapter?.getItemKey(position)) {
				getString(R.string.overview) -> {
					toolbarMenu.findItem(R.id.sort).isVisible = false
					toolbarMenu.findItem(R.id.share).isVisible = true
				}
				getString(R.string.responses) -> {
					toolbarMenu.findItem(R.id.share).isVisible = false
					toolbarMenu.findItem(R.id.sort).isVisible = true
				}
				else -> {
					toolbarMenu.findItem(R.id.share).isVisible = false
					toolbarMenu.findItem(R.id.sort).isVisible = false
				}
			}
		}
	}

	override fun onError() {
		isError = true
		fab.hide()
	}

	private fun onFabClicked() {
		when (adapter?.getItemKey(pager.currentItem)) {
			getString(R.string.responses) -> {
				startActivityForResult(Intent(this, EditorActivity::class.java)
						.putExtra(BundleConstant.EXTRA_TYPE, BundleConstant.EDITOR_NEW_RESPONSE)
						.putExtra(BundleConstant.ID, workId), BundleConstant.REFRESH_RESPONSE_CODE)
			}
		}
	}

	private fun setupTab(count: Int, index: Int) {
		val tabView = ViewHelper.getTabView(tabs, index)
		when (adapter?.getItemKey(index)) {
			getString(R.string.overview) -> tabView.first.text = getString(R.string.overview)
			getString(R.string.responses) -> {
				tabView.first.text = getString(R.string.responses)
				tabView.second.text = count.toString()
			}
			getString(R.string.analogs) -> {
				tabView.first.text = getString(R.string.analogs)
				tabView.second.text = count.toString()
			}
		}
	}

	override fun onScrolled(isUp: Boolean) {
		if (isUp) {
			fab.hide()
		} else {
			hideShowFab(pager.currentItem)
		}
	}

	override fun onSetMarked(isMarked: Boolean, mark: Int) {
		this.mark = mark
	}

	override fun onGetMark(): Int {
		return mark
	}

	override fun onResponsesRefresh() {
		val fragment = pager.adapter?.instantiateItem(pager, 1) as? WorkResponsesFragment
		fragment?.onRefresh()
	}

	override fun isCycle(): Boolean = false

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		when (requestCode) {
			BundleConstant.REFRESH_RESPONSE_CODE -> {
				val fragment = pager.adapter?.instantiateItem(pager, 1) as? WorkResponsesFragment
				fragment?.onRefresh()
			}
			BundleConstant.BOOKCASE_EDITOR -> {
				if (resultCode == RESULT_OK
						&& adapter?.getItemKey(pager.currentItem) == getString(R.string.bookcases)) {
					val fragment = pager.adapter?.instantiateItem(pager, pager.currentItem) as? BookcaseSelectorFragment
					fragment?.onRefresh()
				}
			}
		}
	}

	companion object {

		fun startActivity(context: Context, workId: Int, workName: String, index: Int = -1) {
			val intent = Intent(context, WorkPagerActivity::class.java)
			intent.putExtras(Bundler.start()
					.put(BundleConstant.EXTRA, workId)
					.put(BundleConstant.EXTRA_TWO, workName)
					.put(BundleConstant.EXTRA_THREE, index)
					.end())
			if (context is Service || context is Application) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			}
			context.startActivity(intent)
		}
	}
}