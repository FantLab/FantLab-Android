package ru.fantlab.android.data.dao

import android.os.Parcelable
import android.support.annotation.IntDef
import kotlinx.android.parcel.Parcelize


@Parcelize
data class SettingsModel(
        val image: Int,
        val title: String,
        val summary: String,
        val settingsType: Int
): Parcelable {
    companion object SettingType{
        val THEME: Int = 0
        val LANGUAGE: Int = 1
    }
}

