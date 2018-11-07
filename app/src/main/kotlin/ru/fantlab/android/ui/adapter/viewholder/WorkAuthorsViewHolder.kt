package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.App
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Work
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.modules.author.AuthorPagerActivity
import ru.fantlab.android.ui.widgets.AvatarLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class WorkAuthorsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Work.Author, WorkAuthorsViewHolder, *>)
	: BaseViewHolder<Work.Author>(itemView, adapter) {

	@BindView(R.id.avatarLayout) lateinit var avatarLayout: AvatarLayout
	@BindView(R.id.name) lateinit var name: FontTextView
	@BindView(R.id.name_orig) lateinit var nameOrig: FontTextView

	override fun bind(author: Work.Author) {
		avatarLayout.setUrl("https://${LinkParserHelper.HOST_DATA}/images/autors/${author.id}")
		name.text = author.name
		if (!InputHelper.isEmpty(author.nameOrig))
			nameOrig.text = author.nameOrig
		else
			nameOrig.visibility = View.GONE

		itemView.setOnClickListener {
			AuthorPagerActivity.startActivity(App.instance, author.id, author.name, 0)
		}
	}

	companion object {

		fun newInstance(viewGroup: ViewGroup, adapter: BaseRecyclerAdapter<Work.Author, WorkAuthorsViewHolder, *>) : WorkAuthorsViewHolder
				= WorkAuthorsViewHolder(getView(viewGroup, R.layout.author_row_item), adapter)
	}
}