package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.requery.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity @Table(name = "nomination")
data class Nomination(
		@SerializedName("cw_id") @get:Column(name = "contest_year") @get:Key var id: Int,
		@get:Column(name = "award_icon") var awardIcon: String,
		@get:Column(name = "award_id") var awardId: Int,
		@get:Column(name = "award_is_opened") var awardIsOpened: Int,
		@get:Column(name = "award_name") var awardName: String,
		@SerializedName("award_rusname") @get:Column(name = "award_rus_name") var awardRusName: String,
		@get:Column(name = "contest_id") var contestId: Int,
		@get:Column(name = "contest_name") var contestName: String,
		@get:Column(name = "contest_year") var contestYear: Int,
		@SerializedName("cw_is_winner") @get:Column(name = "win") var win: Int,
		@SerializedName("cw_postfix") @get:Column(name = "postfix") var postfix: String,
		@SerializedName("cw_prefix") @get:Column(name = "prefix") var prefix: Int,
		@get:Column(name = "nomination_id") var nominationId: Int,
		@get:Column(name = "nomination_name") var nominationName: String,
		@SerializedName("nomination_rusname") @get:Column(name = "nomination_rus_name") var nominationRusName: String,
		@get:Column(name = "work_id") var workId: Int,
		@get:Column(name = "work_name") var workName: String,
		@SerializedName("work_rusname") @get:Column(name = "work_rus_name") var workRusName: String,
		@get:Column(name = "work_year") var workYear: String,
		@get:Column(name = "author_id") var authorId: Int?
) : Persistable, Parcelable