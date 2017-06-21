package org.odddev.fantlab.core.bind

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

interface VHFactory<out VH : RecyclerView.ViewHolder> {

	fun onCreateViewHolder(parent: ViewGroup): VH
}
