package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.autplans_row_item.*
import ru.fantlab.android.App
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Autplans
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DATA
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.ui.modules.author.AuthorPagerActivity
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class AutplansViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Autplans.Object, AutplansViewHolder>)
	: BaseViewHolder<Autplans.Object>(itemView, adapter), LayoutContainer {

	override val containerView: View?
		get() = itemView

	override fun bind(autplan: Autplans.Object) {
		coverLayout.setUrl(Uri.Builder().scheme(PROTOCOL_HTTPS)
				.authority(HOST_DATA)
				.appendPath("images")
				.appendPath("autors")
				.appendPath("small")
				.appendPath(autplan.autors.autorId)
				.toString())

		author.text = autplan.autors.autorRusname
		author.setOnClickListener {
			AuthorPagerActivity.startActivity(
					App.instance.applicationContext,
					autplan.autors.autorId.toInt(),
					author.text.toString(),
					0
			)
		}

		name.text = if (autplan.rusname.isNotEmpty()) autplan.rusname else autplan.name

		if (autplan.description.isNotEmpty()) {
			description.html = autplan.description
			description.isVisible = true
		} else {
			description.isVisible = false
		}

		type.text = autplan.workType.capitalize()

		if (!autplan.saga.workType.isNullOrEmpty() || autplan.saga.workId != "0") {
			val sagaName = buildString {
				if (!autplan.saga.workType.isNullOrEmpty()) {
					append(autplan.saga.workType.capitalize())
					append(" ")
				}
				append("[work=${autplan.saga.workId}]")
				append(autplan.saga.rusname ?: autplan.saga.name)
				append("[/work]")
			}
			saga.html = sagaName
			saga.isVisible = true
		} else {
			saga.isVisible = false
		}

		if (!autplan.year.isNullOrEmpty()) {
			date.text = autplan.year
			date.isVisible = true
		} else {
			date.isVisible = false
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