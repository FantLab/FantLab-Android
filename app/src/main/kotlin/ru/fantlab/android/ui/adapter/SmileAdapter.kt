package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import ru.fantlab.android.data.dao.model.Smile
import ru.fantlab.android.ui.adapter.viewholder.SmileViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class SmileAdapter(listener: BaseViewHolder.OnItemClickListener<Smile>)
	: BaseRecyclerAdapter<Smile, SmileViewHolder, BaseViewHolder.OnItemClickListener<Smile>>(listener), Filterable {

	var copiedList = mutableListOf<Smile>()

	override fun viewHolder(parent: ViewGroup, viewType: Int): SmileViewHolder {
		return SmileViewHolder.newInstance(parent, this)
	}

	override fun onBindView(holder: SmileViewHolder, position: Int) {
		holder.bind(data[position])
	}

	override fun getFilter(): Filter {
		return object : Filter() {
			override fun performFiltering(constraint: CharSequence): Filter.FilterResults {
				if (copiedList.isEmpty()) {
					copiedList.addAll(data)
				}
				val text = constraint.toString().toLowerCase()
				val filteredResults: List<Smile> = if (text.isNotBlank()) {
					val data = data.filter {
						text in it.description || it.description.contains(text)
					}
					if (data.isNotEmpty()) data
					else copiedList
				} else {
					copiedList
				}
				val results = FilterResults()
				results.values = filteredResults
				results.count = filteredResults.size
				return results
			}

			@Suppress("UNCHECKED_CAST")
			override fun publishResults(var1: CharSequence, results: Filter.FilterResults) {
				results.values?.let {
					insertItems(it as List<Smile>)
				}
			}
		}
	}
}