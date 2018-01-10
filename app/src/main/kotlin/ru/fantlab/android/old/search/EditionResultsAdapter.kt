package ru.fantlab.android.old.search

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import ru.fantlab.android.R
import ru.fantlab.android.databinding.SearchEditionItemBinding

class EditionResultsAdapter(val handler: ISearchActions?)
	: RecyclerView.Adapter<EditionResultsAdapter.EditionViewHolder>() {

	private var items: ArrayList<EditionSearchResult> = ArrayList()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
			EditionViewHolder(SearchEditionItemBinding.inflate(LayoutInflater.from(parent.context)))

	override fun onBindViewHolder(holder: EditionResultsAdapter.EditionViewHolder, position: Int) {
		val context = holder.binding.root.context
		val edition = items[position]

		holder.binding.edition.setOnClickListener { handler?.onEditionClicked(edition.editionId, edition.name) }

		holder.binding.year.text = edition.year.toString()

		Glide.with(context)
				.load("https://fantlab.ru/images/editions/big/${edition.editionId}")
				.placeholder(ContextCompat.getDrawable(context, R.drawable.not_found))
				.error(ContextCompat.getDrawable(context, R.drawable.not_found))
				.into(holder.binding.cover)
	}

	override fun getItemCount() = items.size

	override fun getItemViewType(position: Int): Int = 0

	internal fun setEditions(editions: List<EditionSearchResult>) {
		items = ArrayList()
		items.addAll(editions)
		notifyDataSetChanged()
	}

	class EditionViewHolder(var binding: SearchEditionItemBinding) : RecyclerView.ViewHolder(binding.root)
}
