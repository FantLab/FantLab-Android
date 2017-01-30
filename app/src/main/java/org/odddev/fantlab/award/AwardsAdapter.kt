package org.odddev.fantlab.award

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import org.odddev.fantlab.R
import org.odddev.fantlab.databinding.AwardItemBinding

import java.util.ArrayList

/**
 * @author kenrube
 * *
 * @since 07.12.16
 */

class AwardsAdapter : RecyclerView.Adapter<AwardsAdapter.AwardViewHolder>() {

	private var awards: List<Award>? = null

	init {
		awards = ArrayList<Award>()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AwardViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		return AwardViewHolder(DataBindingUtil.inflate<AwardItemBinding>(inflater, R.layout.award_item, parent, false))
	}

	override fun onBindViewHolder(holder: AwardViewHolder, position: Int) {
		holder.binding.award = awards!![position]
	}

	override fun getItemCount(): Int {
		return awards!!.size
	}

	internal fun setAwards(awards: List<Award>) {
		this.awards = awards
		notifyDataSetChanged()
	}

	internal inner class AwardViewHolder(var binding: AwardItemBinding) : RecyclerView.ViewHolder(binding.root)
}
