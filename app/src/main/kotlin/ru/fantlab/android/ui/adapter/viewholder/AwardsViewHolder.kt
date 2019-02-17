package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.awardlist_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.AwardInList
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DEFAULT
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class AwardsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<AwardInList, AwardsViewHolder>)
	: BaseViewHolder<AwardInList>(itemView, adapter) {

	override fun bind(nom: AwardInList) {
		itemView.awardIcon.setUrl("https://${LinkParserHelper.HOST_DATA}/images/awards/${nom.id}")
		Glide.with(itemView.context)
				.load("https://$HOST_DEFAULT/img/flags/${nom.countryId}.png")
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.into(itemView.langIcon)
		itemView.name_rus.text = nom.nameRus
		if (!nom.nameOrig.isEmpty()) {
			itemView.name_orig.text = nom.nameOrig
			itemView.name_orig.visibility = View.VISIBLE
		}
		itemView.country.text = StringBuilder()
				.append(nom.countryName)
				.append(", ")
				.append(nom.langName)
		itemView.noms.text = StringBuilder()
				.append(nom.nomCount)
				.append(" | ")
				.append(nom.contestsCount)
		itemView.years.text = StringBuilder()
				.append(nom.minDate.split("-")[0])
				.append("-")
				.append(nom.maxDate.split("-")[0])
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<AwardInList, AwardsViewHolder>
		): AwardsViewHolder =
				AwardsViewHolder(getView(viewGroup, R.layout.awardlist_row_item), adapter)
	}
}