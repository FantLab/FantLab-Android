package ru.fantlab.android.data.dao.newmodel

import com.google.gson.annotations.SerializedName

data class Response(
		@SerializedName("mark") val mark: Int?,
		@SerializedName("response_date") val date: String,
		@SerializedName("response_date_iso") val dateIso: String,
		@SerializedName("response_id") val id: Int,
		@SerializedName("response_text") val text: String,
		@SerializedName("response_votes") val voteCount: Int,
		@SerializedName("user_avatar") val userAvatar: String,
		@SerializedName("user_id") val userId: Int,
		@SerializedName("user_name") val userName: String,
		@SerializedName("user_sex") val userSex: String,
		@SerializedName("work_author") val workAuthor: String,
		@SerializedName("work_author_orig") val workAuthorOrig: String,
		@SerializedName("work_id") val workId: Int,
		@SerializedName("work_image") val workImage: String,
		@SerializedName("work_name") val workName: String,
		@SerializedName("work_name_orig") val workNameOrig: String,
		@SerializedName("work_type") val workType: String,
		@SerializedName("work_type_id") val workTypeId: Int
)