package org.odddev.fantlab.autors

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.odddev.fantlab.R
import org.odddev.fantlab.databinding.AutorItemBinding
import java.util.*

/**
 * @author kenrube
 * *
 * @since 07.12.16
 */

class AutorsAdapter : RecyclerView.Adapter<AutorsAdapter.AutorViewHolder>() {

	private var autors: List<Autor> = ArrayList()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutorViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		return AutorViewHolder(DataBindingUtil.inflate<AutorItemBinding>(inflater, R.layout.autor_item, parent, false))
	}

	override fun onBindViewHolder(holder: AutorViewHolder, position: Int) {
		holder.binding.autor = autors[position]
	}

	override fun getItemCount(): Int {
		return autors.size
	}

	internal fun setAutors(autors: List<Autor>) {
		this.autors = autors
		notifyDataSetChanged()
	}

	inner class AutorViewHolder(var binding: AutorItemBinding) : RecyclerView.ViewHolder(binding.root)
}
