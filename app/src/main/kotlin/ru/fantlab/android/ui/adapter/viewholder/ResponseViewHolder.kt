package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.helper.getTimeAgo
import ru.fantlab.android.helper.parseFullDate
import ru.fantlab.android.ui.widgets.CoverLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class ResponseViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Response, ResponseViewHolder, *>)
	: BaseViewHolder<Response>(itemView, adapter) {

	@JvmField @BindView(R.id.coverLayout) var coverLayout: CoverLayout? = null
	@BindView(R.id.info) lateinit var info: FontTextView
	@BindView(R.id.workName) lateinit var workName: FontTextView
	@BindView(R.id.text) lateinit var text: FontTextView
	@BindView(R.id.rating) lateinit var rating: FontTextView
	@BindView(R.id.votes) lateinit var votes: FontTextView

	override fun bind(response: Response) {
		coverLayout?.setUrl(if (response.workImage != null) "https:${response.workImage}" else null)
		info.text = StringBuilder()
				.append(response.userName)
				.append(", ")
				.append(response.dateIso.parseFullDate(true).getTimeAgo())
		info.setOnClickListener {
			listener?.onOpenContextMenu(response)
		}

		workName.text = if (response.workName.isNotEmpty()) {
			if (response.workNameOrig.isNotEmpty()) {
				String.format("%s / %s", response.workName, response.workNameOrig)
			} else {
				response.workName
			}
		} else {
			response.workNameOrig
		}

		text.text = response.text
				.replace("(\r\n)+".toRegex(), "\n")    // пустые переносы строк
				.replace("\\[spoiler].*|\\[\\/spoiler]".toRegex(), "")
				.replace("\\[.*]".toRegex(), "")       // bb-коды
				.replace(":\\w+:".toRegex(), "")       // смайлы

		if (response.mark == null) {
			rating.visibility = View.GONE
		} else {
			rating.text = response.mark.toString()
			rating.visibility = View.VISIBLE
		}

		if (response.voteCount > 0) {
			votes.text = response.voteCount.toString()
		} else {
			votes.visibility = View.GONE
		}
	}

	interface OnOpenContextMenu {
		fun onOpenContextMenu(userItem: Response)
	}

	companion object {
		private var listener: OnOpenContextMenu? = null

		fun newInstance(
				viewGroup: ViewGroup,
				noImage: Boolean,
				adapter: BaseRecyclerAdapter<Response, ResponseViewHolder, *>
		): ResponseViewHolder {
			return if (noImage) {
				ResponseViewHolder(getView(viewGroup, R.layout.response_row_no_image_item), adapter)
			} else {
				ResponseViewHolder(getView(viewGroup, R.layout.response_row_item), adapter)
			}
		}

		fun setOnContextMenuListener(listener: ResponseViewHolder.OnOpenContextMenu) {
			this.listener = listener
		}
	}
}