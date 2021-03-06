package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Response(
		val mark: Int?,
		@SerializedName("response_date") val date: String,
		@SerializedName("response_date_iso") val dateIso: String,
		@SerializedName("response_id") val id: Int,
		@SerializedName("response_text") var text: String,
		@SerializedName("response_votes") var voteCount: Int,
		@SerializedName("user_avatar") val userAvatar: String,
		@SerializedName("user_id") val userId: Int,
		@SerializedName("user_name") val userName: String,
		@SerializedName("user_sex") val userSex: String,
		@SerializedName("work_author") val workAuthor: String,
		@SerializedName("work_author_orig") val workAuthorOrig: String,
		@SerializedName("work_id") val workId: Int,
		@SerializedName("work_image") val workImage: String?,
		@SerializedName("work_name") val workName: String,
		@SerializedName("work_name_orig") val workNameOrig: String,
		@SerializedName("work_type") val workType: String,
		@SerializedName("work_type_id") val workTypeId: Int
) : Parcelable