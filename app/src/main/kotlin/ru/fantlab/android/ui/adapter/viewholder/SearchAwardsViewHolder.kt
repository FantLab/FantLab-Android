package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.SearchAward
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DATA
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.ui.widgets.AvatarLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class SearchAwardsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<SearchAward, SearchAwardsViewHolder>)
	: BaseViewHolder<SearchAward>(itemView, adapter) {

	@BindView(R.id.avatarLayout) lateinit var avatarLayout: AvatarLayout
	@BindView(R.id.name) lateinit var name: FontTextView
	@BindView(R.id.country) lateinit var country: FontTextView
	@BindView(R.id.dates) lateinit var dates: FontTextView

	override fun bind(award: SearchAward) {
		avatarLayout.setUrl(
				Uri.Builder().scheme(PROTOCOL_HTTPS)
						.authority(HOST_DATA)
						.appendPath("images")
						.appendPath("awards")
						.appendPath(award.id.toString())
						.toString())

		name.text = if (award.rusName.isNotEmpty()) {
			if (award.name.isNotEmpty()) {
				String.format("%s / %s", award.rusName, award.name)
			} else {
				award.rusName
			}
		} else {
			award.name
		}

		country.text = if (award.country.isNotEmpty()) award.country else "N/A"

		dates.text = StringBuilder()
				.append(if (award.openYear != 0) award.openYear else "N/A")
				.append(if (award.closeYear != 0) " - ${award.closeYear}" else "")
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<SearchAward, SearchAwardsViewHolder>
		): SearchAwardsViewHolder =
				SearchAwardsViewHolder(getView(viewGroup, R.layout.search_awards_row_item), adapter)
	}
}