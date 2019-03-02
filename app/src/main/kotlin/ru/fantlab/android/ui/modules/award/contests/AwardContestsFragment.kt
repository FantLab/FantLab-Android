package ru.fantlab.android.ui.modules.award.contests

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Award
import ru.fantlab.android.data.dao.model.Consts
import ru.fantlab.android.data.dao.model.ConstsParent
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.BundleConstant.EXTRA_TWO
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.viewholder.ConstsViewHolder
import ru.fantlab.android.ui.adapter.viewholder.ConstsWorkViewHolder
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.award.AwardPagerMvp
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import ru.fantlab.android.ui.widgets.recyclerview.layoutManager.StaggeredManager
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import java.util.*

class AwardContestsFragment : BaseFragment<AwardContestsMvp.View, AwardContestsPresenter>(),
		AwardContestsMvp.View {

	private var countCallback: AwardPagerMvp.View? = null

	private var workId: Int? = -1

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list

	override fun providePresenter() = AwardContestsPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		workId = arguments?.getInt(EXTRA_TWO)
		stateLayout.setEmptyText(R.string.no_contests)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		recycler.addDivider()
		presenter.onFragmentCreated(arguments!!)
	}

	override fun onInitViews(contests: List<Award.Contest>?) {
		hideProgress()
		if (contests != null) {
			onSetTabCount(contests.size)
			initAdapter(contests)
		}
	}

	private fun initAdapter(contests: List<Award.Contest>) {
		val nodes = arrayListOf<TreeNode<*>>()

		contests.forEachIndexed { subIndex, item ->
			val title = item.nameyear
			val app = TreeNode(ConstsParent(title, item.description))
			nodes.add(app)

			item.contestWorks?.forEach { contestsWork ->

				var nameConsts = if (!contestsWork.workRusname.isNullOrEmpty()) {
					if (!contestsWork.cwName.isNullOrEmpty()) {
						String.format("%s / %s", contestsWork.workRusname, contestsWork.cwName)
					} else {
						contestsWork.workRusname
					}
				} else {
					contestsWork.cwName
				}
				if (nameConsts.isNullOrEmpty()) {
					nameConsts = if (contestsWork.autorRusname.isEmpty()) {
						contestsWork.cwRusname
					} else contestsWork.autorRusname
				}

				nameConsts = if (contestsWork.cwLinkId != null) {
					StringBuilder()
							.append("\"$nameConsts\"")
							.append(", ")
							.append(contestsWork.autorRusname)
							.toString()
				} else nameConsts

				nodes[subIndex].addChild(TreeNode(Consts(nameConsts
						?: "", contestsWork.nominationRusname
						?: contestsWork.nominationName, contestsWork.cwLinkId ?: 0)))
				if (workId != -1) app.expandAll()
			}

		}
		val adapter = TreeViewAdapter(nodes, Arrays.asList(ConstsWorkViewHolder(), ConstsViewHolder()))
		if (recycler.adapter == null)
			recycler.adapter = adapter
		else
			adapter.notifyDataSetChanged()
		adapter.setOnTreeNodeListener(object : TreeViewAdapter.OnTreeNodeListener {
			override fun onSelected(extra: Int, add: Boolean) {
			}

			override fun onClick(node: TreeNode<*>, holder: RecyclerView.ViewHolder): Boolean {
				if (!node.isLeaf) {
					onToggle(!node.isExpand, holder)
				} else if (node.isLeaf && node.content is ConstsParent) {
					return false
				} else {
					val itemWork = node.content as Consts
					if (itemWork.workId != 0) {
						WorkPagerActivity.startActivity(context!!, itemWork.workId, itemWork.title)
					}
				}
				return false
			}

			override fun onToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder) {
				val dirViewHolder = holder as ConstsViewHolder.ViewHolder
				val ivArrow = dirViewHolder.ivArrow
				val rotateDegree = if (isExpand) 90.0f else -90.0f
				ivArrow.animate().rotationBy(rotateDegree)
						.start()
			}
		})
		if (workId != -1) {
			var position = 0
			var success = false
			nodes.forEachIndexed { index, treeNode ->
				if (success) return
				val works = treeNode.childList
				works.forEach { workNode ->
					val work = workNode.content as Consts
					if (work.workId == workId) {
						val lm: StaggeredManager = recycler.layoutManager as StaggeredManager
						lm.scrollToPosition(position)
						success = true
						return@forEachIndexed
					}
					position++
				}
				position++
			}
		}
		fastScroller.attachRecyclerView(recycler)
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

	override fun showMessage(titleRes: Int, msgRes: Int) {
		hideProgress()
		super.showMessage(titleRes, msgRes)
	}

	companion object {

		fun newInstance(awardId: Int, workId: Int): AwardContestsFragment {
			val view = AwardContestsFragment()
			view.arguments = Bundler.start()
					.put(BundleConstant.EXTRA, awardId)
					.put(BundleConstant.EXTRA_TWO, workId)
					.end()
			return view
		}
	}

	override fun onRefresh() {
		presenter.getContests(true)
	}

	override fun onClick(v: View?) {
		onRefresh()
	}

	override fun onSetTabCount(allCount: Int) {
		countCallback?.onSetBadge(1, allCount)
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (context is AwardPagerMvp.View) {
			countCallback = context
		}
	}

	override fun onDetach() {
		countCallback = null
		super.onDetach()
	}

	override fun onItemClicked(item: Award.Contest, position: Int) {
	}
}