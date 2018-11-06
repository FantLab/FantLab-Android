package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class SearchAuthor(
		@SerializedName("autor_id") val authorId: Int,
		@SerializedName("birthyear") val birthYear: Int,
		val country: String,
		@SerializedName("country_id") val countryId: Int,
		@SerializedName("deathyear") val deathYear: Int,
		@SerializedName("editioncount") val editionCount: Int,
		@SerializedName("is_opened") val isOpened: Int,
		@SerializedName("markcount") val markCount: Int,
		@SerializedName("midmark") val midMark: Int,
		@SerializedName("moviecount") val movieCount: Int,
		val name: String,
		@SerializedName("pseudo_names") val pseudoNames: String,
		@SerializedName("responsecount") val responseCount: Int,
		@SerializedName("rusname") val rusName: String
) : Parcelable