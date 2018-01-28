package ru.fantlab.android.ui.adapter.viewholder

import android.support.annotation.NonNull
import android.view.View
import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.ForumMessage
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class ForumMessageViewHolder(itemView: View, adapter: BaseRecyclerAdapter<ForumMessage, ForumMessageViewHolder, *>)
	: BaseViewHolder<ForumMessage>(itemView, adapter) {

	override fun bind(t: ForumMessage) {
		// todo implement
	}

	companion object {

		fun getView(@NonNull viewGroup: ViewGroup, noImage: Boolean): View {
			return null!!
		}
	}
}