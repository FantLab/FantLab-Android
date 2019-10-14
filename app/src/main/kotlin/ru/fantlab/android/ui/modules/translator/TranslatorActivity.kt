package ru.fantlab.android.ui.modules.translator

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.evernote.android.state.State
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.appbar_tabbed_elevation.*
import ru.fantlab.android.R
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.base.BaseActivity
import kotlinx.android.synthetic.main.tabbed_pager_layout.*
import ru.fantlab.android.data.dao.FragmentPagerAdapterModel
import ru.fantlab.android.data.dao.TabsCountStateModel
import ru.fantlab.android.helper.ViewHelper
import ru.fantlab.android.ui.adapter.FragmentsPagerAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import java.util.HashSet

class TranslatorActivity : BaseActivity<TranslatorMvp.View, BasePresenter<TranslatorMvp.View>>(), TranslatorMvp.View {
    @State var index: Int = 0
    @State var translatorId: Int = 0
    @State var translatorName: String = ""
    @State var tabsCountSet = HashSet<TabsCountStateModel>()

    override fun isTransparent(): Boolean = true

    override fun providePresenter(): BasePresenter<TranslatorMvp.View> = BasePresenter()

    override fun layout(): Int = R.layout.tabbed_pager_layout

    override fun canBack(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            translatorId = intent?.extras?.getInt(BundleConstant.EXTRA, -1) ?: -1
            translatorName = intent?.extras?.getString(BundleConstant.EXTRA_TWO) ?: ""
            index = intent?.extras?.getInt(BundleConstant.EXTRA_THREE, -1) ?: -1
        }

        if (translatorId == -1) {
            finish()
            return
        }

        setTaskName(translatorName)
        title = translatorName

        val adapter = FragmentsPagerAdapter(
            supportFragmentManager,
            FragmentPagerAdapterModel.buildForTranslator(this, translatorId)
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
            }
        })
        if (savedInstanceState != null && !tabsCountSet.isEmpty()) {
            tabsCountSet.forEach { setupTab(count = it.count, index = it.tabIndex) }
        }
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

    private fun setupTab(count: Int, index: Int) {
        val tabView = ViewHelper.getTabView(tabs, index)
        when (index) {
            0 -> tabView.first.text = getString(R.string.overview)
            1 -> {
                tabView.first.text = getString(R.string.bibiography)
                tabView.second.text = count.toString()
            }
        }
    }

    private fun invalidateTabs(adapter: FragmentsPagerAdapter) {
        for (i in 0 until tabs.tabCount) {
            val tab = tabs.getTabAt(i)
            if (tab != null) {
                val custom = tab.customView
                if (custom == null) tab.customView = adapter.getCustomTabView(this)
                setupTab(0, i)
            }
        }
    }

    companion object {

        fun startActivity(context: Context, translatorId: Int, translatorName: String, index: Int = -1) {
            val intent = Intent(context, TranslatorActivity::class.java)
            intent.putExtras(Bundler.start()
                    .put(BundleConstant.EXTRA, translatorId)
                    .put(BundleConstant.EXTRA_TWO, translatorName)
                    .put(BundleConstant.EXTRA_THREE, index)
                    .end())
            if (context is Service || context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }
}