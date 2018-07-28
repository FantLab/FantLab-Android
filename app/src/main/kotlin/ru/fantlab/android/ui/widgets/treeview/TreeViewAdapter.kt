package ru.fantlab.android.ui.widgets.treeview

import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

class TreeViewAdapter(nodes: List<TreeNode<*>>?, private val viewBinders: List<TreeViewBinder<*>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	private val displayNodes: MutableList<TreeNode<*>>?
	private var padding = 30
	private var onTreeNodeListener: OnTreeNodeListener? = null
	private var toCollapseChild: Boolean = false

	init {
		displayNodes = ArrayList()
		if (nodes != null)
			findDisplayNodes(nodes)
	}

	private fun findDisplayNodes(nodes: List<TreeNode<*>>) {
		for (node in nodes) {
			displayNodes!!.add(node)
			if (!node.isLeaf && node.isExpand)
				findDisplayNodes(node.getChildList()!!)
		}
	}

	override fun getItemViewType(position: Int): Int {
		return displayNodes!![position].content!!.layoutId
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val v = LayoutInflater.from(parent.context)
				.inflate(viewType, parent, false)
		if (viewBinders.size == 1)
			return viewBinders[0].provideViewHolder(v)
		for (viewBinder in viewBinders) {
			if (viewBinder.layoutId == viewType)
				return viewBinder.provideViewHolder(v)
		}
		return viewBinders[0].provideViewHolder(v)
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
		if (!payloads.isEmpty()) {
			val b = payloads[0] as Bundle
			for (key in b.keySet()) {
				when (key) {
					KEY_IS_EXPAND -> if (onTreeNodeListener != null)
						onTreeNodeListener!!.onToggle(b.getBoolean(key), holder)
				}
			}
		}
		super.onBindViewHolder(holder, position, payloads)
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		holder.itemView.setPadding(displayNodes!![position].getHeight() * padding, 3, 3, 3)
		holder.itemView.setOnClickListener(View.OnClickListener {
			val selectedNode = displayNodes[holder.layoutPosition]
			try {
				val lastClickTime = holder.itemView.tag as Long
				if (System.currentTimeMillis() - lastClickTime < 500)
					return@OnClickListener
			} catch (e: Exception) {
				holder.itemView.tag = System.currentTimeMillis()
			}

			holder.itemView.tag = System.currentTimeMillis()

			if (onTreeNodeListener != null && onTreeNodeListener!!.onClick(selectedNode, holder))
				return@OnClickListener
			if (selectedNode.isLeaf)
				return@OnClickListener
			if (selectedNode.isLocked) return@OnClickListener
			val isExpand = selectedNode.isExpand
			val positionStart = displayNodes.indexOf(selectedNode) + 1
			if (!isExpand) {
				notifyItemRangeInserted(positionStart, addChildNodes(selectedNode, positionStart))
			} else {
				notifyItemRangeRemoved(positionStart, removeChildNodes(selectedNode, true))
			}
		})
		for (viewBinder in viewBinders) {
			if (viewBinder.layoutId == displayNodes[position].content!!.layoutId)
				viewBinder.bindView(holder, position, displayNodes[position])
		}
	}

	private fun addChildNodes(pNode: TreeNode<*>, startIndex: Int): Int {
		val childList = pNode.getChildList()
		var addChildCount = 0
		for (treeNode in childList!!) {
			displayNodes!!.add(startIndex + addChildCount++, treeNode)
			if (treeNode.isExpand) {
				addChildCount += addChildNodes(treeNode, startIndex + addChildCount)
			}
		}
		if (!pNode.isExpand)
			pNode.toggle()
		return addChildCount
	}

	private fun removeChildNodes(pNode: TreeNode<*>) {
		removeChildNodes(pNode, true)
	}

	private fun removeChildNodes(pNode: TreeNode<*>, shouldToggle: Boolean): Int {
		if (pNode.isLeaf)
			return 0
		val childList = pNode.getChildList()
		var removeChildCount = childList!!.size
		displayNodes!!.removeAll(childList)
		for (child in childList) {
			if (child.isExpand) {
				if (toCollapseChild)
					child.toggle()
				removeChildCount += removeChildNodes(child, false)
			}
		}
		if (shouldToggle)
			pNode.toggle()
		return removeChildCount
	}

	override fun getItemCount(): Int {
		return displayNodes?.size ?: 0
	}

	fun setPadding(padding: Int) {
		this.padding = padding
	}

	fun ifCollapseChildWhileCollapseParent(toCollapseChild: Boolean) {
		this.toCollapseChild = toCollapseChild
	}

	fun setOnTreeNodeListener(onTreeNodeListener: OnTreeNodeListener) {
		this.onTreeNodeListener = onTreeNodeListener
	}

	interface OnTreeNodeListener {

		fun onClick(node: TreeNode<*>, holder: RecyclerView.ViewHolder): Boolean

		fun onToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder)

	}

	fun refresh(treeNodes: List<TreeNode<*>>) {
		displayNodes!!.clear()
		findDisplayNodes(treeNodes)
		notifyDataSetChanged()
	}

	private fun notifyDiff(temp: List<TreeNode<*>>) {
		val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
			override fun getOldListSize(): Int {
				return temp.size
			}

			override fun getNewListSize(): Int {
				return displayNodes!!.size
			}

			override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
				return this@TreeViewAdapter.areItemsTheSame(temp[oldItemPosition], displayNodes!![newItemPosition])
			}

			override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
				return this@TreeViewAdapter.areContentsTheSame(temp[oldItemPosition], displayNodes!![newItemPosition])
			}

			override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
				return this@TreeViewAdapter.getChangePayload(temp[oldItemPosition], displayNodes!![newItemPosition])
			}
		})
		diffResult.dispatchUpdatesTo(this)
	}

	private fun getChangePayload(oldNode: TreeNode<*>, newNode: TreeNode<*>): Any? {
		val diffBundle = Bundle()
		if (newNode.isExpand != oldNode.isExpand) {
			diffBundle.putBoolean(KEY_IS_EXPAND, newNode.isExpand)
		}
		return if (diffBundle.size() == 0) null else diffBundle
	}

	private fun areContentsTheSame(oldNode: TreeNode<*>, newNode: TreeNode<*>): Boolean {
		return (oldNode.content != null && oldNode.content == newNode.content
				&& oldNode.isExpand == newNode.isExpand)
	}

	private fun areItemsTheSame(oldNode: TreeNode<*>, newNode: TreeNode<*>): Boolean {
		return oldNode.content != null && oldNode.content == newNode.content
	}

	fun collapseAll() {
		val temp = backupDisplayNodes()
		val roots = ArrayList<TreeNode<*>>()
		for (displayNode in displayNodes!!) {
			if (displayNode.isRoot)
				roots.add(displayNode)
		}
		for (root in roots) {
			if (root.isExpand)
				removeChildNodes(root)
		}
		notifyDiff(temp)
	}

	private fun backupDisplayNodes(): List<TreeNode<*>> {
		val temp = ArrayList<TreeNode<*>>()
		for (displayNode in displayNodes!!) {
			try {
				temp.add(displayNode.clone())
			} catch (e: CloneNotSupportedException) {
				temp.add(displayNode)
			}

		}
		return temp
	}

	fun collapseNode(pNode: TreeNode<*>) {
		val temp = backupDisplayNodes()
		removeChildNodes(pNode)
		notifyDiff(temp)
	}

	fun collapseBrotherNode(pNode: TreeNode<*>) {
		val temp = backupDisplayNodes()
		if (pNode.isRoot) {
			val roots = ArrayList<TreeNode<*>>()
			for (displayNode in displayNodes!!) {
				if (displayNode.isRoot)
					roots.add(displayNode)
			}
			for (root in roots) {
				if (root.isExpand && root != pNode)
					removeChildNodes(root)
			}
		} else {
			val parent = pNode.parent ?: return
			val childList = parent.getChildList()
			for (node in childList!!) {
				if (node == pNode || !node.isExpand)
					continue
				removeChildNodes(node)
			}
		}
		notifyDiff(temp)
	}

	companion object {
		private val KEY_IS_EXPAND = "IS_EXPAND"
	}

}
