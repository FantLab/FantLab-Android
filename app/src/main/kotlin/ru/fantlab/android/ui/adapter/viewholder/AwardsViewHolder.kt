package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.BindView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.AwardInList
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DEFAULT
import ru.fantlab.android.ui.widgets.AvatarLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class AwardsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<AwardInList, AwardsViewHolder, *>)
	: BaseViewHolder<AwardInList>(itemView, adapter) {

	@BindView(R.id.awardIcon) lateinit var coverLayout: AvatarLayout
	@BindView(R.id.langIcon) lateinit var langLayout: ImageView
	@BindView(R.id.name_rus) lateinit var nameRus: FontTextView
	@BindView(R.id.name_orig) lateinit var nameOrig: FontTextView
	@BindView(R.id.country) lateinit var country: FontTextView
	@BindView(R.id.noms) lateinit var noms: FontTextView
	@BindView(R.id.years) lateinit var years: FontTextView

	override fun bind(nom: AwardInList) {
		coverLayout.setUrl("https://${LinkParserHelper.HOST_DATA}/images/awards/${nom.id}")
		Glide.with(itemView.context)
				.load("https://$HOST_DEFAULT/img/flags/${nom.countryId}.png")
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.into(langLayout)
		nameRus.text = nom.nameRus
		if (!nom.nameOrig.isEmpty()) {
			nameOrig.text = nom.nameOrig
			nameOrig.visibility = View.VISIBLE
		}
		country.text = StringBuilder()
				.append(nom.countryName)
				.append(", ")
				.append(nom.langName)
		noms.text = StringBuilder()
				.append(nom.nomCount)
				.append(" | ")
				.append(nom.contestsCount)
		years.text = StringBuilder()
				.append(nom.minDate.split("-")[0])
				.append("-")
				.append(nom.maxDate.split("-")[0])
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<AwardInList, AwardsViewHolder, *>
		): AwardsViewHolder =
				AwardsViewHolder(getView(viewGroup, R.layout.awardlist_row_item), adapter)
	}
}