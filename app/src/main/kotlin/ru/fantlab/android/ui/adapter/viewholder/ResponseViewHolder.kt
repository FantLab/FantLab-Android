package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.response_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.helper.FantlabHelper
import ru.fantlab.android.helper.getTimeAgo
import ru.fantlab.android.helper.parseFullDate
import ru.fantlab.android.ui.modules.work.CyclePagerActivity
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder


class ResponseViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Response, ResponseViewHolder>)
	: BaseViewHolder<Response>(itemView, adapter) {

	override fun bind(response: Response) {
		itemView.coverLayout.setUrl(if (response.workImage != null) "https:${response.workImage}" else null)

		itemView.info.text = StringBuilder()
				.append(response.userName)
				.append(", ")
				.append(response.dateIso.parseFullDate(true).getTimeAgo())
		itemView.info.setOnClickListener {
			listener?.onOpenContextMenu(response)
		}

		itemView.coverLayout.setOnClickListener {
			if (response.workTypeId == FantlabHelper.WorkType.CYCLE.id)
				CyclePagerActivity.startActivity(itemView.context, response.workId, response.workName, 0)
			else
				WorkPagerActivity.startActivity(itemView.context, response.workId, response.workName, 0)
		}

		itemView.workName.text = if (response.workName.isNotEmpty()) {
			if (response.workNameOrig.isNotEmpty()) {
				String.format("%s / %s", response.workName, response.workNameOrig)
			} else {
				response.workName
			}
		} else {
			response.workNameOrig
		}

		itemView.text.text = response.text
				.replace("(\r\n)+".toRegex(), "\n")    // пустые переносы строк
				.replace("\\[spoiler].*|\\[\\/spoiler]".toRegex(), "")
				.replace("\\[.*]".toRegex(), "")       // bb-коды
				.replace(":\\w+:".toRegex(), "")       // смайлы

		if (response.mark == null) {
			itemView.rating.visibility = View.GONE
		} else {
			itemView.rating.text = response.mark.toString()
			itemView.rating.visibility = View.VISIBLE
		}

		response.voteCount.let {
			when {
				it < 0 -> {
					itemView.votes.setDrawables(R.drawable.ic_thumb_down_small)
					itemView.votes.text = response.voteCount.toString()
					itemView.votes.visibility = View.VISIBLE
				}
				it > 0 -> {
					itemView.votes.setDrawables(R.drawable.ic_thumb_up_small)
					itemView.votes.text = response.voteCount.toString()
					itemView.votes.visibility = View.VISIBLE
				}
				else -> itemView.votes.visibility = View.GONE
			}
		}
	}

	interface OnOpenContextMenu {
		fun onOpenContextMenu(userItem: Response)
	}

	companion object {
		private var listener: OnOpenContextMenu? = null

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<Response, ResponseViewHolder>
		): ResponseViewHolder {
			return ResponseViewHolder(getView(viewGroup, R.layout.response_row_item), adapter)
		}

		fun setOnContextMenuListener(listener: ResponseViewHolder.OnOpenContextMenu) {
			this.listener = listener
		}
	}
}