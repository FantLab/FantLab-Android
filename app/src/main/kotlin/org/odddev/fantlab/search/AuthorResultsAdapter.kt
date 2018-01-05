package org.odddev.fantlab.search

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import org.odddev.fantlab.R
import org.odddev.fantlab.core.utils.CircleTransform
import org.odddev.fantlab.databinding.SearchAuthorItemBinding

class AuthorResultsAdapter(val handler: ISearchActions?)
	: RecyclerView.Adapter<AuthorResultsAdapter.AuthorViewHolder>() {

	private var items: ArrayList<AuthorSearchResult> = ArrayList()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
			AuthorViewHolder(SearchAuthorItemBinding.inflate(LayoutInflater.from(parent.context)))

	override fun onBindViewHolder(holder: AuthorResultsAdapter.AuthorViewHolder, position: Int) {
		val context = holder.binding.root.context
		val author = items[position]

		holder.binding.author.setOnClickListener { handler?.onAuthorClicked(author.authorId, author.name) }

		val name = StringBuilder()
		if (author.rusName.isNotEmpty()) {
			name.append(author.rusName)
		}
		if (author.name.isNotEmpty()) {
			if (name.isNotEmpty()) {
				name.append(" / ")
			}
			name.append(author.name)
		}
		holder.binding.name.text = name.toString()

		holder.binding.pseudonyms.text = author.pseudoNames
		holder.binding.pseudonyms.visibility =
				if (holder.binding.pseudonyms.text.isNotEmpty()) View.VISIBLE
				else View.GONE

		val info = StringBuilder()
		if (author.country.isNotEmpty()) {
			info.append(author.country)
		}
		// todo пока сравниваем с 0 - он отдается в поиске вместо null (https://github.com/kenrube/Fantlab-API/issues/16)
		if (author.birthYear != 0) {
			if (info.isNotEmpty()) {
				info.append(", ")
			}
			info.append(author.birthYear)
		}
		if (author.deathYear != 0) {
			if (info.isNotEmpty()) {
				info.append(" - ")
			}
			info.append(author.deathYear)
		}
		holder.binding.info.text = info.toString()
		holder.binding.info.visibility =
				if (holder.binding.info.text.isNotEmpty()) View.VISIBLE
				else View.GONE

		val stat = StringBuilder()
		if (author.responseCount != 0) {
			stat.append("отзывов: ${author.responseCount}")
		}
		if (author.editionCount != 0) {
			if (stat.isNotEmpty()) {
				stat.append(", ")
			}
			stat.append("изданий: ${author.editionCount}")
		}
		if (author.movieCount != 0) {
			if (stat.isNotEmpty()) {
				stat.append(", ")
			}
			stat.append("экранизаций: ${author.movieCount}")
		}
		holder.binding.stat.text = stat.toString()
		holder.binding.stat.visibility =
				if (holder.binding.stat.text.isNotEmpty()) View.VISIBLE
				else View.GONE

		Glide.with(context)
				.load("https://fantlab.ru/images/autors/${author.authorId}")
				.placeholder(ContextCompat.getDrawable(context, R.drawable.not_found))
				.error(ContextCompat.getDrawable(context, R.drawable.not_found))
				.transform(CircleTransform(context))
				.into(holder.binding.avatar)
	}

	override fun getItemCount() = items.size

	override fun getItemViewType(position: Int): Int = 0

	internal fun setAuthors(authors: List<AuthorSearchResult>) {
		items = ArrayList()
		items.addAll(authors)
		notifyDataSetChanged()
	}

	class AuthorViewHolder(var binding: SearchAuthorItemBinding) : RecyclerView.ViewHolder(binding.root)
}
