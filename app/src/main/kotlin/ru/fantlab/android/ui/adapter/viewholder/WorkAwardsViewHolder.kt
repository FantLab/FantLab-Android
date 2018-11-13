package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Nomination
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.widgets.AvatarLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class WorkAwardsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Nomination, WorkAwardsViewHolder, *>)
	: BaseViewHolder<Nomination>(itemView, adapter) {

	@BindView(R.id.awardIcon) lateinit var coverLayout: AvatarLayout
	@BindView(R.id.award) lateinit var award: FontTextView

	override fun bind(nom: Nomination) {
		coverLayout.setUrl("https://${LinkParserHelper.HOST_DATA}/images/awards/${nom.awardId}")
		award.text = StringBuilder()
				.append(nom.awardName)
				.append(" / ")
				.append(nom.awardRusName)
				.append(",")
				.append(nom.contestName)
				.append(" // ")
				.append(nom.nominationRusName)
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<Nomination, WorkAwardsViewHolder, *>
		): WorkAwardsViewHolder =
				WorkAwardsViewHolder(getView(viewGroup, R.layout.award_row_item), adapter)
	}
}