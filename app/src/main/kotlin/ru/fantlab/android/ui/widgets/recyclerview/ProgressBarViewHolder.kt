package ru.fantlab.android.ui.widgets.recyclerview

import android.view.View
import android.view.ViewGroup
import ru.fantlab.android.R

/**
 * Created by kosh on 03/08/2017.
 */

class ProgressBarViewHolder private constructor(itemView: View) : BaseViewHolder<Any>(itemView) {

	override fun bind(o: Any) {
	}

	companion object {

		fun newInstance(viewGroup: ViewGroup): ProgressBarViewHolder {
			return ProgressBarViewHolder(getView(viewGroup, R.layout.progress_layout))
		}
	}
}
