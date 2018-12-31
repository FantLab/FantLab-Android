package ru.fantlab.android.ui.widgets.recyclerview

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.ViewGroup
import ru.fantlab.android.helper.AnimHelper
import ru.fantlab.android.helper.PrefGetter

abstract class BaseRecyclerAdapter<M, VH : BaseViewHolder<M>>(internal var data: MutableList<M> = mutableListOf(), var listener: BaseViewHolder.OnItemClickListener<M>? = null) : RecyclerView.Adapter<VH>() {
	private val PROGRESS_TYPE = 2017
	private var lastKnowingPosition = -1
	private var enableAnimation = PrefGetter.isRVAnimationEnabled()
	private var progressAdded: Boolean = false
	private var rowWidth: Int = 0

	fun getData(): List<M> {
		return data
	}

	fun getItemByPosition(position: Int): M {
		return data[position]
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
		if (viewType == PROGRESS_TYPE) {
			addSpanLookup(parent)
			return ProgressBarViewHolder.newInstance(parent) as VH
		} else {
			return viewHolder(parent, viewType)
		}
	}

	override fun onBindViewHolder(holder: VH, position: Int) {
		if (holder is ProgressBarViewHolder) {
			if (holder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
				val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
				layoutParams.isFullSpan = true
			}
		} else if (getItem(position) != null) {
			animate(holder, position)
			onBindView(holder, position)
		}
	}

	override fun getItemViewType(position: Int): Int {
		return if (getItem(position) == null) {
			PROGRESS_TYPE
		} else super.getItemViewType(position)
	}

	override fun getItemCount(): Int {
		return data.size
	}

	override fun onViewDetachedFromWindow(holder: VH) {
		holder.onViewIsDetaching()
		super.onViewDetachedFromWindow(holder)
	}

	private fun animate(holder: VH, position: Int) {
		if (isEnableAnimation() && position > lastKnowingPosition) {
			AnimHelper.startBeatsAnimation(holder.itemView)
			lastKnowingPosition = position
		}
	}

	protected abstract fun onBindView(holder: VH, position: Int)

	fun isEnableAnimation(): Boolean {
		return enableAnimation
	}

	fun setEnableAnimation(enableAnimation: Boolean) {
		this.enableAnimation = enableAnimation
		notifyDataSetChanged()
	}

	private fun addSpanLookup(parent: ViewGroup) {
		if (parent is RecyclerView) {
			if (parent.layoutManager is GridLayoutManager) {
				val layoutManager = parent.layoutManager as GridLayoutManager
				layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
					override fun getSpanSize(position: Int): Int {
						return if (getItemViewType(position) == PROGRESS_TYPE) layoutManager.spanCount else 1
					}
				}
			}
		}
	}

	protected abstract fun viewHolder(parent: ViewGroup, viewType: Int): VH

	fun getItem(position: Int): M {
		return data[position]
	}

	fun insertItems(items: List<M>) {
		data.clear()
		data.addAll(items)
		notifyDataSetChanged()
		progressAdded = false
	}

	fun addItem(item: M, position: Int) {
		data.add(position, item)
		notifyItemInserted(position)
	}

	fun addItems(items: List<M>) {
		removeProgress()
		data.addAll(items)
		notifyItemRangeInserted(itemCount, itemCount + items.size - 1)
	}

	private fun removeProgress() {
		if (!isEmpty()) {
			val m = getItem(itemCount - 1)
			if (m == null) {
				removeItem(itemCount - 1)
			}
			progressAdded = false
		}
	}

	fun isEmpty(): Boolean {
		return data.isEmpty()
	}

	fun removeItem(position: Int) {
		data.removeAt(position)
		notifyItemRemoved(position)
	}

	fun removeItem(item: M) {
		val position = data.indexOf(item)
		if (position != -1) {
			removeItem(position)
		}
	}

	fun removeItems(items: List<M>) {
		val prevSize = itemCount
		data.removeAll(items)
		notifyItemRangeRemoved(prevSize, Math.abs(data.size - prevSize))
	}

	fun swapItem(model: M) {
		val index = getItem(model)
		swapItem(model, index)
	}

	fun getItem(t: M): Int {
		return data.indexOf(t)
	}

	fun swapItem(model: M, position: Int) {
		if (position != -1) {
			data.set(position, model)
			notifyItemChanged(position)
		}
	}

	fun subList(fromPosition: Int, toPosition: Int) {
		if (data.isEmpty()) {
			return
		}
		data.subList(fromPosition, toPosition).clear()
		notifyItemRangeRemoved(fromPosition, toPosition)
	}

	fun clear() {
		progressAdded = false
		data.clear()
		notifyDataSetChanged()
	}

	fun getRowWidth(): Int {
		return rowWidth
	}

	fun setRowWidth(rowWidth: Int) {
		if (this.rowWidth == 0) {
			this.rowWidth = rowWidth
			notifyDataSetChanged()
		}
	}

	fun addProgress() {
		if (!progressAdded && !isEmpty()) {
			addItem(null)
			progressAdded = true
		}
	}

	fun addItem(item: M?) {
		removeProgress()
		addNullSafe(data, item);
		if (data.size == 0) {
			notifyDataSetChanged()
		} else {
			notifyItemInserted(data.size - 1)
		}
	}

	private fun <T> addNullSafe(list: MutableList<T>, element: T?): Boolean {
		return if (list.isEmpty() || element == null) {
			false
		} else list.add(element)

	}

	fun isProgressAdded(): Boolean {
		return progressAdded
	}

}
