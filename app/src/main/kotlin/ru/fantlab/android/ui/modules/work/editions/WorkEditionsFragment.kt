package ru.fantlab.android.ui.modules.work.editions

import android.content.Context
import android.os.Bundle
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
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.edition.EditionPagerActivity
import ru.fantlab.android.ui.modules.work.WorkPagerMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView

class WorkEditionsFragment : BaseFragment<WorkEditionsMvp.View, WorkEditionsPresenter>(),
		WorkEditionsMvp.View {

	private val adapter: EditionsAdapter by lazy { EditionsAdapter(arrayListOf()) }
	private var countCallback: WorkPagerMvp.View? = null

	private var languages: ArrayList<Pair<String, String>> = arrayListOf()
	private var types: ArrayList<Pair<String, String>> = arrayListOf()
	private var editionBlocks: EditionsBlocks? = null

	@State
	var currentFilter: Pair<String, String> = Pair("all", "all")

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list

	override fun providePresenter() = WorkEditionsPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			stateLayout.hideProgress()
		}
		stateLayout.setEmptyText(R.string.no_editions)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		adapter.listener = presenter
		recycler.adapter = adapter
		presenter.onFragmentCreated(arguments!!)
		fastScroller.attachRecyclerView(recycler)
	}

	override fun onInitViews(editions: EditionsBlocks?, editionsInfo: EditionsInfo) {
		hideProgress()
		onSetTabCount(editionsInfo.allCount)
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
		editions?.editionsBlocks?.let { block ->
			val blocks = if (currentFilter.second != "all") block.filter { it.name == currentFilter.second } else block
			blocks.forEach {
				if (currentFilter.first != "all") {
					adapter.insertItems(it.list.filter { it.languageCode == currentFilter.first })
				} else adapter.insertItems(it.list)
			}
		}
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (context is WorkPagerMvp.View) {
			countCallback = context
		}
	}

	override fun onDetach() {
		countCallback = null
		super.onDetach()
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		refresh.isRefreshing = true
		stateLayout.showProgress()
	}

	override fun hideProgress() {
		refresh.isRefreshing = false
		stateLayout.showReload(adapter.itemCount)
	}

	override fun showErrorMessage(msgRes: String?) {
		hideProgress()
		super.showErrorMessage(msgRes)
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		hideProgress()
		super.showMessage(titleRes, msgRes)
	}

	override fun onRefresh() {
		presenter.getEditions(true)
	}

	override fun onItemClicked(item: EditionsBlocks.Edition) {
		EditionPagerActivity.startActivity(context!!, item.editionId, item.name, 0)
	}

	override fun onClick(v: View?) {
		onRefresh()
	}

	override fun onSetTabCount(allCount: Int) {
		countCallback?.onSetBadge(3, allCount)
	}

	fun showFilterDialog() {
		val dialogView = ContextMenuDialogView()
		dialogView.initArguments("main", ContextMenuBuilder.buildForEditionsFilter(recycler.context, languages, types, currentFilter))
		dialogView.show(childFragmentManager, "ContextMenuDialogView")
	}

	override fun onItemSelected(parent: String, item: ContextMenus.MenuItem, position: Int, listItem: Any) {
		when (parent) {
			"lang" -> currentFilter = Pair(item.id, "all")
			"type" -> currentFilter = Pair("all", item.id)
		}
		initData(editionBlocks)
	}

	companion object {

		fun newInstance(workId: Int): WorkEditionsFragment {
			val view = WorkEditionsFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, workId).end()
			return view
		}
	}
}