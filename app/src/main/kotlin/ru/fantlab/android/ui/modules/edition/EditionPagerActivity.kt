package ru.fantlab.android.ui.modules.edition

import android.app.Activity
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
import ru.fantlab.android.helper.*
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.adapter.FragmentsPagerAdapter
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.modules.bookcases.editor.BookcaseEditorActivty
import ru.fantlab.android.ui.modules.bookcases.selector.BookcaseSelectorFragment
import java.text.NumberFormat
import java.util.*

class EditionPagerActivity : BaseActivity<EditionPagerMvp.View, BasePresenter<EditionPagerMvp.View>>(),
		EditionPagerMvp.View {

	@State var index: Int = 0
	@State var editionId: Int = 0
	@State var editionName: String = ""
	@State var tabsCountSet = HashSet<TabsCountStateModel>()
	private val numberFormat = NumberFormat.getNumberInstance()
	private var adapter: FragmentsPagerAdapter? = null

	override fun layout(): Int = R.layout.tabbed_pager_layout

	override fun isTransparent(): Boolean = true

	override fun canBack(): Boolean = true

	override fun providePresenter(): BasePresenter<EditionPagerMvp.View> = BasePresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (savedInstanceState == null) {
			editionId = intent?.extras?.getInt(BundleConstant.EXTRA, -1) ?: -1
			editionName = intent?.extras?.getString(BundleConstant.EXTRA_TWO) ?: ""
			index = intent?.extras?.getInt(BundleConstant.EXTRA_THREE, -1) ?: -1
		}
		if (editionId == -1) {
			finish()
			return
		}
		setTaskName(editionName)
		title = editionName
		selectMenuItem(R.id.mainView, false)
		val currentUser = PrefGetter.getLoggedUser()
		adapter = if (currentUser == null) {
			FragmentsPagerAdapter(
					supportFragmentManager,
					FragmentPagerAdapterModel.buildForEdition(this, editionId)
			)
		}
		else {
			FragmentsPagerAdapter(
					supportFragmentManager,
					FragmentPagerAdapterModel.buildForEdition(this, editionId, currentUser.id)
			)
		}
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
		pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
			override fun onPageSelected(position: Int) {
				super.onPageSelected(position)
				hideShowFab(position)
			}
		})
		if (savedInstanceState != null && !tabsCountSet.isEmpty()) {
			tabsCountSet.forEach { setupTab(count = it.count, index = it.tabIndex) }
		}
		hideShowFab(pager.currentItem)
		fab.setOnClickListener { onFabClicked() }
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.share_menu, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.share -> {
				ActivityHelper.shareUrl(this, Uri.Builder().scheme(LinkParserHelper.PROTOCOL_HTTPS)
						.authority(LinkParserHelper.HOST_DEFAULT)
						.appendPath("edition$editionId")
						.toString())
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

	override fun onSetBadge(tabIndex: Int, count: Int) {
		tabsCountSet.add(TabsCountStateModel(count = count, tabIndex = tabIndex))
		setupTab(count, tabIndex)
	}

	override fun onSetTitle(title: String) {
		this.title = title
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == BundleConstant.BOOKCASE_EDITOR
					&& adapter!!.getItemKey(pager.currentItem) == getString(R.string.bookcases)) {
				val fragment = pager.adapter?.instantiateItem(pager, pager.currentItem) as? BookcaseSelectorFragment
				fragment?.onRefresh()
			}
		}
		super.onActivityResult(requestCode, resultCode, data)
	}

	private fun hideShowFab(position: Int) {
		when (adapter!!.getItemKey(position)) {
			getString(R.string.my_bookcases) -> {
				fab.setImageResource(R.drawable.ic_add)
				fab.show()
			}
			else -> fab.hide()
		}
	}

	private fun onFabClicked() {
		when (adapter!!.getItemKey(pager.currentItem)) {
			getString(R.string.my_bookcases) -> {
				startActivityForResult(Intent(this, BookcaseEditorActivty::class.java)
						.putExtra(BundleConstant.ID, PrefGetter.getLoggedUser()), BundleConstant.BOOKCASE_EDITOR)
			}
		}
	}

	private fun setupTab(count: Int, index: Int) {
		val textView = ViewHelper.getTabTextView(tabs, index)
		when (adapter!!.getItemKey(index)) {
			getString(R.string.content) -> textView.text = String.format("%s(%s)", getString(R.string.content), numberFormat.format(count.toLong()))
			getString(R.string.photos) -> textView.text = String.format("%s(%s)", getString(R.string.photos), numberFormat.format(count.toLong()))
		}
	}

	companion object {

		fun startActivity(context: Context, editionId: Int, editionName: String, index: Int = -1) {
			val intent = Intent(context, EditionPagerActivity::class.java)
			intent.putExtras(Bundler.start()
					.put(BundleConstant.EXTRA, editionId)
					.put(BundleConstant.EXTRA_TWO, editionName)
					.put(BundleConstant.EXTRA_THREE, index)
					.end())
			if (context is Service || context is Application) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			}
			context.startActivity(intent)
		}
	}
}