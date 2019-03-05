package ru.fantlab.android.ui.modules.profile.bookcases

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.evernote.android.state.State
import ru.fantlab.android.R
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.data.dao.model.Bookcase
import ru.fantlab.android.data.dao.model.BookcaseCategory
import ru.fantlab.android.data.dao.model.BookcaseChild
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.viewholder.BookcaseHeaderViewHolder
import ru.fantlab.android.ui.adapter.viewholder.BookcaseViewHolder
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.bookcases.editions.BookcaseEditionsActivity
import ru.fantlab.android.ui.modules.user.UserPagerMvp
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import java.util.*

class ProfileBookcasesFragment : BaseFragment<ProfileBookcasesMvp.View, ProfileBookcasesPresenter>(),
        ProfileBookcasesMvp.View {

    @State var userId: Int = -1
    private var countCallback: UserPagerMvp.View? = null
    private var categories: ArrayList<Pair<String, String>> = arrayListOf()

    override fun fragmentLayout(): Int = R.layout.micro_grid_refresh_list

    override fun providePresenter(): ProfileBookcasesPresenter = ProfileBookcasesPresenter()

    init {
        categories.add(Pair("work", "Подборки произведений"))
        categories.add(Pair("edition", "Книжные полки изданий"))
        categories.add(Pair("films", "Кинополки"))
    }

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            stateLayout.hideProgress()
        }
        userId = arguments!!.getInt(BundleConstant.EXTRA)
        stateLayout.setEmptyText(R.string.no_responses)
        stateLayout.setOnReloadListener(this)
        refresh.setOnRefreshListener(this)
        recycler.setEmptyView(stateLayout, refresh)
        presenter.onFragmentCreated(arguments!!)
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
                if (bookcase.type.equals(category.first)) {
                    nodes[subIndex].addChild(TreeNode(BookcaseChild(bookcase.name, bookcase.description, bookcase.type)))
                }
            }
            categoryNode.expandAll()
        }

        Log.v("ProfileBookcasesFragmen", "categories=" + categories.toString());
        Log.v("ProfileBookcasesFragmen", "nodes=" + nodes.toString());
        Log.v("ProfileBookcasesFragmen", "bookcases=" + bookcases.toString());

        val adapter = TreeViewAdapter(nodes, Arrays.asList(BookcaseViewHolder(), BookcaseHeaderViewHolder()))
        if (recycler.adapter == null)
            recycler.adapter = adapter
        else
            adapter.notifyDataSetChanged()
        Log.v("ProfileBookcasesFragmen", "adapter=" + recycler.adapter);
        Log.v("ProfileBookcasesFragmen", "adapter.items=" + recycler.adapter.itemCount);
        adapter.setOnTreeNodeListener(object : TreeViewAdapter.OnTreeNodeListener {
            override fun onSelected(extra: Int, add: Boolean) {
            }

            override fun onClick(node: TreeNode<*>, holder: RecyclerView.ViewHolder): Boolean {
                if (!node.isLeaf) {
                    onToggle(!node.isExpand, holder)
                } else if (node.isLeaf && node.content is BookcaseCategory) {
                    return false
                } else {
                    /// TODO: replace with the real call
                    BookcaseEditionsActivity.startActivity(context!!, 3056)
                    /*val itemWork = node.content as Consts
                    if (itemWork.workId != 0) {
                        WorkPagerActivity.startActivity(context!!, itemWork.workId, itemWork.title)
                    }*/
                }
                return false
            }

            override fun onToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder) {
            }
        })
        fastScroller.attachRecyclerView(recycler)
    }

    override fun onItemClicked(item: Bookcase, position: Int) {
        /// TODO: replace with the real call
        BookcaseEditionsActivity.startActivity(context!!, 3056)
        //BookcaseEditionsActivity.startActivity(context!!, item.id)
    }

    override fun onRefresh() {
        presenter.getBookcases(userId, true)
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

    override fun onAttach(context: Context?) {
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

        fun newInstance(userId: Int): ProfileBookcasesFragment {
            val view = ProfileBookcasesFragment()
            view.arguments = Bundler.start().put(BundleConstant.EXTRA, userId).end()
            return view
        }
    }

    override fun onItemSelected(parent: String, item: ContextMenus.MenuItem, position: Int, listItem: Any) {
        TODO("not implemented")
    }
}