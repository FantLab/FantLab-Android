package org.odddev.fantlab.award

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.odddev.fantlab.databinding.AwardItemBinding
import java.util.*

class AwardsAdapter : RecyclerView.Adapter<AwardsAdapter.AwardViewHolder>() {

	private var awards: List<Award> = ArrayList()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
			AwardViewHolder(AwardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

	override fun onBindViewHolder(holder: AwardViewHolder, position: Int) {
		holder.binding.award = awards[position]
	}

	override fun getItemCount() = awards.size

	internal fun setAwards(awards: List<Award>) {
		this.awards = awards
		notifyDataSetChanged()
	}

	inner class AwardViewHolder(var binding: AwardItemBinding) : RecyclerView.ViewHolder(binding.root)
}
