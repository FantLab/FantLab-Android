package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class AwardInList(
		@SerializedName("award_close") val closed: Int,
		@SerializedName("award_id") val id: Int,
		@SerializedName("award_type") val type: Int,
		@SerializedName("contests_count") val contestsCount: Int,
		@SerializedName("country_id") val countryId: Int,
		@SerializedName("country_name") val countryName: String,
		@SerializedName("homepage") val homepage: String,
		@SerializedName("is_opened") val isOpened: Int,
		@SerializedName("lang_id") val langId: Int?,
		@SerializedName("lang_name") val langName: String,
		@SerializedName("max_date") val maxDate: String,
		@SerializedName("min_date") val minDate: String,
		@SerializedName("name") val nameOrig: String,
		@SerializedName("nomi_count") val nomCount: Int,
		@SerializedName("non_fantastic") val nonFantastic: Int,
		@SerializedName("rusname") val nameRus: String
) : Parcelable