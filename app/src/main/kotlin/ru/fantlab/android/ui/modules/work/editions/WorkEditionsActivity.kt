package ru.fantlab.android.ui.modules.work.editions

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.ContextMenuBuilder
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.data.dao.model.EditionsBlocks
import ru.fantlab.android.data.dao.model.EditionsInfo
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.EditionsAdapter
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.modules.edition.EditionActivity
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView

class WorkEditionsActivity : BaseActivity<WorkEditionsMvp.View, WorkEditionsPresenter>(), WorkEditionsMvp.View {

	@State var workId: Int = 0
	@State var workName: String = ""

	private val adapter: EditionsAdapter by lazy { EditionsAdapter(arrayListOf()) }

	private var languages: ArrayList<Pair<String, String>> = arrayListOf()
	private var types: ArrayList<Pair<String, String>> = arrayListOf()
	private var editionBlocks: EditionsBlocks? = null

	@State
	var currentFilter: Pair<String, String> = Pair("all", "all")

	override fun layout(): Int = R.layout.work_editions_layout

	override fun isTransparent(): Boolean = true

	override fun canBack(): Boolean = true

	override fun providePresenter() = WorkEditionsPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		hideShowShadow(true)
		if (savedInstanceState == null) {
			workId = intent?.extras?.getInt(BundleConstant.EXTRA, -1) ?: -1
			workName = intent?.extras?.getString(BundleConstant.EXTRA_TWO) ?: ""
		}
		if (workId == -1) {
			finish()
			return
		}
		setTaskName(workName)
		title = workName
		toolbar?.subtitle = getString(R.string.editions)
		if (savedInstanceState == null) {
			stateLayout.hideProgress()
		}
		stateLayout.setEmptyText(R.string.no_editions)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		adapter.listener = presenter
		recycler.adapter = adapter
		presenter.onCallApi(workId)
		fastScroller.attachRecyclerView(recycler)
	}

	override fun onInitViews(editions: EditionsBlocks?, editionsInfo: EditionsInfo) {
		hideProgress()
		editionBlocks = editions
		initData(editions)
		initFilterData(editions)
	}

	private fun initFilterData(editions: EditionsBlocks?) {
		types.clear()
		languages.clear()

		editions?.editionsBlocks?.let { blocks ->
			blocks.forEach {
				types.add(Pair(it.title, it.name))

				it.list.forEach { edition ->
					val notContains = languages.none { it.second == edition.languageCode }
					if (notContains) languages.add(Pair(edition.language, edition.languageCode))
				}
			}
		}
	}

	private fun initData(editions: EditionsBlocks?) {
		adapter.clear()
		editions?.editionsBlocks?.let { block ->
			val blocks = if (currentFilter.second != "all") block.filter { it.name == currentFilter.second } else block
			blocks.forEach {
				if (currentFilter.first != "all") {
					adapter.addItems(it.list.filter { it.languageCode == currentFilter.first })
				} else adapter.addItems(it.list)
			}
		}
	}

	override fun onRefresh() {
		presenter.getEditions(true)
	}

	override fun onClick(v: View?) {
		onRefresh()
	}

	override fun hideProgress() {
		refresh.isRefreshing = false
		stateLayout.hideProgress()
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		refresh.isRefreshing = true
		stateLayout.showProgress()
	}

	private fun showFilterDialog() {
		val dialogView = ContextMenuDialogView()
		dialogView.initArguments("main", ContextMenuBuilder.buildForEditionsFilter(recycler.context, languages, types, currentFilter))
		dialogView.show(supportFragmentManager, "ContextMenuDialogView")
	}

	override fun onItemSelected(parent: String, item: ContextMenus.MenuItem, position: Int, listItem: Any) {
		when (parent) {
			"lang" -> currentFilter = Pair(item.id, "all")
			"type" -> currentFilter = Pair("all", item.id)
		}
		initData(editionBlocks)
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.editions_menu, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.filter -> {
				showFilterDialog()
			}
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onItemClicked(item: EditionsBlocks.Edition) {
		EditionActivity.startActivity(this, item.editionId, item.name)
	}

	companion object {

		fun startActivity(context: Context, workId: Int, workName: String) {
			val intent = Intent(context, WorkEditionsActivity::class.java)
			intent.putExtras(Bundler.start()
					.put(BundleConstant.EXTRA, workId)
					.put(BundleConstant.EXTRA_TWO, workName)
					.end())
			if (context is Service || context is Application) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			}
			context.startActivity(intent)
		}
	}
}