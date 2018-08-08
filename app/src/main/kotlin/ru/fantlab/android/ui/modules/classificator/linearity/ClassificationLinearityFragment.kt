package ru.fantlab.android.ui.modules.classificator.linearity

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Classificator
import ru.fantlab.android.data.dao.model.ClassificatorModel
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.viewholder.ClassificatorViewHolder
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.classificator.ClassificatorPagerMvp
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import java.util.*
import kotlin.collections.ArrayList

class ClassificationLinearityFragment : BaseFragment<ClassificationLinearityMvp.View, ClassificationLinearityPresenter>(),
		ClassificationLinearityMvp.View {

	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller
	@BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
	@BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout

	private var classificator: ArrayList<ClassificatorModel>? = null

	private var pagerCallback: ClassificatorPagerMvp.View? = null

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list

	var selectedItems = 0

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			classificator = savedInstanceState.getParcelableArrayList("classificator")
			if (classificator != null) {
				onInitViews(classificator!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = ClassificationLinearityPresenter()

	override fun onInitViews(classificators: ArrayList<ClassificatorModel>) {
		hideProgress()
		recycler.addKeyLineDivider()
		fastScroller.attachRecyclerView(recycler)
		refresh.setOnRefreshListener(this)

		val nodes = arrayListOf<TreeNode<*>>()

		classificators.forEach {
			val root = TreeNode(Classificator(it.name, it.descr, it.id))
			nodes.add(root)
			work(it, root, false)
		}

		val adapter = TreeViewAdapter(nodes, Arrays.asList(ClassificatorViewHolder()))

		recycler.adapter = adapter

		adapter.setOnTreeNodeListener(object : TreeViewAdapter.OnTreeNodeListener {

			override fun onSelected(extra: Int, add: Boolean) {
				pagerCallback?.onSelected(extra, add)
				if (add)
					selectedItems++
				else
					selectedItems--
				onSetTabCount(selectedItems)
			}

			override fun onClick(node: TreeNode<*>, holder: RecyclerView.ViewHolder): Boolean {
				val viewHolder = holder as ClassificatorViewHolder.ViewHolder
				val state = viewHolder.checkbox.isChecked
				if (!node.isLeaf) {
					onToggle(!node.isExpand, holder)
					if (!state && !node.isExpand) {
						viewHolder.checkbox.isChecked = true
					}
				} else {
					viewHolder.checkbox.isChecked = !state
				}
				return false
			}
			override fun onToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder) {
				val viewHolder = holder as ClassificatorViewHolder.ViewHolder
				val ivArrow = viewHolder.arrow
				val rotateDegree = if (isExpand) 90.0f else -90.0f
				ivArrow.animate().rotationBy(rotateDegree)
						.start()
			}
		})
	}

	private fun work(it: ClassificatorModel, root: TreeNode<Classificator>, lastLevel: Boolean) {
		if (it.childs != null){
			val counter = root.childList.size - 1
			it.childs.forEach{
				val child = TreeNode(Classificator(it.name, it.descr, it.id))
				if (lastLevel) {
					val childB = TreeNode(Classificator(it.name, it.descr, it.id))
					root.childList[counter].addChild(childB)
				} else root.addChild(child)
				work(it, root, true)
			}
		}
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

	override fun onClick(v: View?) {
		onRefresh()
	}

	override fun onRefresh() {
		hideProgress()
	}

	fun onSetTabCount(count: Int) {
		pagerCallback?.onSetBadge(5, count)
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		if (context is ClassificatorPagerMvp.View) {
			pagerCallback = context
		}
	}

	override fun onDetach() {
		pagerCallback = null
		super.onDetach()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelableArrayList("classificator", classificator)
	}

	companion object {

		fun newInstance(editionId: Int): ClassificationLinearityFragment {
			val view = ClassificationLinearityFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, editionId).end()
			return view
		}
	}
}