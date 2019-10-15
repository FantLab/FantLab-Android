package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class ContextMenus(
		val title: String,
		val items: ArrayList<MenuItem>,
		val parent: String
) : Parcelable {
	@Keep
	@Parcelize
	data class MenuItem(
			val title: String,
			val icon: Int?,
			val id: String,
			val logged: Boolean = false,
			val selected: Boolean = false
	) : Parcelable
}