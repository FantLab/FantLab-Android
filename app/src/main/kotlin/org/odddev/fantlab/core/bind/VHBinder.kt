package org.odddev.fantlab.core.bind

import android.support.v7.widget.RecyclerView

interface VHBinder<in VH : RecyclerView.ViewHolder, in T> {

	fun onBindViewHolder(holder: VH, position: Int, entry: T)
}
