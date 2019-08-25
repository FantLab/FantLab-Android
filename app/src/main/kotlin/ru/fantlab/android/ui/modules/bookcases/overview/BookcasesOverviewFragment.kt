package ru.fantlab.android.ui.modules.bookcases.overview

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import ru.fantlab.android.R
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.data.dao.model.Bookcase
import ru.fantlab.android.data.dao.model.BookcaseCategory
import ru.fantlab.android.data.dao.model.BookcaseChild
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.ui.adapter.viewholder.BookcaseHeaderViewHolder
import ru.fantlab.android.ui.adapter.viewholder.BookcaseViewHolder
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.bookcases.viewer.BookcaseViewerActivity
import ru.fantlab.android.ui.modules.user.UserPagerMvp
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import java.util.*

class BookcasesOverviewFragment : BaseFragment<BookcasesOverviewMvp.View, BookcasesOverviewPresenter>(),
        BookcasesOverviewMvp.View {

    private var countCallback: UserPagerMvp.View? = null
    private var categories: ArrayList<Pair<String, String>> = arrayListOf()

    override fun fragmentLayout(): Int = R.layout.micro_grid_refresh_list

    override fun providePresenter(): BookcasesOverviewPresenter = BookcasesOverviewPresenter()

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        categories.add(Pair("work", getString(R.string.bookcase_work)))
        categories.add(Pair("edition", getString(R.string.bookcase_edition)))
        categories.add(Pair("film", getString(R.string.bookcase_film)))

        if (savedInstanceState == null) {
            stateLayout.hideProgress()
        }
        stateLayout.setEmptyText(R.string.no_bookcases)
        stateLayout.setOnReloadListener(this)
        refresh.setOnRefreshListener(this)
        recycler.setEmptyView(stateLayout, refresh)
        presenter.onFragmentCreated()
    }

    override fun onInitViews(items: ArrayList<Bookcase>?) {
        hideProgress()
        if (items != null) {
            onSetTabCount(items.size)
            initAdapter(items)
        }
    }

    private fun initAdapter(bookcases: ArrayList<Bookcase>) {
        val nodes = arrayListOf<TreeNode<*>>()
        categories.forEachIndexed { subIndex, category ->
            val categoryNode = TreeNode(BookcaseCategory(category.second))
            nodes.add(categoryNode)
            bookcases.forEach{ bookcase ->
                if (bookcase.bookcaseType == category.first) {
                    nodes[subIndex].addChild(TreeNode(BookcaseChild(bookcase)))
                }
            }
            categoryNode.expandAll()
        }
        val adapter = TreeViewAdapter(nodes, Arrays.asList(BookcaseViewHolder(), BookcaseHeaderViewHolder()))
        if (recycler.adapter == null) {
            recycler.adapter = adapter
            adapter.setOnTreeNodeListener(object : TreeViewAdapter.OnTreeNodeListener {
                override fun onSelected(extra: Int, add: Boolean) {
                }

                override fun onClick(node: TreeNode<*>, holder: RecyclerView.ViewHolder): Boolean {
                    if (!node.isLeaf) {
                        onToggle(!node.isExpand, holder)
                    } else if (node.isLeaf && node.content is BookcaseCategory) {
                        return false
                    } else {
                        val item = node.content as BookcaseChild
                        BookcaseViewerActivity.startActivity(activity!!,
                                item.bookcase.bookcaseId,
                                item.bookcase.bookcaseName,
                                item.bookcase.bookcaseType)
                    }
                    return false
                }

                override fun onToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder) {
                }
            })
            fastScroller.attachRecyclerView(recycler)
        }
        else
            (recycler.adapter as TreeViewAdapter).refresh(nodes)
    }

    override fun onItemClicked(item: Bookcase, position: Int) {
        BookcaseViewerActivity.startActivity(activity!!,
                item.bookcaseId,
                item.bookcaseName,
                item.bookcaseType)
    }

    override fun onRefresh() {
        presenter.getBookcases(true)
    }

    override fun onClick(v: View?) {
        onRefresh()
    }

    override fun onSetTabCount(allCount: Int) {
        countCallback?.onSetBadge(3, allCount)
    }

    override fun hideProgress() {
        refresh.isRefreshing = false
        stateLayout.showReload(recycler.adapter?.itemCount ?: 0)
    }

    override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
        refresh.isRefreshing = true
        stateLayout.showProgress()
    }

    override fun showErrorMessage(msgRes: String?) {
        hideProgress()
        super.showErrorMessage(msgRes)
    }

    override fun showMessage(titleRes: Int, msgRes: Int) {
        hideProgress()
        super.showMessage(titleRes, msgRes)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is UserPagerMvp.View) {
            countCallback = context
        }
    }

    override fun onDetach() {
        countCallback = null
        super.onDetach()
    }

    companion object {

        fun newInstance(): BookcasesOverviewFragment {
            return BookcasesOverviewFragment()
        }
    }

    override fun onItemSelected(parent: String, item: ContextMenus.MenuItem, position: Int, listItem: Any) {
        //TODO("not implemented")
    }
}