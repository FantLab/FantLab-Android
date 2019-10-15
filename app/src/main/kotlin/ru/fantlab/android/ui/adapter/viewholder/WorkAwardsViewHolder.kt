package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.award_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Nomination
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class WorkAwardsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Nomination, WorkAwardsViewHolder>)
	: BaseViewHolder<Nomination>(itemView, adapter) {

	override fun bind(nom: Nomination) {
		Glide.with(itemView.context).load("https://${LinkParserHelper.HOST_DATA}/images/awards/${nom.awardId}")
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(itemView.awardIcon)

		itemView.awardInfo.text = StringBuilder()
				.append(if (!InputHelper.isNullEmpty(nom.awardRusName)) nom.awardRusName else nom.awardName)
				.append(", " + nom.contestYear)
				.toString()

		val awardType = if (!InputHelper.isNullEmpty(nom.nominationRusName)) nom.nominationRusName else if (!InputHelper.isNullEmpty(nom.nominationName)) nom.nominationName else ""

		if (awardType.isNotEmpty()) itemView.awardType.text = awardType else itemView.awardType.visibility = View.GONE

		itemView.awardWorkType.text = if (nom.isWinner == 1) itemView.context.getString(R.string.winner) else itemView.context.getString(R.string.laureate)

	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<Nomination, WorkAwardsViewHolder>
		): WorkAwardsViewHolder =
				WorkAwardsViewHolder(getView(viewGroup, R.layout.award_row_item), adapter)
	}
}