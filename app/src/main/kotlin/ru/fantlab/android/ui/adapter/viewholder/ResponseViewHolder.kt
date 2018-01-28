package ru.fantlab.android.ui.adapter.viewholder

import android.support.annotation.NonNull
import android.view.View
import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class ResponseViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Response, ResponseViewHolder, *>) : BaseViewHolder<Response>(itemView, adapter) {

	override fun bind(t: Response) {
		// todo implement
	}

	companion object {

		fun getView(@NonNull viewGroup: ViewGroup, noImage: Boolean): View {
			return null!!
		}
	}
}