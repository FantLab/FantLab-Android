package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class User(
		@SerializedName("autor_id") val authorId: Int?,
		@SerializedName("autor_is_opened") val authorIsOpened: Int?,
		@SerializedName("autor_name") val authorName: String?,
		@SerializedName("autor_name_orig") val authorNameOrig: String?,
		val avatar: String,
		@SerializedName("birthday") val birthDay: String?,
		@SerializedName("block") val blocked: Int,
		@SerializedName("blog_id") val blogId: Int?,
		@SerializedName("bookcase_count") val bookcaseCount: Int,
		@SerializedName("city_id") val cityId: Int?,
		@SerializedName("city_name") val cityName: String?,
		@SerializedName("class_name") val className: String,
		@SerializedName("classifcount") val classificationCount: Int,
		@SerializedName("country_id") val countryId: Int?,
		@SerializedName("country_name") val countryName: String?,
		@SerializedName("curator_autors") val curatedAuthorsCount: Int,
		@SerializedName("date_of_block") val blockDate: String?,
		@SerializedName("date_of_block_end") val blockEndDate: String?,
		@SerializedName("date_of_last_action") val lastActionDate: String,
		@SerializedName("date_of_reg") val regDate: String,
		@SerializedName("descriptioncount") val descriptionCount: Int,
		val fio: String,
		val level: Float,
		val location: String?,
		val login: String,
		@SerializedName("markcount") val markCount: Int,
		@SerializedName("messagecount") val forumMessageCount: Int,
		@SerializedName("responsecount") val responseCount: Int,
		val sex: String,
		val sign: String?,
		@SerializedName("tickets_count") val ticketCount: Int,
		@SerializedName("topiccount") val topicCount: Int,
		@SerializedName("user_class") val `class`: Int,
		@SerializedName("user_id") val id: Int,
		@SerializedName("user_timer") val timer: Long,
		@SerializedName("votecount") val voteCount: Int
) : Parcelable