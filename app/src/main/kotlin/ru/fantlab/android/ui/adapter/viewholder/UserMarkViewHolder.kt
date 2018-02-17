package ru.fantlab.android.ui.adapter.viewholder

import android.support.annotation.NonNull
import android.view.View
import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.UserMark
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class UserMarkViewHolder(itemView: View, adapter: BaseRecyclerAdapter<UserMark, UserMarkViewHolder, *>)
	: BaseViewHolder<UserMark>(itemView, adapter) {

	override fun bind(t: UserMark) {
		// todo implement
	}

	companion object {

		fun getView(@NonNull viewGroup: ViewGroup): View {
			return null!!
		}
	}
}