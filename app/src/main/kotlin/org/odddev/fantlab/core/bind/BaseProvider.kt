package org.odddev.fantlab.core.bind

import android.support.v7.widget.RecyclerView

abstract class BaseProvider : Provider {

	private lateinit var adapter: RecyclerView.Adapter<*>

	override fun setAdapter(adapter: RecyclerView.Adapter<*>) {
		this.adapter = adapter
	}

	fun getAdapter() = adapter

	fun notifyDataSetChanged() {
		adapter.notifyDataSetChanged()
	}

	fun notifyItemChanged(position: Int) {
		adapter.notifyItemChanged(position)
	}

	fun notifyItemRemoved(position: Int) {
		adapter.notifyItemRemoved(position)
	}

	fun notifyItemInserted(position: Int) {
		adapter.notifyItemInserted(position)
	}

	fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) {
		adapter.notifyItemRangeChanged(positionStart, itemCount)
	}

	fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {
		adapter.notifyItemRangeInserted(positionStart, itemCount)
	}

	fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
		adapter.notifyItemRangeRemoved(positionStart, itemCount)
	}
}