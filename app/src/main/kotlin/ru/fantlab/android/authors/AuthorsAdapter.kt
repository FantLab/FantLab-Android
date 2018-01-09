package ru.fantlab.android.authors

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import ru.fantlab.android.R
import ru.fantlab.android.databinding.AuthorItemBinding
import ru.fantlab.android.databinding.LetterItemBinding
import ru.fantlab.android.core.utils.CircleTransform

class AuthorsAdapter(val handler: IAuthorsActions?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	private var items: ArrayList<IAuthorItem> = ArrayList()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
			when (viewType) {
				0 -> {
					AuthorViewHolder(AuthorItemBinding.inflate(LayoutInflater.from(parent.context)))
				}
				1 -> {
					LetterViewHolder(LetterItemBinding.inflate(LayoutInflater.from(parent.context)))
				}
				else -> null
			}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (holder is AuthorViewHolder) {
			val context = holder.binding.root.context
			val author = items[position] as AuthorInList
			holder.binding.author.setOnClickListener { handler?.onAuthorClicked(author) }
			holder.binding.name.text = author.shortRusName?.replace(",", "")
			holder.binding.nameOrig.text = author.name
			holder.binding.nameOrig.visibility = if (author.name.isNullOrEmpty()) View.GONE else View.VISIBLE
			Glide.with(context)
					.load("https://fantlab.ru/images/autors/${author.authorId}")
					.placeholder(ContextCompat.getDrawable(context, R.drawable.not_found))
					.error(ContextCompat.getDrawable(context, R.drawable.not_found))
					.transform(CircleTransform(context))
					.into(holder.binding.avatar)
		} else if (holder is LetterViewHolder) {
			val letter = items[position] as LetterItem
			holder.binding.letter.text = letter.letter
		}
	}

	override fun getItemCount() = items.size

	override fun getItemViewType(position: Int): Int = if (items[position] is AuthorInList) 0 else 1

	internal fun setAuthors(authors: List<AuthorInList>) {
		items = ArrayList()

		var letter: Char? = '0'
		authors
				.map {
					if (it.shortRusName?.first() != letter) {
						letter = it.shortRusName?.first()
						items.add(LetterItem(letter?.toString()))
					}
					items.add(it)
				}

		notifyDataSetChanged()
	}

	internal class AuthorViewHolder(var binding: AuthorItemBinding) : RecyclerView.ViewHolder(binding.root)

	internal class LetterViewHolder(var binding: LetterItemBinding) : RecyclerView.ViewHolder(binding.root)
}
