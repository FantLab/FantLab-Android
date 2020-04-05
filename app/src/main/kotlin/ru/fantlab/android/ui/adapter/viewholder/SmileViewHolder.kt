package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.smile_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Smile
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class SmileViewHolder private constructor(view: View, adapter: BaseRecyclerAdapter<Smile, *>)
	: BaseViewHolder<Smile>(view, adapter) {

	override fun bind(t: Smile) {
		Glide.with(itemView.context)
				.load("file:///android_asset/smiles/${t.id}.png")
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.into(itemView.smile)
	}

	companion object {
		fun newInstance(parent: ViewGroup, adapter: BaseRecyclerAdapter<Smile, *>): SmileViewHolder {
			return SmileViewHolder(getView(parent, R.layout.smile_row_item), adapter)
		}
	}
}