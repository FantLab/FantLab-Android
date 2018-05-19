package ru.fantlab.android.data.dao.newmodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Nomination(
		@SerializedName("award_icon") val awardIcon: String,
		@SerializedName("award_id") val awardId: Int,
		@SerializedName("award_in_list") val awardInList: Int,
		@SerializedName("award_is_opened") val isAwardOpened: Int,
		@SerializedName("award_name") val awardName: String,
		@SerializedName("award_rusname") val awardRusName: String,
		@SerializedName("contest_id") val contestId: Int,
		@SerializedName("contest_name") val contestName: String,
		@SerializedName("contest_year") val contestYear: Int,
		@SerializedName("cw_id") val id: Int,
		@SerializedName("cw_is_winner") val isWinner: Int,
		@SerializedName("cw_postfix") val postfix: String?,
		@SerializedName("cw_prefix") val prefix: String?,
		@SerializedName("nomination_id") val nominationId: Int,
		@SerializedName("nomination_name") val nominationName: String,
		@SerializedName("nomination_rusname") val nominationRusName: String
) : Parcelable