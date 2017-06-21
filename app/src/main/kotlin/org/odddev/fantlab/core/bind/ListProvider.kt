package org.odddev.fantlab.core.bind

import android.support.v7.widget.RecyclerView
import java.util.*

class ListProvider : BaseProvider() {

	private val entries = ArrayList<Any?>()
	private val vhFactories = ArrayList<VHFactory<*>>()
	private val vhBinders = ArrayList<VHBinder<out RecyclerView.ViewHolder, out Any?>>()

	override fun getVHFactory(position: Int) = vhFactories[position]

	override fun bindVH(holder: RecyclerView.ViewHolder, position: Int) {
		val entry = entries[position]
		val binder = vhBinders[position]
		//binder.onBindViewHolder(holder, position, entry)
	}

	override fun getEntry(position: Int) = entries[position]

	override fun getEntryCount() = entries.size

	fun <VH : RecyclerView.ViewHolder, T> setItems(items: Collection<T>, vhFactory: VHFactory<VH>,
		vhBinder: VHBinder<VH, T>) {

		clearItems()

		entries.addAll(items as Collection<Any?>)
		vhFactories.ensureCapacity(items.size)
		vhBinders.ensureCapacity(items.size)
		var i = 0
		val size = items.size
		while (i < size) {
			vhFactories.add(vhFactory)
			vhBinders.add(vhBinder)
			i++
		}
	}

	fun <VH : RecyclerView.ViewHolder, T> addItems(items: List<T>,
		vhFactory: VHFactory<VH>, vhBinder: VHBinder<VH, T>) {
		var i = 0
		val size = items.size
		while (i < size) {
			val item = items[i]
			entries.add(item)
			vhFactories.add(vhFactory)
			vhBinders.add(vhBinder)
			i++
		}
	}

	fun clearItems() {
		entries.clear()
		vhFactories.clear()
		vhBinders.clear()
	}
}