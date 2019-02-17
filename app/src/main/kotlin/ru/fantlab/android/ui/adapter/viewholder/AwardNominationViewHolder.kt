package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.award_nomination_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Award
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class AwardNominationViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Award.Nominations, AwardNominationViewHolder>)
	: BaseViewHolder<Award.Nominations>(itemView, adapter) {

	override fun bind(award: Award.Nominations) {
		itemView.title.text = if (award.rusname.isNotEmpty()) {
			if (award.name.isNotEmpty()) {
				String.format("%s / %s", award.rusname, award.name)
			} else {
				award.rusname
			}
		} else {
			award.name
		}
		if (!award.description.isEmpty()) {
			itemView.description.text = award.description
			itemView.description.visibility = View.VISIBLE
		} else {
			itemView.description.text = ""
			itemView.description.visibility = View.GONE
		}
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<Award.Nominations, AwardNominationViewHolder>
		): AwardNominationViewHolder =
				AwardNominationViewHolder(getView(viewGroup, R.layout.award_nomination_row_item), adapter)
	}
}