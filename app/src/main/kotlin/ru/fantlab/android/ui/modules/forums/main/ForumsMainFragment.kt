package ru.fantlab.android.ui.modules.forums.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.Keep
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Forums
import ru.fantlab.android.data.dao.model.ForumsTreeChild
import ru.fantlab.android.data.dao.model.ForumsTreeParent
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.ui.adapter.viewholder.ForumsChildViewHolder
import ru.fantlab.android.ui.adapter.viewholder.ForumsParentViewHolder
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.forums.ForumsMvp
import ru.fantlab.android.ui.modules.forums.forum.ForumFragment
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import java.util.*

class ForumsMainFragment : BaseFragment<ForumsMainMvp.View, ForumsMainPresenter>(),
		ForumsMainMvp.View {

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list

	override fun providePresenter() = ForumsMainPresenter()

	private var forumsCallback: ForumsMvp.View? = null

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		forumsCallback?.setTitle(getString(R.string.forums_list))
		stateLayout.setEmptyText(R.string.no_results)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		presenter.getForum(false)
		fastScroller.attachRecyclerView(recycler)
		fastScroller.setHidden(true)
	}

	override fun onHiddenChanged(hidden: Boolean) {
		super.onHiddenChanged(hidden)
		if (!hidden) forumsCallback?.setTitle(getString(R.string.forums_list))
	}

	override fun onInitViews(forums: ArrayList<Forums.ForumBlock>) {
		hideProgress()
		if (forums.isEmpty()) {
			return
		}
		val nodes = arrayListOf<TreeNode<*>>()
		forums.forEachIndexed { _, forumBlock ->
			val parentForum = TreeNode(ForumsTreeParent(forumBlock.title))
			nodes.add(parentForum)
			forumBlock.forums.forEachIndexed { blockIndex, forum ->
				val childForum = TreeNode(ForumsTreeChild(
						forum.title,
						forum.lastMessage.topic.title,
						forum.lastMessage.date,
						forum.lastMessage.user.login,
						forum.lastMessage.user.id,
						forum.stats.messageCount,
						forum.stats.topicCount,
						forum.id
				))
				parentForum.addChild(childForum)
			}
			parentForum.expandAll()

		}

		val adapter = TreeViewAdapter(nodes, Arrays.asList(ForumsParentViewHolder(), ForumsChildViewHolder(PrefGetter.isForumExtended())))
		adapter.setPadding(0)
		if (recycler.adapter != null) (recycler.adapter as TreeViewAdapter).refresh(nodes) else recycler.adapter = adapter
		fastScroller.attachRecyclerView(recycler)
		adapter.setOnTreeNodeListener(this)
	}

	override fun onClick(node: TreeNode<*>, holder: RecyclerView.ViewHolder): Boolean {
		if (holder is ForumsChildViewHolder.ViewHolder) {
			val parentNode = node.content as ForumsTreeChild?
			forumsCallback?.openForum(ForumFragment.TAG, parentNode?.forumId ?: -1, holder.title.text.toString(), 0)
		} else if (!node.isLeaf) onToggle(!node.isExpand, holder)
		return false
	}

	override fun onToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder) {
		val viewHolder = holder as ForumsParentViewHolder.ViewHolder
		val ivArrow = viewHolder.expandButton
		val rotateDegree = if (isExpand) 90.0f else -90.0f
		ivArrow.animate().rotationBy(rotateDegree)
				.start()
	}

	override fun onSelected(extra: Int, add: Boolean) {
	}


	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		refresh.isRefreshing = true
		stateLayout.showProgress()
	}

	override fun hideProgress() {
		refresh.isRefreshing = false
		stateLayout.hideProgress()
	}

	override fun showErrorMessage(msgRes: String?) {
		hideProgress()
		super.showErrorMessage(msgRes)
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		hideProgress()
		super.showMessage(titleRes, msgRes)
	}

	override fun onClick(v: View?) {
		onRefresh()
	}

	override fun onRefresh() {
		presenter.getForum(true)
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (context is ForumsMvp.View) {
			forumsCallback = context
		}
	}

	override fun onDetach() {
		forumsCallback = null
		super.onDetach()
	}

	companion object {
		@Keep
		val TAG: String = ForumsMainFragment::class.java.simpleName
	}

}