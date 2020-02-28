package ru.fantlab.android.ui.modules.translator.works

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.ContextMenuBuilder
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.data.dao.model.TranslatedWorkHeader
import ru.fantlab.android.data.dao.model.TranslatedWorkItem
import ru.fantlab.android.data.dao.model.Translator
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.provider.rest.TranslationsSortOption
import ru.fantlab.android.ui.adapter.viewholder.TranslatedWorkHeaderViewHolder
import ru.fantlab.android.ui.adapter.viewholder.TranslatedWorkItemViewHolder
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.translator.TranslatorMvp
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class TranslatorWorksFragment : BaseFragment<TranslatorWorksMvp.View, TranslatorWorksPresenter>(),
        TranslatorWorksMvp.View {

    private var translatorId = -1
    private var countCallback: TranslatorMvp.View? = null

    override fun fragmentLayout(): Int = R.layout.micro_grid_refresh_list

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        stateLayout.hideReload()
        translatorId = arguments!!.getInt(BundleConstant.EXTRA)
        stateLayout.setEmptyText(R.string.noTranslations)
        stateLayout.setOnReloadListener(this)
        refresh.setOnRefreshListener(this)
        recycler.setEmptyView(stateLayout, refresh)

        presenter.getTranslatorWorks(translatorId)
    }

    override fun providePresenter() = TranslatorWorksPresenter()

    override fun onTranslatorInformationRetrieved(translatedWorks: HashMap<String, Translator.TranslatedWork>) {
        hideProgress()
        onSetTabCount(translatedWorks.size)
        val nodes = buildNodes(translatedWorks, TranslatedWorkSorting(presenter.getCurrentSort()))

        val adapter = TreeViewAdapter(nodes, Arrays.asList(TranslatedWorkItemViewHolder(), TranslatedWorkHeaderViewHolder()))
        if (recycler.adapter == null) {
            recycler.adapter = adapter
            adapter.setOnTreeNodeListener(object : TreeViewAdapter.OnTreeNodeListener {
                override fun onSelected(extra: Int, add: Boolean) {
                }

                override fun onClick(node: TreeNode<*>, holder: RecyclerView.ViewHolder): Boolean {
                    if (!node.isLeaf) {
                        onToggle(!node.isExpand, holder)
                    } else if (node.isLeaf && node.content is TranslatedWorkHeader) {
                        return false
                    } else {
                        val item = node.content as TranslatedWorkItem
                        WorkPagerActivity.startActivity(activity!!,
                                item.translatedWork.work.id,
                                item.translatedWork.work.name)
                    }
                    return false
                }

                override fun onToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder) {
                    val viewHolder = holder as TranslatedWorkHeaderViewHolder.ViewHolder
                    val ivArrow = viewHolder.expandButton
                    val rotateDegree = if (isExpand) 90.0f else -90.0f
                    ivArrow.animate().rotationBy(rotateDegree)
                            .start()
                }
            })
            fastScroller.attachRecyclerView(recycler)
        }
        else
            (recycler.adapter as TreeViewAdapter).refresh(nodes)

    }

    private fun buildNodes(translatedWorks: HashMap<String, Translator.TranslatedWork>, sortingStrategy: TranslatedWorkSorting): ArrayList<TreeNode<*>> {
        val nodes = arrayListOf<TreeNode<*>>()
        val years: HashSet<String> = HashSet()
        for (work in translatedWorks.values) {
            if (sortingStrategy.getCategoryName(work).isNotEmpty()) {
                years.add(sortingStrategy.getCategoryName(work))
            }
       }
        val categories = years.toSortedSet()
        categories.forEachIndexed { subIndex, category ->
            val categoryNode = TreeNode(TranslatedWorkHeader(category))
            nodes.add(categoryNode)
            translatedWorks.forEach{ work ->
                if (sortingStrategy.isIncludedIntoCategory(work.value, category)) {
                    nodes[subIndex].addChild(TreeNode(TranslatedWorkItem(work.value)))
                }
            }
            categoryNode.expandAll()
        }
        return nodes
    }

    override fun onSetTabCount(allCount: Int) {
        countCallback?.onSetBadge(1, allCount)
    }

    override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
        refresh.isRefreshing = true
        stateLayout.showProgress()
    }

    override fun hideProgress() {
        refresh.isRefreshing = false
        stateLayout.showReload(recycler.adapter?.itemCount ?: 0)
    }

    override fun showErrorMessage(msgRes: String?) {
        hideProgress()
        super.showErrorMessage(msgRes)
    }

    override fun onRefresh() {
        presenter.getTranslatorWorks(translatorId)
    }

    override fun onClick(p0: View?) {
        onRefresh()
    }

    override fun onItemSelected(parent: String, item: ContextMenus.MenuItem, position: Int, listItem: Any) {
        presenter.setCurrentSort(TranslationsSortOption.values()[position])
    }

    override fun showMessage(titleRes: Int, msgRes: Int) {
        hideProgress()
        super.showMessage(titleRes, msgRes)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TranslatorMvp.View) {
            countCallback = context
        }
    }

    override fun onDetach() {
        countCallback = null
        super.onDetach()
    }

    fun showSortDialog() {
        val dialogView = ContextMenuDialogView()
        val sort = presenter.getCurrentSort()
        dialogView.initArguments("sort", ContextMenuBuilder.buildForTranslatorWorksSorting(recycler.context, sort))
        dialogView.show(childFragmentManager, "ContextMenuDialogView")
    }

    class TranslatedWorkSorting (var sortBy: TranslationsSortOption) {
        fun getCategoryName(work: Translator.TranslatedWork): String {
            var resName = "";
            when (sortBy) {
                TranslationsSortOption.BY_YEAR -> {
                    if (work.editions.isNotEmpty()) {
                        resName = work.editions[0].year.toString()
                    }
                }
                TranslationsSortOption.BY_AUTHOR -> {
                    resName = work.author.name
                }
                TranslationsSortOption.BY_TYPE -> {
                    resName = work.work.nameType
                }
            }

            return resName
        }

        fun isIncludedIntoCategory(work: Translator.TranslatedWork, category: String): Boolean {
            when (sortBy) {
                TranslationsSortOption.BY_YEAR -> {
                    if (work.editions.isNotEmpty() && work.editions[0].year.toString() == category) {
                        return true
                    }
                }
                TranslationsSortOption.BY_AUTHOR -> {
                    if (work.author.name == category) {
                        return true;
                    }
                }
                TranslationsSortOption.BY_TYPE -> {
                    if (work.work.nameType == category) {
                        return true;
                    }
                }
            }

            return false
        }
    }

    companion object {

        fun newInstance(translatorId: Int): TranslatorWorksFragment {
            val view = TranslatorWorksFragment()
            view.arguments = Bundler.start().put(BundleConstant.EXTRA, translatorId).end()
            return view
        }
    }
}