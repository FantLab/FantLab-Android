package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.response_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.helper.FantlabHelper
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.getTimeAgo
import ru.fantlab.android.helper.parseFullDate
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.provider.storage.WorkTypesProvider
import ru.fantlab.android.ui.modules.work.CyclePagerActivity
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder


class ResponseViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Response, ResponseViewHolder>)
	: BaseViewHolder<Response>(itemView, adapter) {

	override fun bind(response: Response) {

		itemView.avatarLayout.setUrl("https://${LinkParserHelper.HOST_DATA}/images/users/${response.userId}")
		itemView.responseUser.text = response.userName.capitalize()

		itemView.userInfo.setOnClickListener {
			listener?.onOpenContextMenu(response)
		}
		itemView.date.text = response.dateIso.parseFullDate(true).getTimeAgo()

		itemView.authors.text = if (!InputHelper.isEmpty(response.workAuthor)) response.workAuthor else response.workAuthorOrig
		itemView.workName.text = if (response.workName.isNotEmpty()) response.workName else response.workNameOrig

		itemView.coverLayout.setUrl("https:${response.workImage}", WorkTypesProvider.getCoverByTypeId(response.workTypeId))

		itemView.coverLayout.setOnClickListener {
			if (response.workTypeId == FantlabHelper.WorkType.WORK_TYPE_CYCLE.id)
				CyclePagerActivity.startActivity(itemView.context, response.workId, response.workName, 0)
			else
				WorkPagerActivity.startActivity(itemView.context, response.workId, response.workName, 0)
		}

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