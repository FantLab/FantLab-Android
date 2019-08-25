package ru.fantlab.android.ui.modules.author

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
import ru.fantlab.android.ui.modules.author.responses.AuthorResponsesFragment
import java.text.NumberFormat
import java.util.*

class AuthorPagerActivity : BaseActivity<AuthorPagerMvp.View, BasePresenter<AuthorPagerMvp.View>>(),
		AuthorPagerMvp.View {

	@State var index: Int = 0
	@State var authorId: Int = 0
	@State var authorName: String = ""
	@State var tabsCountSet = HashSet<TabsCountStateModel>()
	private val numberFormat = NumberFormat.getNumberInstance()
	private lateinit var toolbarMenu: Menu

	override fun layout(): Int = R.layout.tabbed_pager_layout

	override fun isTransparent(): Boolean = true

	override fun canBack(): Boolean = true

	override fun providePresenter(): BasePresenter<AuthorPagerMvp.View> = BasePresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (savedInstanceState == null) {
			authorId = intent?.extras?.getInt(BundleConstant.EXTRA, -1) ?: -1
			authorName = intent?.extras?.getString(BundleConstant.EXTRA_TWO) ?: ""
			index = intent?.extras?.getInt(BundleConstant.EXTRA_THREE, -1) ?: -1
		}
		if (authorId == -1) {
			finish()
			return
		}
		setTaskName(authorName)
		title = authorName
		selectMenuItem(R.id.mainView, false)
		val adapter = FragmentsPagerAdapter(
				supportFragmentManager,
				FragmentPagerAdapterModel.buildForAuthor(this, authorId)
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
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.author_menu, menu)
		toolbarMenu = menu
		hideShowToolbar(pager.currentItem)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.share -> {
				ActivityHelper.shareUrl(this, Uri.Builder().scheme(LinkParserHelper.PROTOCOL_HTTPS)
						.authority(LinkParserHelper.HOST_DEFAULT)
						.appendPath("autor$authorId")
						.toString())
				return true
			}
			R.id.sort -> {
				val fragment = pager.adapter?.instantiateItem(pager, 3) as? AuthorResponsesFragment
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
		when (position) {
			1 -> fab.hide()/*fab.show()*/
			2 -> fab.hide()/*fab.show()*/
			3 -> fab.hide()/*fab.show()*/
			else -> fab.hide()
		}
	}

	private fun hideShowToolbar(position: Int) {
		if (::toolbarMenu.isInitialized) {
			when (position) {
				0 -> {
					toolbarMenu.findItem(R.id.sort).isVisible = false
					toolbarMenu.findItem(R.id.share).isVisible = true
				}
				3 -> {
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

	private fun setupTab(count: Int, index: Int) {
		val tabView = ViewHelper.getTabView(tabs, index)
		when (index) {
			0 -> tabView.first.text = getString(R.string.overview)
			1 -> {
				tabView.first.text = getString(R.string.bibiography)
			}
			2 -> {
				tabView.first.text = getString(R.string.editions)
				tabView.second.text = count.toString()
			}
			3 -> {
				tabView.first.text = getString(R.string.responses)
				tabView.second.text = count.toString()
			}
		}
	}

	private fun invalidateTabs(adapter: FragmentsPagerAdapter) {
		for (i in 0 until tabs.tabCount) {
			val tab = tabs.getTabAt(i)
			if (tab != null) {
				val custom = tab.customView
				if (custom == null) tab.customView = adapter.getCustomTabView(applicationContext)
				setupTab(0, i)
			}
		}
	}

	companion object {

		fun startActivity(context: Context, authorId: Int, authorName: String, index: Int = -1) {
			val intent = Intent(context, AuthorPagerActivity::class.java)
			intent.putExtras(Bundler.start()
					.put(BundleConstant.EXTRA, authorId)
					.put(BundleConstant.EXTRA_TWO, authorName)
					.put(BundleConstant.EXTRA_THREE, index)
					.end())
			if (context is Service || context is Application) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			}
			context.startActivity(intent)
		}
	}

	override fun onScrolled(isUp: Boolean) {
		if (isUp) {
			fab.hide();
		} else {
			hideShowFab(pager.currentItem)
		}
	}
}