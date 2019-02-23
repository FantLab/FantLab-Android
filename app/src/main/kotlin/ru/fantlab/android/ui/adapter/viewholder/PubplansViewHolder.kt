package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.pubplans_row_item.view.*
import ru.fantlab.android.App
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Pubplans
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DATA
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.ui.modules.author.AuthorPagerActivity
import ru.fantlab.android.ui.modules.edition.EditionPagerActivity
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class PubplansViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Pubplans.Object, PubplansViewHolder>)
	: BaseViewHolder<Pubplans.Object>(itemView, adapter) {

	override fun bind(publish: Pubplans.Object) {
		itemView.coverLayout.setUrl(Uri.Builder().scheme(PROTOCOL_HTTPS)
				.authority(HOST_DATA)
				.appendPath("images")
				.appendPath("editions")
				.appendPath("small")
				.appendPath(publish.editionId)
				.toString(), R.drawable.ic_edition)

		itemView.author.text = publish.autors.replace("\\[(.*?)]".toRegex(), "")
		itemView.author.setOnClickListener {
			AuthorPagerActivity.startActivity(App.instance.applicationContext, publish.autors.substring(publish.autors.indexOf("=")+1, publish.autors.indexOf("]")).toInt(), itemView.author.text.toString(), 0)
		}
		itemView.name.text = publish.name
		itemView.description.html = publish.description
		itemView.date.text = publish.date.capitalize()
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<Pubplans.Object, PubplansViewHolder>
		): PubplansViewHolder {
			return PubplansViewHolder(getView(viewGroup, R.layout.pubplans_row_item), adapter)
		}

	}
}