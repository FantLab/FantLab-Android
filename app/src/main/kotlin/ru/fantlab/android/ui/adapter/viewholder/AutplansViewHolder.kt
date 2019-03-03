package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.autplans_row_item.view.*
import ru.fantlab.android.App
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Autplans
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DATA
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.ui.modules.author.AuthorPagerActivity
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class AutplansViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Autplans.Object, AutplansViewHolder>)
	: BaseViewHolder<Autplans.Object>(itemView, adapter) {

	override fun bind(autplan: Autplans.Object) {
		itemView.coverLayout.setUrl(Uri.Builder().scheme(PROTOCOL_HTTPS)
				.authority(HOST_DATA)
				.appendPath("images")
				.appendPath("autors")
				.appendPath("small")
				.appendPath(autplan.autors.autorId)
				.toString())

		itemView.author.text = autplan.autors.autorRusname
		itemView.author.setOnClickListener {
			AuthorPagerActivity.startActivity(App.instance.applicationContext, autplan.autors.autorId.toInt(), itemView.author.text.toString(), 0)
		}

		itemView.name.text = if (!InputHelper.isEmpty(autplan.rusname)) autplan.rusname else autplan.name

		if (!InputHelper.isEmpty(autplan.description)) {
			itemView.description.html = autplan.description
			itemView.description.visibility = View.VISIBLE
		} else itemView.description.visibility = View.GONE

		itemView.type.text = autplan.workType.capitalize()

		if (!autplan.saga.workType.isEmpty() && autplan.saga.workId != "0") {
			val sagaName = buildString {
				if (!autplan.saga.workType.isEmpty()) {
					append(autplan.saga.workType.capitalize())
					append(" ")
				}
				append("[work=${autplan.saga.workId}]")
				append(autplan.saga.rusname ?: autplan.saga.name)
				append("[/work]")
			}
			itemView.saga.html = sagaName
			itemView.saga.visibility = View.VISIBLE
		} else {
			itemView.saga.visibility = View.GONE
		}

		if (!InputHelper.isEmpty(autplan.year)) {
			itemView.date.text = autplan.year
			itemView.date.visibility = View.VISIBLE
		} else {
			itemView.date.visibility = View.GONE
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