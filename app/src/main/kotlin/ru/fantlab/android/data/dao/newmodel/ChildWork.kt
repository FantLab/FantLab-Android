package ru.fantlab.android.data.dao.newmodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChildWork(
		val authors: ArrayList<Author>,
		val deep: Int,
		val plus: Int?,
		@SerializedName("publish_status") val publishStatus: String,
		@SerializedName("val midmark_by_weight") val rating: Float?,
		@SerializedName("val_voters") val votersCount: Int?,
		@SerializedName("work_description") val description: String?,
		@SerializedName("work_id") val id: Int?,
		@SerializedName("work_lp") val hasLinguaProfile: Int?,
		@SerializedName("work_name") val name: String,
		@SerializedName("work_name_alt") val nameAlt: String,
		@SerializedName("work_name_bonus") val nameBonus: String?,
		@SerializedName("work_name_orig") val nameOrig: String,
		@SerializedName("work_notes") val notes: String,
		@SerializedName("work_notfinished") val notFinished: Int,
		@SerializedName("work_parent") val parentId: Int,
		@SerializedName("work_preparing") val preparing: Int,
		@SerializedName("work_published") val published: Int,
		@SerializedName("work_root_saga") val rootSagas: ArrayList<WorkRootSaga>,
		@SerializedName("work_type") val type: String?,
		@SerializedName("work_type_id") val typeId: Int,
		@SerializedName("work_type_name") val typeName: String?,
		@SerializedName("work_year") val year: Int?,
		@SerializedName("work_year_of_write") val yearOfWrite: Int?
) : Parcelable {
	@Parcelize
	data class Author(
			val id: Int,
			val name: String,
			val type: String
	) : Parcelable
}