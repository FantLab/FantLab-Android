package org.odddev.fantlab.authors

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import org.odddev.fantlab.R
import org.odddev.fantlab.core.utils.CircleTransform
import org.odddev.fantlab.databinding.AuthorItemBinding

class AuthorsAdapter(val handler: IAuthorsActions?) : RecyclerView.Adapter<AuthorsAdapter.ViewHolder>() {

	private var authors: List<AuthorInList> = ArrayList()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
			ViewHolder(AuthorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val context = holder.binding.root.context
		val author = authors[position]
		holder.binding.author.setOnClickListener { handler?.onAuthorClicked(author) }
		holder.binding.name.text = author.shortRusName?.replace(",", "")
		holder.binding.nameOrig.text = author.name
		Glide.with(context)
				.load("https://fantlab.ru/images/autors/${author.authorId}")
				.placeholder(ContextCompat.getDrawable(context, R.drawable.not_found))
				.error(ContextCompat.getDrawable(context, R.drawable.not_found))
				.transform(CircleTransform(context))
				.into(holder.binding.avatar)
	}

	override fun getItemCount() = authors.size

	internal fun setAuthors(authors: List<AuthorInList>) {
		this.authors = authors
		notifyDataSetChanged()
	}

	class ViewHolder(var binding: AuthorItemBinding) : RecyclerView.ViewHolder(binding.root)
}
