package ru.fantlab.android.ui.adapter.viewholder

import android.support.annotation.NonNull
import android.view.View
import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.News
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class NewsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<News, NewsViewHolder, *>)
	: BaseViewHolder<News>(itemView, adapter) {

	override fun bind(t: News) {
		// todo implement
	}

	companion object {

		fun getView(@NonNull viewGroup: ViewGroup, noImage: Boolean): View {
			return null!!
		}
	}
}