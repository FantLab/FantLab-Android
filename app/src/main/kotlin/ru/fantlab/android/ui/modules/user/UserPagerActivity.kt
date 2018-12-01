package ru.fantlab.android.ui.modules.user

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import com.evernote.android.state.State
import es.dmoral.toasty.Toasty
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
import ru.fantlab.android.ui.modules.editor.EditorActivity
import ru.fantlab.android.ui.modules.login.LoginActivity
import ru.fantlab.android.ui.widgets.ViewPagerView
import shortbread.Shortcut
import java.text.NumberFormat
import java.util.*


@Shortcut(id = "profile", icon = R.drawable.sb_profile, shortLabelRes = R.string.profile, rank = 0)
class UserPagerActivity : BaseActivity<UserPagerMvp.View, BasePresenter<UserPagerMvp.View>>(), UserPagerMvp.View {

	@BindView(R.id.tabs) lateinit var tabs: TabLayout
	@BindView(R.id.tabbedPager) lateinit var pager: ViewPagerView
	@BindView(R.id.fab) lateinit var fab: FloatingActionButton

	@State var index: Int = 0
	@State var login: String? = null
	@State var userId: Int = 0
	@State var tabsCountSet = HashSet<TabsCountStateModel>()
	private val numberFormat = NumberFormat.getNumberInstance()
	private var isError = false

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
		val adapter = FragmentsPagerAdapter(
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
			}
		})
		if (savedInstanceState != null && !tabsCountSet.isEmpty()) {
			tabsCountSet.forEach { setupTab(count = it.count, index = it.tabIndex) }
		}
		hideShowFab(pager.currentItem)
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.share_menu, menu)
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

	private fun hideShowFab(position: Int) {
		if (isError) {
			fab.hide()
			return
		}
		when (position) {
			0 -> {
				if (userId != PrefGetter.getLoggedUser()?.id) {
					fab.setImageResource(R.drawable.ic_message)
					fab.show()
				} else fab.hide()
			}
			1 -> fab.hide()/*fab.show()*/
			2 -> fab.hide()/*fab.show()*/
			else -> fab.hide()
		}
	}

	override fun onError() {
		isError = true
		fab.hide()
	}

	@OnClick(R.id.fab)
	fun onFabClicked() {
		startActivity(Intent(this, EditorActivity::class.java)
				.putExtra(BundleConstant.EXTRA_TYPE, BundleConstant.EDITOR_NEW_MESSAGE)
				.putExtra(BundleConstant.ID, userId))
	}

	private fun setupTab(count: Int, index: Int) {
		val textView = ViewHelper.getTabTextView(tabs, index)
		when (index) {
			1 -> textView.text = String.format("%s (%s)", getString(R.string.marks), numberFormat.format(count.toLong()))
			2 -> textView.text = String.format("%s (%s)", getString(R.string.responses), numberFormat.format(count.toLong()))
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