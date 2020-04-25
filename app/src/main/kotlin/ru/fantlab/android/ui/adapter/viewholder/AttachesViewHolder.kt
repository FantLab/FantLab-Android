package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.attach_list_row.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.AttachModel
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder


class AttachesViewHolder(itemView: View, adapter: BaseRecyclerAdapter<AttachModel, AttachesViewHolder>)
	: BaseViewHolder<AttachModel>(itemView, adapter) {

	override fun bind(attach: AttachModel) {
		itemView.fileName.text = attach.filename
		itemView.filePath.text = attach.filepath
		itemView.deleteAttach.setOnClickListener { listener?.onDelete(attach) }
	}

	interface OnClickMenu {
		fun onDelete(attach: AttachModel)
	}

	companion object {
		private var listener: OnClickMenu? = null

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<AttachModel, AttachesViewHolder>
		): AttachesViewHolder {
			return AttachesViewHolder(getView(viewGroup, R.layout.attach_list_row), adapter)
		}

		fun setOnClickMenuListener(listener: OnClickMenu) {
			this.listener = listener
		}
	}


}