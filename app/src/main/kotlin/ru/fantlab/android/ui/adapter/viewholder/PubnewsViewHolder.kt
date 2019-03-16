package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.restyle_pubnews_row_item.view.*
import ru.fantlab.android.App
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Pubnews
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.modules.author.AuthorPagerActivity
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder


class PubnewsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Pubnews.Object, PubnewsViewHolder>)
	: BaseViewHolder<Pubnews.Object>(itemView, adapter) {

	override fun bind(publish: Pubnews.Object) {

		val authorId = "\\[autor=(\\d+)\\]".toRegex().find(publish.autors)
		itemView.avatarLayout.setUrl("https://${LinkParserHelper.HOST_DATA}/images/autors/${authorId?.groupValues?.get(1)}")

		if (!InputHelper.isEmpty(publish.autors)) {
			itemView.pubnewsAuthor.text = publish.autors.replace("\\[(.*?)]".toRegex(), "").split(",")[0]
			itemView.authorInfo.setOnClickListener {
				AuthorPagerActivity.startActivity(App.instance.applicationContext, publish.autors.substring(publish.autors.indexOf("=")+1, publish.autors.indexOf("]")).toInt(), itemView.pubnewsAuthor.text.toString(), 0)
			}
			itemView.pubnewsAuthor.visibility = View.VISIBLE
		} else itemView.pubnewsAuthor.text = "Автор не указан"

		itemView.date.text = publish.date

		itemView.pubnewsSeries.text = publish.series.replace("\\[(.*?)]".toRegex(), "")
		itemView.workName.text = publish.name
		itemView.pubnewsDescription.text = publish.description.replace("\\[(.*?)]".toRegex(), "")

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
				adapter: BaseRecyclerAdapter<Pubnews.Object, PubnewsViewHolder>
		): PubnewsViewHolder {
			return PubnewsViewHolder(getView(viewGroup, R.layout.restyle_pubnews_row_item), adapter)
		}

	}
}