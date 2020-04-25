package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.attach_upload_row.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.AttachModel
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class AttachUploadViewHolder(itemView: View, adapter: BaseRecyclerAdapter<AttachModel, AttachUploadViewHolder>)
	: BaseViewHolder<AttachModel>(itemView, adapter) {

	override fun bind(attach: AttachModel) {
		itemView.fileName.text = attach.filename
		itemView.progress.progress = attach.progress
		itemView.progressText.text = if (attach.progress == -1) "∞" else "${attach.progress}%"
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<AttachModel, AttachUploadViewHolder>
		): AttachUploadViewHolder {
			return AttachUploadViewHolder(getView(viewGroup, R.layout.attach_upload_row), adapter)
		}
	}


}