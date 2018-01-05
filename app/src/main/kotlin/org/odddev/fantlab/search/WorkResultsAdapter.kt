package org.odddev.fantlab.search

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import org.odddev.fantlab.R
import org.odddev.fantlab.core.utils.CircleTransform
import org.odddev.fantlab.databinding.SearchWorkItemBinding

class WorkResultsAdapter(val handler: ISearchActions?)
	: RecyclerView.Adapter<WorkResultsAdapter.WorkViewHolder>() {

	private var items: ArrayList<WorkSearchResult> = ArrayList()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
			WorkViewHolder(SearchWorkItemBinding.inflate(LayoutInflater.from(parent.context)))

	override fun onBindViewHolder(holder: WorkResultsAdapter.WorkViewHolder, position: Int) {
		val context = holder.binding.root.context
		val work = items[position]

		holder.binding.work.setOnClickListener { handler?.onWorkClicked(work.workId, work.name) }

		holder.binding.author.text = work.allAuthorRusName

		val name = StringBuilder()
		if (work.rusName.isNotEmpty()) {
			name.append(work.rusName)
		}
		if (work.name.isNotEmpty()) {
			if (name.isNotEmpty()) {
				name.append(" / ")
			}
			name.append(work.name)
		}
		holder.binding.name.text = name.toString()

		holder.binding.altNames.text = work.altName
		holder.binding.altNames.visibility =
				if (holder.binding.altNames.text.isNotEmpty()) View.VISIBLE
				else View.GONE

		val info = StringBuilder()
		// todo пока сравниваем с 0 - он отдается в поиске вместо null (https://github.com/kenrube/Fantlab-API/issues/16)
		if (work.year != 0) {
			info.append(work.year)
		}
		if (work.workType.isNotEmpty()) {
			if (info.isNotEmpty()) {
				info.append(", ")
			}
			info.append(work.workType)
		}
		holder.binding.info.text = info.toString()
		holder.binding.info.visibility =
				if (holder.binding.info.text.isNotEmpty()) View.VISIBLE
				else View.GONE

		Glide.with(context)
				.load("https://fantlab.ru/images/editions/big/${work.coverEditionId}")
				.placeholder(ContextCompat.getDrawable(context, R.drawable.not_found))
				.error(ContextCompat.getDrawable(context, R.drawable.not_found))
				.transform(CircleTransform(context))
				.into(holder.binding.cover)
	}

	override fun getItemCount() = items.size

	override fun getItemViewType(position: Int): Int = 0

	internal fun setWorks(works: List<WorkSearchResult>) {
		items = ArrayList()
		items.addAll(works)
		notifyDataSetChanged()
	}

	class WorkViewHolder(var binding: SearchWorkItemBinding) : RecyclerView.ViewHolder(binding.root)
}
