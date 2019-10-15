package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.pubplans_row_item.view.*
import ru.fantlab.android.App
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Pubplans
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.modules.author.AuthorPagerActivity
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class PubplansViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Pubplans.Object, PubplansViewHolder>)
	: BaseViewHolder<Pubplans.Object>(itemView, adapter) {

	override fun bind(publish: Pubplans.Object) {
		val authorId = "\\[autor=(\\d+)\\]".toRegex().find(publish.autors)
		itemView.avatarLayout.setUrl("https://${LinkParserHelper.HOST_DATA}/images/autors/${authorId?.groupValues?.get(1)}")

		if (!InputHelper.isEmpty(publish.autors)) {
			itemView.pubplansAuthor.text = publish.autors.replace("\\[(.*?)]".toRegex(), "").split(",")[0]
			itemView.authorInfo.setOnClickListener {
				AuthorPagerActivity.startActivity(App.instance.applicationContext, publish.autors.substring(publish.autors.indexOf("=")+1, publish.autors.indexOf("]")).toInt(), itemView.pubplansAuthor.text.toString(), 0)
			}
			itemView.pubplansAuthor.visibility = View.VISIBLE
		} else itemView.pubplansAuthor.text = "Автор не указан"

		itemView.date.text = publish.date

		itemView.pubplansSeries.text = publish.series.replace("\\[(.*?)]".toRegex(), "")
		itemView.workName.text = publish.name
		itemView.pubplansDescription.text = publish.description.replace("\\[(.*?)]".toRegex(), "")

		itemView.coverLayout.setUrl("https://${LinkParserHelper.HOST_DATA}/images/editions/small/${publish.editionId}", R.drawable.ic_edition)

		if (!InputHelper.isEmpty(publish.popularity)) {
			itemView.popularity.text = publish.popularity
			itemView.popularity.visibility = View.VISIBLE
		} else {
			itemView.popularity.text = "0"
		}
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