package ru.fantlab.android.ui.modules.user

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.evernote.android.state.State
import com.google.android.material.tabs.TabLayout
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.appbar_tabbed_elevation.*
import kotlinx.android.synthetic.main.tabbed_pager_layout.*
import ru.fantlab.android.App
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.FragmentPagerAdapterModel
import ru.fantlab.android.data.dao.TabsCountStateModel
import ru.fantlab.android.helper.*
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DEFAULT
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.ui.adapter.FragmentsPagerAdapter
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.modules.bookcases.editor.BookcaseEditorActivty
import ru.fantlab.android.ui.modules.bookcases.overview.BookcasesOverviewFragment
import ru.fantlab.android.ui.modules.editor.EditorActivity
import ru.fantlab.android.ui.modules.login.LoginActivity
import ru.fantlab.android.ui.modules.profile.marks.ProfileMarksFragment
import shortbread.Shortcut
import java.text.NumberFormat
import java.util.*


@Shortcut(id = "profile", icon = R.drawable.sb_profile, shortLabelRes = R.string.profile, rank = 0)
class UserPagerActivity : BaseActivity<UserPagerMvp.View, BasePresenter<UserPagerMvp.View>>(), UserPagerMvp.View {

	@State var index: Int = 0
	@State var login: String? = null
	@State var userId: Int = 0
	@State var tabsCountSet = HashSet<TabsCountStateModel>()
	private lateinit var toolbarMenu: Menu
	private val numberFormat = NumberFormat.getNumberInstance()
	private var isError = false
	private var adapter: FragmentsPagerAdapter? = null

	override fun layout(): Int = R.layout.tabbed_pager_layout

	override fun isTransparent(): Boolean = true

	override fun canBack(): Boolean = true

	override fun providePresenter(): BasePresenter<UserPagerMvp.View> = BasePresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val currentUser = PrefGetter.getLoggedUser()
		if (savedInstanceState == null) {
			userId = intent?.extras?.getInt(BundleConstant.EXTRA_TWO, -1) ?: -1
			index = intent?.extras?.getInt(BundleConstant.EXTRA_THREE, -1) ?: -1
			login = intent?.extras?.getString(BundleConstant.EXTRA)
			if (login == null) {
				login = currentUser?.login
			}
			if (userId == -1) {
				userId = currentUser?.id ?: -1
			}
		}
		if (currentUser == null && userId == -1) {
			Toasty.error(App.instance, getString(R.string.unauthorized_user), Toast.LENGTH_LONG).show()
			startActivity(Intent(this, LoginActivity::class.java))
			finish()
			return
		} else if (InputHelper.isEmpty(login) || userId == -1) {
			finish()
			return
		}
		setTaskName(login)
		title = login
		if (login == currentUser?.login) {
			selectMenuItem(R.id.profile, true)
		}
		adapter = FragmentsPagerAdapter(
				supportFragmentManager,
				FragmentPagerAdapterModel.buildForProfile(this, userId)
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

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.profile_marks_menu, menu)
		toolbarMenu = menu
		hideShowToolbar(pager.currentItem)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.share -> {
				if (userId != -1) {
					ActivityHelper.shareUrl(this, Uri.Builder().scheme(PROTOCOL_HTTPS)
							.authority(HOST_DEFAULT)
							.appendPath("user$userId")
							.toString())
					return true
				}
			}
			R.id.sort -> {
				val fragment = pager.adapter?.instantiateItem(pager, 1) as? ProfileMarksFragment
				fragment?.showSortDialog()
			}
			R.id.filter -> {
				val fragment = pager.adapter?.instantiateItem(pager, 1) as? ProfileMarksFragment
				fragment?.showFilterDialog()
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

	override fun onSelectTab(tabIndex: Int) {
		pager.currentItem = tabIndex
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (resultCode == Activity.RESULT_OK) {
			if ((requestCode == BundleConstant.BOOKCASE_EDITOR || requestCode == BundleConstant.BOOKCASE_VIEWER)
					&& adapter!!.getItemKey(pager.currentItem) == getString(R.string.bookcases)) {
				val fragment = pager.adapter?.instantiateItem(pager, pager.currentItem) as? BookcasesOverviewFragment
				fragment?.onRefresh()
			}
		}
		super.onActivityResult(requestCode, resultCode, data)
	}

	private fun hideShowFab(position: Int) {
		if (isError) {
			fab.hide()
			return
		}
		when (adapter!!.getItemKey(position)) {
			getString(R.string.overview) -> {
				if (isLoggedIn() && userId != PrefGetter.getLoggedUser()?.id) {
					fab.setImageResource(R.drawable.ic_message)
					fab.show()
				} else fab.hide()
			}
			getString(R.string.marks) -> {
				fab.setImageResource(R.drawable.ic_charts)
				fab.show()
			}
			getString(R.string.responses) -> fab.hide()/*fab.show()*/
			getString(R.string.bookcases) -> {
				fab.setImageResource(R.drawable.ic_add)
				fab.show()
			}
			else -> fab.hide()
		}
	}

	override fun onError() {
		isError = true
		fab.hide()
	}


	private fun onFabClicked() {
		when (adapter!!.getItemKey(pager.currentItem)) {
			getString(R.string.overview) -> {
				startActivity(Intent(this, EditorActivity::class.java)
						.putExtra(BundleConstant.EXTRA_TYPE, BundleConstant.EDITOR_NEW_MESSAGE)
						.putExtra(BundleConstant.ID, userId))
			}
			getString(R.string.marks) -> {
				val fragment = pager.adapter?.instantiateItem(pager, 1) as? ProfileMarksFragment
				fragment?.showChartsDialog()
			}
			getString(R.string.bookcases) -> {
				BookcaseEditorActivty.startActivityForCreation(this)
			}
		}
	}

	private fun setupTab(count: Int, index: Int) {
		val textView = ViewHelper.getTabTextView(tabs, index)
		when (adapter!!.getItemKey(index)) {
			getString(R.string.marks) -> textView.text = String.format("%s (%s)", getString(R.string.marks), numberFormat.format(count.toLong()))
			getString(R.string.responses) -> textView.text = String.format("%s (%s)", getString(R.string.responses), numberFormat.format(count.toLong()))
			getString(R.string.bookcases) -> textView.text = String.format("%s (%s)", getString(R.string.bookcases), numberFormat.format(count.toLong()))
		}
	}

	private fun hideShowToolbar(position: Int) {
		if (::toolbarMenu.isInitialized) {
			when (position) {
				1 -> {
					toolbarMenu.findItem(R.id.share).isVisible = false
					toolbarMenu.findItem(R.id.sort).isVisible = true
					toolbarMenu.findItem(R.id.filter).isVisible = true
				}
				else -> {
					toolbarMenu.findItem(R.id.share).isVisible = true
					toolbarMenu.findItem(R.id.sort).isVisible = false
					toolbarMenu.findItem(R.id.filter).isVisible = false
				}
			}
		}
	}

	companion object {

		fun startActivity(context: Context, login: String, userId: Int, index: Int = -1) {
			val intent = Intent(context, UserPagerActivity::class.java)
			intent.putExtras(Bundler.start()
					.put(BundleConstant.EXTRA, login)
					.put(BundleConstant.EXTRA_TWO, userId)
					.put(BundleConstant.EXTRA_THREE, index)
					.end())
			if (context is Service || context is Application) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			}
			context.startActivity(intent)
		}
	}
}
