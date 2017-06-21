package org.odddev.fantlab.core.bind

import android.support.v7.widget.RecyclerView

interface VHBinder<VH : RecyclerView.ViewHolder, T> {

	fun onBindViewHolder(holder: VH, position: Int, entry: T)
}
