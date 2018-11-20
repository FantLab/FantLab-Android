package ru.fantlab.android.ui.modules.work.classification

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ClassParent
import ru.fantlab.android.data.dao.model.GenreGroup
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.viewholder.ClassChildViewHolder
import ru.fantlab.android.ui.adapter.viewholder.ClassParentViewHolder
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.work.WorkPagerMvp
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import java.util.*

class WorkClassificationFragment : BaseFragment<WorkClassificationMvp.View, WorkClassificationPresenter>(),
		WorkClassificationMvp.View {

	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
	@BindView(R.id.progress) lateinit var progress: View
	@BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller

	private var workCallback: WorkPagerMvp.View? = null

	override fun fragmentLayout() = R.layout.work_classification_layout

	override fun providePresenter() = WorkClassificationPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		presenter.onFragmentCreated(arguments!!)
	}

	override fun onInitViews(classificatory: ArrayList<GenreGroup>) {
		hideProgress()
		stateLayout.setEmptyText(R.string.no_classification)
		stateLayout.setOnReloadListener(this)
		recycler.setEmptyView(stateLayout)
		recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
				workCallback?.onScrolled(dy > 0);
			}
		})
		val nodes = arrayListOf<TreeNode<*>>()
		classificatory.forEachIndexed { index, item ->
			val parent = TreeNode(ClassParent(item.label, null))
			nodes.add(parent)
			var toSkip = -1
			item.genres.forEachIndexed { subIndex, pair ->
				val currentLevel = pair.first
				val currentTitle = pair.second.label

				if (toSkip == subIndex) {
					return@forEachIndexed
				}

				val parentChild = TreeNode(ClassParent(currentTitle, pair.second.percent * 100))
				val prevPair = if (subIndex > 0) item.genres[subIndex - 1] else null
				val nextPair = if (item.genres.size > subIndex + 1) item.genres[subIndex + 1] else null
				if (currentLevel == 0) {
					parent.addChild(parentChild)
				} else if (prevPair != null && prevPair.first == currentLevel - 1) {
					if (nextPair != null && nextPair.first != currentLevel) {
						parent.childList[0].addChild(parentChild.addChild(TreeNode(ClassParent(nextPair.second.label, pair.second.percent * 100))))
						toSkip = nextPair.first
					} else {
						parent.childList[0].addChild(parentChild)
					}
				} else {
					parent.childList[0].addChild(parentChild)
				}
			}
			parent.expandAll()
		}
		val adapter = TreeViewAdapter(nodes, Arrays.asList(ClassParentViewHolder(), ClassChildViewHolder()))
		adapter.setOnTreeNodeListener(object : TreeViewAdapter.OnTreeNodeListener {
			override fun onSelected(extra: Int, add: Boolean) {
			}

			override fun onClick(node: TreeNode<*>, holder: RecyclerView.ViewHolder): Boolean {
				if (!node.isLeaf) {
					onToggle(!node.isExpand, holder)
				}
				return false
			}

			override fun onToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder) {
				val dirViewHolder = holder as ClassParentViewHolder.ViewHolder
				val ivArrow = dirViewHolder.ivArrow
				val rotateDegree = if (isExpand) 90.0f else -90.0f
				ivArrow.animate().rotationBy(rotateDegree)
						.start()
			}
		})
		recycler.adapter = adapter
		fastScroller.attachRecyclerView(recycler)
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		progress.visibility = View.VISIBLE
		stateLayout.showProgress()
	}

	override fun hideProgress() {
		stateLayout.hideProgress()
		progress.visibility = View.GONE
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
		presenter.getClassificatory(true)
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		if (context is WorkPagerMvp.View) {
			workCallback = context
		}
	}

	override fun onDetach() {
		workCallback = null
		super.onDetach()
	}

	companion object {

		fun newInstance(workId: Int): WorkClassificationFragment {
			val view = WorkClassificationFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, workId).end()
			return view
		}
	}
}