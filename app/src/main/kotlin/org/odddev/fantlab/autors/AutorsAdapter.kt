package org.odddev.fantlab.autors

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.odddev.fantlab.databinding.AutorItemBinding
import java.util.*

/**
 * @author kenrube
 * *
 * @since 07.12.16
 */

class AutorsAdapter : RecyclerView.Adapter<AutorsAdapter.AutorViewHolder>() {

	private var autors: List<Autor> = ArrayList()
	private var autorsCopy: List<Autor> = autors
	private lateinit var listener: Listener

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		AutorViewHolder(AutorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

	override fun onBindViewHolder(holder: AutorViewHolder, position: Int) {
		val autor = autors[position]
		holder.binding.autor = autor
		holder.binding.root.setOnClickListener { listener.onClick(autor.autorId, autor.name) }
	}

	override fun getItemCount() = autors.size

	internal fun setAutors(autors: List<Autor>) {
		this.autors = autors
		autorsCopy = autors
		notifyDataSetChanged()
	}

	internal fun setListener(listener: Listener) {
		this.listener = listener
	}

	fun filter(text: String) {
		autors = ArrayList()
		if (text.isEmpty())
			autors = autorsCopy
		else autorsCopy
				.filter { it.name.contains(text, true) || it.nameOrig.contains(text, true) }
				.forEach { (autors as ArrayList<Autor>).add(it) }
		notifyDataSetChanged()
	}

	inner class AutorViewHolder(var binding: AutorItemBinding) : RecyclerView.ViewHolder(binding.root)

	interface Listener {

		fun onClick(id: Int, name: String)
	}
}
