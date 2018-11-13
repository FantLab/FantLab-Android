package ru.fantlab.android.ui.widgets.treeview

import java.util.*

class TreeNode<T : LayoutItemType>(var content: T?) : Cloneable {

	var parent: TreeNode<*>? = null
	internal var childList: MutableList<TreeNode<*>> = ArrayList()
	var isExpand: Boolean = false
		private set
	var isLocked: Boolean = false
		private set
	private var height = UNDEFINE

	val isRoot: Boolean
		get() = parent == null

	val isLeaf: Boolean
		get() = childList.isEmpty()

	fun getHeight(): Int {
		if (isRoot)
			height = 0
		else if (height == UNDEFINE)
			height = parent!!.getHeight() + 1
		return height
	}

	fun getChildList(): List<TreeNode<*>>? {
		return childList
	}

	fun setChildList(childList: List<TreeNode<*>>) {
		this.childList.clear()
		for (treeNode in childList) {
			addChild(treeNode)
		}
	}

	fun addChild(node: TreeNode<*>): TreeNode<*> {
		childList.add(node)
		node.parent = this
		return this
	}

	fun toggle(): Boolean {
		isExpand = !isExpand
		return isExpand
	}

	fun collapse() {
		if (isExpand) {
			isExpand = false
		}
	}

	fun collapseAll() {
		if (childList.isEmpty()) {
			return
		}
		for (child in this.childList) {
			child.collapseAll()
		}
	}

	fun expand() {
		if (!isExpand) {
			isExpand = true
		}
	}

	fun expandAll() {
		expand()
		if (childList.isEmpty()) {
			return
		}
		for (child in this.childList) {
			child.expandAll()
		}
	}

	fun lock(): TreeNode<T> {
		isLocked = true
		return this
	}

	fun unlock(): TreeNode<T> {
		isLocked = false
		return this
	}

	override fun toString(): String {
		return "TreeNode{" +
				"content=" + this.content +
				", parent=" + (if (parent == null) "null" else parent!!.content!!.toString()) +
				", childList=" + (if (false) "null" else childList.toString()) +
				", isExpand=" + isExpand +
				'}'.toString()
	}

	@Throws(CloneNotSupportedException::class)
	public override fun clone(): TreeNode<T> {
		val clone = TreeNode(this.content!!)
		clone.isExpand = this.isExpand
		return clone
	}

	companion object {

		private val UNDEFINE = -1
	}
}
