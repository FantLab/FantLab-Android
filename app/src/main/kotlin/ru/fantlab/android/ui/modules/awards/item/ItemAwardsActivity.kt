package ru.fantlab.android.ui.modules.awards.item

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Awards
import ru.fantlab.android.data.dao.model.WorkAwardsChild
import ru.fantlab.android.data.dao.model.WorkAwardsParent
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.viewholder.AwardChildViewHolder
import ru.fantlab.android.ui.adapter.viewholder.AwardParentViewHolder
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.modules.award.AwardPagerActivity
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter

class ItemAwardsActivity : BaseActivity<ItemAwardsMvp.View, ItemAwardsPresenter>(), ItemAwardsMvp.View {

	@State var workId: Int = 0
	@State var workName: String = ""

	override fun layout(): Int = R.layout.work_awards_layout

	override fun isTransparent(): Boolean = true

	override fun canBack(): Boolean = true

	override fun providePresenter() = ItemAwardsPresenter()

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
		toolbar?.subtitle = getString(R.string.awards)
		if (savedInstanceState == null) {
			stateLayout.hideProgress()
		}
		stateLayout.setEmptyText(R.string.no_nominations)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		presenter.onCallApi(workId)
		fastScroller.attachRecyclerView(recycler)
	}

	override fun onInitViews(awards: Awards) {
		hideProgress()

		val nodes = arrayListOf<TreeNode<*>>()

		if (!awards.nominations.isNullOrEmpty()) {
			nodes.add(TreeNode(WorkAwardsParent(getString(R.string.nominations))))
			awards.nominations.forEach { nom ->
				val childNode = TreeNode(WorkAwardsChild(nom.awardId, nom.awardRusName, nom.awardName, "", nom.nominationRusName, nom.contestYear))
				nodes[0].addChild(childNode)
			}
			nodes[0].expandAll()
		}

		if (!awards.wins.isNullOrEmpty()) {
			nodes.add(TreeNode(WorkAwardsParent(getString(R.string.winners))))
			awards.wins.forEach { win ->
				val childNode = TreeNode(WorkAwardsChild(win.awardId, win.awardRusName, win.awardName, "", win.nominationRusName, win.contestYear))
				nodes[if (!awards.nominations.isNullOrEmpty()) 1 else 0].addChild(childNode)
			}
			nodes[if (!awards.nominations.isNullOrEmpty()) 1 else 0].expandAll()
		}

		val adapter = TreeViewAdapter(nodes, listOf(AwardParentViewHolder(), AwardChildViewHolder()))
		if (recycler.adapter != null) (recycler.adapter as TreeViewAdapter).refresh(nodes) else recycler.adapter = adapter

		adapter.setOnTreeNodeListener(object : TreeViewAdapter.OnTreeNodeListener {

			override fun onSelected(extra: Int, add: Boolean) {}

			override fun onClick(node: TreeNode<*>, holder: RecyclerView.ViewHolder): Boolean {
				if (holder is AwardChildViewHolder.ViewHolder) {
					val parentNode = node.content as WorkAwardsChild?
					if (parentNode != null) {
						val name = if (parentNode.nameRus.isNotEmpty()) {
							if (parentNode.nameOrig.isNotEmpty()) {
								String.format("%s / %s", parentNode.nameRus, parentNode.nameOrig)
							} else {
								parentNode.nameRus
							}
						} else {
							parentNode.nameOrig
						}
						AwardPagerActivity.startActivity(applicationContext, parentNode.awardId, name, 1, workId)
					}
				} else if (!node.isLeaf) onToggle(!node.isExpand, holder)
				return false
			}

			override fun onToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder) {
				val viewHolder = holder as AwardParentViewHolder.ViewHolder
				val ivArrow = viewHolder.expandButton
				val rotateDegree = if (isExpand) 90.0f else -90.0f
				ivArrow.animate().rotationBy(rotateDegree).start()
			}
		})
	}

	override fun onRefresh() {
		presenter.onCallApi(workId)
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

	companion object {

		fun startActivity(context: Context, workId: Int, workName: String) {
			val intent = Intent(context, ItemAwardsActivity::class.java)
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