package org.odddev.fantlab.core.bind

import android.support.v7.widget.RecyclerView

interface Provider {

	fun setAdapter(adapter: RecyclerView.Adapter<*>)

	fun getVHFactory(position: Int): VHFactory<*>

	fun bindVH(holder: RecyclerView.ViewHolder, position: Int)

	fun getEntry(position: Int): Any?

	fun getEntryCount(): Int
}