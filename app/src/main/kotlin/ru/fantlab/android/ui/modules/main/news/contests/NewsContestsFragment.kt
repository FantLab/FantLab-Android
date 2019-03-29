package ru.fantlab.android.ui.modules.main.news.contests

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Award
import ru.fantlab.android.data.dao.model.ConstsParent
import ru.fantlab.android.data.dao.model.NewsContest
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.viewholder.ConstsViewHolder
import ru.fantlab.android.ui.adapter.viewholder.NewsContestWorkViewHolder
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.work.CyclePagerActivity
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import java.util.*

class NewsContestsFragment : BaseFragment<NewsContestsMvp.View, NewsContestsPresenter>(),
		NewsContestsMvp.View {

	override fun fragmentLayout() = R.layout.contest_list

	override fun providePresenter() = NewsContestsPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		presenter.onFragmentCreated(arguments!!)
	}

	override fun onInitViews(contests: List<Pair<String?, List<Award.ContestWork>>>?) {
		hideProgress()
		if (!contests.isNullOrEmpty()) {
			initAdapter(contests)
		}
	}

	private fun initAdapter(contests: List<Pair<String?, List<Award.ContestWork>>>?) {
		val nodes = arrayListOf<TreeNode<*>>()

		recycler.isNestedScrollingEnabled = false

		contests?.forEachIndexed { subIndex, item ->
			val title = item.first
			val app = TreeNode(ConstsParent(title ?: "", null))
			nodes.add(app)

			item.second.forEach { contestsWork ->

				val nameConsts = if (!contestsWork.autorRusname.isEmpty() && !contestsWork.cwName.isNullOrEmpty()) {
					"${contestsWork.autorRusname} / ${contestsWork.cwName}"
				} else if (!contestsWork.autorRusname.isEmpty()) {
					contestsWork.autorRusname
				} else if (!contestsWork.cwName.isNullOrEmpty()) {
					contestsWork.cwName
				} else ""

				nodes[subIndex].addChild(TreeNode(NewsContest(nameConsts, contestsWork.nominationRusname, contestsWork.cwLinkType, contestsWork.cwLinkId
						?: 0, contestsWork.cwNumber)))
			}

		}
		val adapter = TreeViewAdapter(nodes, Arrays.asList(NewsContestWorkViewHolder(), ConstsViewHolder()))
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
					val itemWork = node.content as NewsContest
					when (itemWork.linkType) {
						"work" -> WorkPagerActivity.startActivity(context!!, itemWork.workId, itemWork.title, 0)
						"cycle" -> CyclePagerActivity.startActivity(context!!, itemWork.workId, itemWork.title, 0)
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
		val TAG: String = NewsContestsFragment::class.java.simpleName

		fun newInstance(contestId: Int): NewsContestsFragment {
			val view = NewsContestsFragment()
			view.arguments = Bundler.start()
					.put(BundleConstant.EXTRA, contestId)
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
}