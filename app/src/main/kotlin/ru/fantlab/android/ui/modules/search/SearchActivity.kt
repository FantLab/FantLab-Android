package ru.fantlab.android.ui.modules.search

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import butterknife.BindView
import butterknife.OnClick
import butterknife.OnEditorAction
import com.evernote.android.state.State
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.FragmentPagerAdapterModel
import ru.fantlab.android.data.dao.TabsCountStateModel
import ru.fantlab.android.helper.AnimHelper
import ru.fantlab.android.helper.ViewHelper
import ru.fantlab.android.ui.adapter.FragmentsPagerAdapter
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.widgets.FontAutoCompleteEditText
import ru.fantlab.android.ui.widgets.ForegroundImageView
import ru.fantlab.android.ui.widgets.ViewPagerView
import java.text.NumberFormat
import java.util.*

class SearchActivity : BaseActivity<SearchMvp.View, SearchPresenter>(), SearchMvp.View {

	@BindView(R.id.searchEditText)
	lateinit var searchEditText: FontAutoCompleteEditText

	@BindView(R.id.clear)
	lateinit var clear: ForegroundImageView

	@BindView(R.id.tabs)
	lateinit var tabs: TabLayout

	@BindView(R.id.pager)
	lateinit var pager: ViewPagerView

	@State
	var tabsCountSet: HashSet<TabsCountStateModel> = LinkedHashSet<TabsCountStateModel>()

	private val numberFormat = NumberFormat.getNumberInstance()
	private val adapter: ArrayAdapter<String> by lazy {
		ArrayAdapter(this, android.R.layout.simple_list_item_1, presenter.getHints())
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		title = ""
		pager.adapter = FragmentsPagerAdapter(supportFragmentManager, FragmentPagerAdapterModel.buildForSearch(this))
		tabs.setupWithViewPager(pager)
		searchEditText.setAdapter(adapter)
		searchEditText.setOnItemClickListener { _, _, _, _ -> presenter.onSearchClicked(pager, searchEditText) }
		searchEditText.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(s: Editable?) {
				val text = s.toString()
				AnimHelper.animateVisibility(clear, text.isNotEmpty())
			}

			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
			}

			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
			}
		})
		if (!tabsCountSet.isEmpty()) {
			for (model in tabsCountSet) {
				setupTab(count = model.count, index = model.tabIndex)
			}
		}
		if (savedInstanceState == null && intent != null) {
			if (intent.hasExtra("search")) {
				searchEditText.setText(intent.getStringExtra("search"))
				presenter.onSearchClicked(pager, searchEditText)
			}
		}
		tabs.addOnTabSelectedListener(object : TabLayout.ViewPagerOnTabSelectedListener(pager) {
			override fun onTabReselected(tab: TabLayout.Tab) {
				super.onTabReselected(tab)
				onScrollTop(tab.position)
			}
		})
		clear.setOnClickListener { searchEditText.setText("") }
	}

	override fun layout(): Int = R.layout.search_layout

	override fun isTransparent(): Boolean = false

	override fun canBack(): Boolean = true

	override fun providePresenter(): SearchPresenter = SearchPresenter()

	override fun isSecured(): Boolean = false

	override fun onNotifyAdapter(query: String?) {
		if (query == null)
			adapter.notifyDataSetChanged()
		else
			adapter.add(query)
	}

	override fun onSetCount(count: Int, index: Int) {
		val model = TabsCountStateModel(count = count, tabIndex = index)
		tabsCountSet.add(model)
		setupTab(count, index)
	}

	@OnClick(R.id.search)
	fun onSearchClicked() {
		presenter.onSearchClicked(pager, searchEditText)
	}

	@OnEditorAction(R.id.searchEditText)
	fun onEditor(): Boolean {
		onSearchClicked()
		return true
	}

	private fun setupTab(count: Int, index: Int) {
		val textView = ViewHelper.getTabTextView(tabs, index)
		when (index) {
			0 -> textView.text = String.format("%s(%s)", getString(R.string.authors), numberFormat.format(count.toLong()))
			1 -> textView.text = String.format("%s(%s)", getString(R.string.works), numberFormat.format(count.toLong()))
			2 -> textView.text = String.format("%s(%s)", getString(R.string.editions), numberFormat.format(count.toLong()))
			3 -> textView.text = String.format("%s(%s)", getString(R.string.awards), numberFormat.format(count.toLong()))
		}
	}
}