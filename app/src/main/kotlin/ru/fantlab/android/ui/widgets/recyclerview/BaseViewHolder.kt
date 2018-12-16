package ru.fantlab.android.ui.widgets.recyclerview

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife

abstract class BaseViewHolder<T>(itemView: View, protected var adapter: BaseRecyclerAdapter<T, *>?) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

	protected constructor(itemView: View) : this(itemView, null)

	private val item: T? = null

	init {
		ButterKnife.bind(this, itemView)
		itemView.setOnClickListener(this)
		itemView.setOnLongClickListener(this)
	}

	override fun onClick(v: View) {
		if (adapter != null && adapter?.listener != null) {
			val position = adapterPosition
			if (position != RecyclerView.NO_POSITION && position < adapter!!.getItemCount()) {
				adapter?.listener?.onItemClick(position, v, adapter?.getItem(position)!!)
			}
		}
	}

	override fun onLongClick(v: View): Boolean {
		if (adapter != null && adapter?.listener != null) {
			val position = adapterPosition
			if (position != RecyclerView.NO_POSITION && position < adapter!!.getItemCount()) {
				adapter?.listener?.onItemLongClick(position, v, adapter?.getItem(position)!!)
			}
		}
		return true
	}

	abstract fun bind(t: T)

	fun onViewIsDetaching() {}

	interface OnItemClickListener<T> {

		fun onItemClick(position: Int, v: View?, item: T)

		fun onItemLongClick(position: Int, v: View?, item: T)
	}

	companion object {

		fun getView(parent: ViewGroup, @LayoutRes layoutRes: Int): View {
			return LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
		}

	}

}