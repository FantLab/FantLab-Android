package ru.fantlab.android.data.dao

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SettingsModel(
		val image: Int,
		val title: String,
		val summary: String,
		val settingsType: Int
) : Parcelable {
	companion object SettingType {
		const val THEME: Int = 0
		const val LANGUAGE: Int = 1
	}
}

