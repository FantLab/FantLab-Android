package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.work_response_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.helper.getTimeAgo
import ru.fantlab.android.helper.parseFullDate
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder


class WorkResponseViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Response, WorkResponseViewHolder>)
	: BaseViewHolder<Response>(itemView, adapter) {

	override fun bind(response: Response) {
		itemView.avatarLayout.setUrl("https://${LinkParserHelper.HOST_DATA}/images/autors/${response.userId}")
		itemView.responseUser.text = response.userName.capitalize()
		itemView.date.text = response.dateIso.parseFullDate(true).getTimeAgo()

		itemView.responseText.text = response.text
				.replace("(\r\n)+".toRegex(), "\n")
				.replace("\\[spoiler].*|\\[\\/spoiler]".toRegex(), "")
				.replace("\\[.*]".toRegex(), "")
				.replace(":\\w+:".toRegex(), "")

		if (response.mark == null) {
			itemView.rating.visibility = View.GONE
		} else {
			itemView.rating.text = response.mark.toString()
			itemView.rating.visibility = View.VISIBLE
		}

		response.voteCount.let {
			when {
				it < 0 -> {
					itemView.votes.setDrawables(R.drawable.ic_thumb_down)
					itemView.votes.text = response.voteCount.toString()
					itemView.votes.visibility = View.VISIBLE
				}
				it > 0 -> {
					itemView.votes.setDrawables(R.drawable.ic_thumb_up)
					itemView.votes.text = response.voteCount.toString()
					itemView.votes.visibility = View.VISIBLE
				}
				else -> itemView.votes.visibility = View.GONE
			}
		}

		itemView.userInfo.setOnClickListener { listener?.onOpenContextMenu(response) }
	}

	interface OnOpenContextMenu {
		fun onOpenContextMenu(userItem: Response)
	}

	companion object {
		private var listener: OnOpenContextMenu? = null

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<Response, WorkResponseViewHolder>
		): WorkResponseViewHolder {
			return WorkResponseViewHolder(getView(viewGroup, R.layout.work_response_row_item), adapter)
		}

		fun setOnContextMenuListener(listener: WorkResponseViewHolder.OnOpenContextMenu) {
			this.listener = listener
		}
	}
}