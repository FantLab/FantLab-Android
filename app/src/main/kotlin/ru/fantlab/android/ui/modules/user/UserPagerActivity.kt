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
import butterknife.BindView
import com.evernote.android.state.State
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.FragmentPagerAdapterModel
import ru.fantlab.android.data.dao.TabsCountStateModel
import ru.fantlab.android.data.dao.model.AbstractLogin
import ru.fantlab.android.helper.*
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DEFAULT
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.ui.adapter.FragmentsPagerAdapter
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.modules.main.MainActivity
import ru.fantlab.android.ui.widgets.ViewPagerView
import shortbread.Shortcut
import java.util.*

@Shortcut(id = "profile", icon = R.drawable.ic_profile, shortLabelRes = R.string.profile, backStack = [MainActivity::class], rank = 0)
class UserPagerActivity : BaseActivity<UserPagerMvp.View, BasePresenter<UserPagerMvp.View>>(), UserPagerMvp.View {

	@BindView(R.id.tabs)
	lateinit var tabs: TabLayout

	@BindView(R.id.tabbedPager)
	lateinit var pager: ViewPagerView

	@BindView(R.id.fab)
	lateinit var fab: FloatingActionButton

	@State
	var index: Int = 0

	@State
	var login: String? = null

	@State
	var userId: Int = 0

	@State
	var counts = HashSet<TabsCountStateModel>()

	override fun layout(): Int = R.layout.tabbed_pager_layout

	override fun isTransparent(): Boolean = true

	override fun canBack(): Boolean = true

	override fun providePresenter(): BasePresenter<UserPagerMvp.View> = BasePresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val currentUser = AbstractLogin.getUser()
		if (currentUser == null) {
			onRequireLogin()
			return
		}
		if (savedInstanceState == null) {
			userId = intent?.extras?.getInt(BundleConstant.EXTRA_TWO, -1) ?: -1
			index = intent?.extras?.getInt(BundleConstant.EXTRA_THREE, -1) ?: -1
			login = intent?.extras?.getString(BundleConstant.EXTRA)
			if (login == null) {
				login = currentUser.login
			}
		}
		if (InputHelper.isEmpty(login)) {
			finish()
			return
		}
		setTaskName(login)
		title = login
		if (login.equals(currentUser.login, ignoreCase = true)) {
			selectProfile()
		}
		val adapter = FragmentsPagerAdapter(
				supportFragmentManager,
				FragmentPagerAdapterModel.buildForProfile(this, login!!)
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
		if (savedInstanceState != null && !counts.isEmpty()) {
			counts.forEach { count -> updateCount(count) }
		}
		hideShowFab(pager.currentItem)
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.share_menu, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == R.id.share && userId != -1) {
			ActivityHelper.shareUrl(this, Uri.Builder().scheme(PROTOCOL_HTTPS)
					.authority(HOST_DEFAULT)
					.appendPath("user$userId")
					.toString())
			return true
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onScrollTop(index: Int) {
		if (pager.adapter == null) return
		val fragment = pager.adapter?.instantiateItem(pager, index) as? BaseFragment<*,*>
		if (fragment is BaseFragment) {
			fragment.onScrollTop(index)
		}
	}

	override fun onSetBadge(tabIndex: Int, count: Int) {
		val model = TabsCountStateModel()
		model.tabIndex = tabIndex
		model.count = count
		counts.add(model)
		updateCount(model)
	}

	private fun hideShowFab(position: Int) {
		if (position == 1) {
			fab.show()
		} else {
			fab.hide()
		}
	}

	private fun updateCount(model: TabsCountStateModel) {
		val tv = ViewHelper.getTabTextView(tabs, model.tabIndex)
		/*tv.text = SpannableBuilder.builder()
				.append(text = getString(R.string.starred))
				.append(text = "   ")
				.append(text = "(")
				.bold(model.count.toString())
				.append(text = ")")*/
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