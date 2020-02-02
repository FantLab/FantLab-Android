package ru.fantlab.android.ui.modules.communities.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.Keep
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Community
import ru.fantlab.android.data.dao.model.CommunityTreeChild
import ru.fantlab.android.data.dao.model.CommunityTreeParent
import ru.fantlab.android.ui.adapter.viewholder.CommunityChildViewHolder
import ru.fantlab.android.ui.adapter.viewholder.CommunityParentViewHolder
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.communities.CommunitiesMvp
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import java.util.*

class CommunitiesMainFragment : BaseFragment<CommunitiesMainMvp.View, CommunitiesMainPresenter>(),
		CommunitiesMainMvp.View {

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list

	override fun providePresenter() = CommunitiesMainPresenter()

	private var communitiesCallback: CommunitiesMvp.View? = null

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		communitiesCallback?.setTitle(getString(R.string.communities_list))
		stateLayout.setEmptyText(R.string.no_communities)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		presenter.getCommunity(false)
	}

	override fun onHiddenChanged(hidden: Boolean) {
		super.onHiddenChanged(hidden)
		if (!hidden) communitiesCallback?.setTitle(getString(R.string.communities_list))
	}

	override fun onInitViews(main: ArrayList<Community.Main>, additional: ArrayList<Community.Additional>) {
		hideProgress()
		if (main.isEmpty() || additional.isEmpty()) {
			stateLayout.showEmptyState()
			return
		}

		val nodes = arrayListOf<TreeNode<*>>()
		val mainCommunities = TreeNode(CommunityTreeParent(getString(R.string.general_communities)))
		nodes.add(mainCommunities)
		main.forEach {

			val mainCommunityNode = TreeNode(CommunityTreeChild(
					it.avatar,
					it.title,
					it.stats.articleCount,
					it.stats.subscriberCount,
					it.lastArticle.date,
					it.lastArticle.user.login,
					it.lastArticle.user.id,
					it.lastArticle.title,
					it.id
			))
			mainCommunities.addChild(mainCommunityNode)
		}
		mainCommunities.expandAll()

		val additionalCommunities = TreeNode(CommunityTreeParent(getString(R.string.additional_communities)))
		nodes.add(additionalCommunities)
		additional.forEach {

			val additionalCommunityNode = TreeNode(CommunityTreeChild(
					it.avatar,
					it.title,
					it.stats.articleCount,
					it.stats.subscriberCount,
					it.lastArticle.date,
					it.lastArticle.user.login,
					it.lastArticle.user.id,
					it.lastArticle.title,
					it.id
			))
			additionalCommunities.addChild(additionalCommunityNode)
		}

		val adapter = TreeViewAdapter(nodes, Arrays.asList(CommunityParentViewHolder(), CommunityChildViewHolder()))
		adapter.setPadding(0)
		if (recycler.adapter != null) (recycler.adapter as TreeViewAdapter).refresh(nodes) else recycler.adapter = adapter
		fastScroller.attachRecyclerView(recycler)
		adapter.setOnTreeNodeListener(this)
	}

	override fun onClick(node: TreeNode<*>, holder: RecyclerView.ViewHolder): Boolean {
		if (!node.isLeaf) onToggle(!node.isExpand, holder)
			return false
	}

	override fun onToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder) {
		val viewHolder = holder as CommunityParentViewHolder.ViewHolder
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
		presenter.getCommunity(true)
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (context is CommunitiesMvp.View) {
			communitiesCallback = context
		}
	}

	override fun onDetach() {
		communitiesCallback = null
		super.onDetach()
	}

	companion object {
		@Keep
		val TAG: String = CommunitiesMainFragment::class.java.simpleName
	}

}