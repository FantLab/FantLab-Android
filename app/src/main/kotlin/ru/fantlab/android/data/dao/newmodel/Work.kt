package ru.fantlab.android.data.dao.newmodel

import com.google.gson.annotations.SerializedName

data class Work(
		val authors: ArrayList<Author>,
		val image: String?,
		@SerializedName("image_preview") val preview: String,
		@SerializedName("publish_statuses") val publishStatuses: ArrayList<String>,
		val rating: Rating,
		val title: String,
		@SerializedName("val_responsecount") val responseCount: Int,
		@SerializedName("work_description") val description: String?,
		@SerializedName("work_description_author") val descriptionAuthor: String?,
		@SerializedName("work_id") val id: Int,
		@SerializedName("work_lp") val hasLinguaProfile: Int?,
		@SerializedName("work_name") val name: String,
		@SerializedName("work_name_alts") val nameAlts: ArrayList<String>,
		@SerializedName("work_name_bonus") val nameBonus: String?,
		@SerializedName("work_name_orig") val nameOrig: String,
		@SerializedName("work_notes") val notes: String,
		@SerializedName("work_notfinished") val notFinished: Int,
		@SerializedName("work_parent") val parentId: Int,
		@SerializedName("work_preparing") val preparing: Int,
		@SerializedName("work_published") val published: Int,
		@SerializedName("work_type") val type: String,
		@SerializedName("work_type_id") val typeId: Int,
		@SerializedName("work_type_name") val typeName: String,
		@SerializedName("work_year") val year: Int?,
		@SerializedName("work_year_of_write") val yearOfWrite: Int?
) {
	data class Author(
			val id: Int,
			@SerializedName("is_opened") val isOpened: Int,
			val name: String,
			@SerializedName("name_orig") val nameOrig: String,
			val type: String
	)

	data class Rating(
			val rating: Float,
			@SerializedName("voters") val votersCount: Int
	)
}