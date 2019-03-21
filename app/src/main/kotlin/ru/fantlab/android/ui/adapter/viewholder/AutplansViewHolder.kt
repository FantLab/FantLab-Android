package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.autplans_row_item.view.*
import ru.fantlab.android.App
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Autplans
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.provider.storage.WorkTypesProvider
import ru.fantlab.android.ui.modules.author.AuthorPagerActivity
import ru.fantlab.android.ui.modules.work.CyclePagerActivity
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class AutplansViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Autplans.Object, AutplansViewHolder>)
	: BaseViewHolder<Autplans.Object>(itemView, adapter) {

	override fun bind(autplan: Autplans.Object) {
		itemView.avatarLayout.setUrl("https://${LinkParserHelper.HOST_DATA}/images/autors/${autplan.autors.autorId}")
		itemView.coverLayout.setUrl(null, WorkTypesProvider.getCoverByTypeName(autplan.workType))

		if (!InputHelper.isEmpty(autplan.autors.autorRusname)) {
			itemView.autplansAuthor.text = autplan.autors.autorRusname.replace("\\[(.*?)]".toRegex(), "").split(",")[0]
			itemView.authorInfo.setOnClickListener {
				AuthorPagerActivity.startActivity(App.instance.applicationContext, autplan.autors.autorId.toInt(), itemView.autplansAuthor.text.toString(), 0)
			}
			itemView.autplansAuthor.visibility = View.VISIBLE
		} else itemView.autplansAuthor.visibility = View.GONE

		if (!InputHelper.isEmpty(autplan.year)) {
			itemView.date.text = autplan.year
			itemView.date.visibility = View.VISIBLE
		} else {
			itemView.date.visibility = View.GONE
		}

		if (!autplan.saga.workType.isNullOrEmpty() && autplan.saga.workId != "0") {
			val sagaName = buildString {
				if (!autplan.saga.workType.isEmpty()) {
					append(autplan.saga.workType.capitalize())
					append(" ")
				}
				append(autplan.saga.rusname)
			}
			itemView.autplansSaga.text = sagaName
			itemView.autplansSaga.setOnClickListener { CyclePagerActivity.startActivity(itemView.context, autplan.saga.workId.toInt(), sagaName, 0) }
			itemView.autplansSaga.visibility = View.VISIBLE
		} else {
			itemView.autplansSaga.visibility = View.GONE
		}

		itemView.workName.text = if (!InputHelper.isEmpty(autplan.rusname)) autplan.rusname else autplan.name
		if (!InputHelper.isEmpty(autplan.description))
			itemView.autplansDescription.text = autplan.description.replace("\\[(.*?)]".toRegex(), "").trim()
		else itemView.autplansDescription.visibility = View.GONE

		if (!InputHelper.isEmpty(autplan.popularity)) {
			itemView.popularity.text = autplan.popularity
			itemView.popularity.visibility = View.VISIBLE
		} else {
			itemView.popularity.text = "0"
		}
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<Autplans.Object, AutplansViewHolder>
		): AutplansViewHolder {
			return AutplansViewHolder(getView(viewGroup, R.layout.autplans_row_item), adapter)
		}

	}
}