package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class SearchEdition(
		@SerializedName("autors") val authors: String,
		val comment: String,
		val compilers: String,
		val correct: Int,
		@SerializedName("edition_id") val id: Int,
		val isbn1: String,
		val isbn2: String,
		val name: String,
		val notes: String,
		@SerializedName("plan_date") val planDate: String,
		val publisher: String,
		val series: String,
		val year: String
) : Parcelable