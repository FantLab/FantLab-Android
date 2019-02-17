package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
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
		itemView.awardIcon.setUrl("https://${LinkParserHelper.HOST_DATA}/images/awards/${nom.awardId}")
		val info = StringBuilder()
		if (!InputHelper.isEmpty(nom.awardRusName)) {
			info.append(nom.awardRusName).append(" / ")
		}
		if (!InputHelper.isEmpty(nom.awardName)) {
			info.append(nom.awardName).append(", ")
		}
		if (!InputHelper.isEmpty(nom.contestName)) {
			info.append(nom.contestName).append(" // ")
		}
		if (!InputHelper.isEmpty(nom.nominationRusName)) {
			info.append(nom.nominationRusName)
		}
		itemView.award.text = info.toString()
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<Nomination, WorkAwardsViewHolder>
		): WorkAwardsViewHolder =
				WorkAwardsViewHolder(getView(viewGroup, R.layout.award_row_item), adapter)
	}
}