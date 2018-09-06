package ru.fantlab.android.ui.modules.award.contests

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.*
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.viewholder.ConstsViewHolder
import ru.fantlab.android.ui.adapter.viewholder.ConstsWorkViewHolder
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.award.AwardPagerMvp
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import java.util.*
import kotlin.collections.ArrayList

class AwardContestsFragment : BaseFragment<AwardContestsMvp.View, AwardContestsPresenter>(),
		AwardContestsMvp.View {
	override fun fragmentLayout() = R.layout.micro_grid_refresh_list
	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
	@BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
	@BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller

    private var countCallback: AwardPagerMvp.View? = null

	private var contests: ArrayList<Award.Contest>? = null

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			contests = savedInstanceState.getParcelableArrayList("contests")
			if (contests != null) {
				onInitViews(contests)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = AwardContestsPresenter()

	override fun onInitViews(contests: List<Award.Contest>?) {
		hideProgress()
		stateLayout.setEmptyText(R.string.no_contests)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		recycler.addDivider()
		fastScroller.attachRecyclerView(recycler)
		if (contests != null) {
			onSetTabCount(contests.size)
			initAdapter(contests)
		}
	}

	private fun initAdapter(contests: List<Award.Contest>) {
		val nodes = arrayListOf<TreeNode<*>>()

		contests.forEachIndexed  { subIndex, item ->
			val title = if (!item.place.isNullOrEmpty()) {
				if (!item.nameyear.isEmpty()) {
					String.format("%s, %s", item.place, item.nameyear)
				} else {
					item.place
				}
			} else {
				item.nameyear
			}
			val app = TreeNode(ConstsParent(title ?: "", item.description))
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

				nodes[subIndex].addChild(TreeNode(Consts(nameConsts ?: "", contestsWork.nominationRusname ?: contestsWork.nominationName, contestsWork.cwLinkId ?: 0)))
			}

		}

		val adapter = TreeViewAdapter(nodes, Arrays.asList(ConstsWorkViewHolder(), ConstsViewHolder()))
		recycler.adapter = adapter
		adapter.setOnTreeNodeListener(object : TreeViewAdapter.OnTreeNodeListener {
			override fun onSelected(extra: Int, add: Boolean) {
			}
			override fun onClick(node: TreeNode<*>, holder: RecyclerView.ViewHolder): Boolean {
				if (!node.isLeaf) {
					onToggle(!node.isExpand, holder)
				} else {
					val itemWork = node.content as Consts
					if (itemWork.workId != 0){
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

	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelableArrayList("contests", contests)
	}

    override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
        refresh.isRefreshing = true
        stateLayout.showProgress()
    }

    override fun hideProgress() {
        refresh.isRefreshing = false
        stateLayout.hideProgress()
    }

	override fun showErrorMessage(msgRes: String) {
		hideProgress()
		super.showErrorMessage(msgRes)
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		hideProgress()
		super.showMessage(titleRes, msgRes)
	}

	companion object {

		fun newInstance(awardId: Int): AwardContestsFragment {
			val view = AwardContestsFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, awardId).end()
			return view
		}
	}

    override fun onRefresh() {
        presenter.onCallApi()
    }

    override fun onClick(v: View?) {
        onRefresh()
    }

    override fun onNotifyAdapter() {
        hideProgress()
    }

    override fun onSetTabCount(allCount: Int) {
        countCallback?.onSetBadge(1, allCount)
    }

    override fun onAttach(context: Context?) {
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