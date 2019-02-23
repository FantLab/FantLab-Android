package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.publishers_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Publishers
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DATA
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder


class PublishersViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Publishers.Publisher, PublishersViewHolder>)
	: BaseViewHolder<Publishers.Publisher>(itemView, adapter) {

	override fun bind(publisher: Publishers.Publisher) {

		itemView.publisherIcon.setUrl(Uri.Builder().scheme(PROTOCOL_HTTPS)
				.authority(HOST_DATA)
				.appendPath("images")
				.appendPath("publisher")
				.appendPath(publisher.publisherId + "_1")
				.toString(), R.drawable.ic_edition)

		Glide.with(itemView.context).load(Uri.Builder().scheme(PROTOCOL_HTTPS)
				.authority(HOST_DATA)
				.appendPath("img")
				.appendPath("flags")
				.appendPath(publisher.countryId + ".png")
				.toString())
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.into(itemView.langIcon)

		itemView.publisherName.text = publisher.name
		itemView.country.text = publisher.countryName ?: ""
		itemView.editions.text = publisher.editionsCount
		itemView.years.text = if (!InputHelper.isNullEmpty(publisher.yearOpen) && !InputHelper.isNullEmpty(publisher.yearClose)) {
			StringBuilder()
					.append(publisher.yearOpen)
					.append(" - ")
					.append(publisher.yearClose)
		} else if (!InputHelper.isNullEmpty(publisher.yearClose)) {
			publisher.yearOpen
		} else "-"
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<Publishers.Publisher, PublishersViewHolder>
		): PublishersViewHolder {
			return PublishersViewHolder(getView(viewGroup, R.layout.publishers_row_item), adapter)
		}

	}
}