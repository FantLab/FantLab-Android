package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.helper.getTimeAgo
import ru.fantlab.android.ui.widgets.AvatarLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class UserResponseViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Response, UserResponseViewHolder, *>)
	: BaseViewHolder<Response>(itemView, adapter) {

	@BindView(R.id.avatarLayout) lateinit var avatarLayout: AvatarLayout
	@BindView(R.id.info) lateinit var info: FontTextView
	@BindView(R.id.workName) lateinit var workName: FontTextView
	@BindView(R.id.text) lateinit var text: FontTextView
	@BindView(R.id.rating) lateinit var rating: FontTextView
	@BindView(R.id.votes) lateinit var votes: FontTextView

	override fun bind(response: Response) {
		info.text = StringBuilder()
				.append(response.userName)
				.append(", ")
				.append(response.responseDate!!.getTimeAgo())
		workName.text = if (!response.workName.isNullOrEmpty()) {
			if (!response.workNameOrig.isNullOrEmpty()) {
				String.format("%s / %s", response.workName, response.workNameOrig)
			} else {
				response.workName
			}
		} else {
			response.workNameOrig
		}
		text.text = response.responseText!!
				.replace("(\r\n)+".toRegex(), "\n")    // пустые переносы строк
				.replace("\\[.*]".toRegex(), "")       // bb-коды
				.replace(":.*:".toRegex(), "")         // смайлы
		if (response.mark == null) {
			rating.visibility = View.GONE
		} else {
			rating.text = response.mark.toString()
		}
		votes.text = response.responseVotes.toString()
	}

	companion object {

		fun newInstance(viewGroup: ViewGroup, adapter: BaseRecyclerAdapter<Response, UserResponseViewHolder, *>) : UserResponseViewHolder
				= UserResponseViewHolder(getView(viewGroup, R.layout.response_row_item), adapter)
	}
}