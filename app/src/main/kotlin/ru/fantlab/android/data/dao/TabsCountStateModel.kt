package ru.fantlab.android.data.dao

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.DrawableRes

data class TabsCountStateModel(
		var count: Int = 0,
		var tabIndex: Int = 0,
		@DrawableRes val drawableId: Int = 0
): Parcelable {

	constructor(parcel: Parcel) : this(
			parcel.readInt(),
			parcel.readInt(),
			parcel.readInt())

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(count)
		parcel.writeInt(tabIndex)
		parcel.writeInt(drawableId)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<TabsCountStateModel> {
		override fun createFromParcel(parcel: Parcel): TabsCountStateModel {
			return TabsCountStateModel(parcel)
		}

		override fun newArray(size: Int): Array<TabsCountStateModel?> {
			return arrayOfNulls(size)
		}
	}
}