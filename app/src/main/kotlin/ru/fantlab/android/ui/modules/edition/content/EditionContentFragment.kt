package ru.fantlab.android.ui.modules.edition.content

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.EditionContent
import ru.fantlab.android.data.dao.model.EditionContentChild
import ru.fantlab.android.data.dao.model.EditionContentParent
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.viewholder.EditionContentChildViewHolder
import ru.fantlab.android.ui.adapter.viewholder.EditionContentParentViewHolder
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.edition.EditionPagerMvp
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import java.util.*

class EditionContentFragment : BaseFragment<EditionContentMvp.View, EditionContentPresenter>(),
		EditionContentMvp.View {

	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
	@BindView(R.id.progress) lateinit var progress: View

	private var content: ArrayList<EditionContent>? = null
	private var countCallback: EditionPagerMvp.View? = null

	override fun fragmentLayout() = R.layout.edition_content_layout

	override fun providePresenter() = EditionContentPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			content = savedInstanceState.getParcelableArrayList<EditionContent>("content")
			if (content != null) {
				onInitViews(content!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun onInitViews(content: ArrayList<EditionContent>) {
		hideProgress()
		stateLayout.setEmptyText(R.string.no_content)
		stateLayout.setOnReloadListener(this)
		recycler.setEmptyView(stateLayout)
		recycler.addDivider()

		val nodes = arrayListOf<TreeNode<*>>()
		content.forEachIndexed { index, item ->
			if (item.level <= 1) {
				val parent = TreeNode(EditionContentParent(item.title))
				nodes.add(parent)
			} else {
				val parent = nodes[nodes.size - 1]
				val node = if (index + 1 != content.size && content[index + 1].level == item.level + 1)
					TreeNode(EditionContentParent(item.title))
				else
					TreeNode(EditionContentChild(item.title))
				when (item.level) {
					2 -> parent.addChild(node)
					3 -> parent.childList[parent.childList.size - 1].addChild(node)
					4 -> parent.childList[parent.childList.size - 1].childList[parent.childList[parent.childList.size - 1].childList.size - 1].addChild(node)
				}
				parent.expandAll()
			}
		}

		val adapter = TreeViewAdapter(nodes, Arrays.asList(EditionContentParentViewHolder(), EditionContentChildViewHolder()))
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
				val dirViewHolder = holder as EditionContentParentViewHolder.ViewHolder
				val ivArrow = dirViewHolder.arrow
				val rotateDegree = if (isExpand) 90.0f else -90.0f
				ivArrow.animate().rotationBy(rotateDegree)
						.start()
			}
		})
		recycler.adapter = adapter
		onSetTabCount(adapter.itemCount)
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelableArrayList("content", content)
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		progress.visibility = View.VISIBLE
		stateLayout.showProgress()
	}

	override fun hideProgress() {
		stateLayout.hideProgress()
		progress.visibility = View.GONE
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

		fun newInstance(editionId: Int): EditionContentFragment {
			val view = EditionContentFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, editionId).end()
			return view
		}
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		if (context is EditionPagerMvp.View) {
			countCallback = context
		}
	}

	override fun onDetach() {
		countCallback = null
		super.onDetach()
	}

	override fun onSetTabCount(allCount: Int) {
		countCallback?.onSetBadge(1, allCount)
	}

	override fun onRefresh() {
		presenter.onCallApi()
	}

	override fun onNotifyAdapter() {
		hideProgress()
	}

	override fun onClick(p0: View?) {
		onRefresh()
	}
}