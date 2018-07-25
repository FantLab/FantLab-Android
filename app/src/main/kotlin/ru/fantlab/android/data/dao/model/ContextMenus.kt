package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContextMenus(
		val title: String,
		val items: ArrayList<MenuItem>,
		val parent: String
) : Parcelable {
	@Parcelize
	data class MenuItem(
			val title: String,
			val icon: Int,
			val id: String,
			val logged: Boolean = false
	) : Parcelable
}