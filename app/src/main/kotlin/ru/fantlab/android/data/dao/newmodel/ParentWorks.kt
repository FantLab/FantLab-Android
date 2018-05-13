package ru.fantlab.android.data.dao.newmodel

import com.google.gson.annotations.SerializedName

data class ParentWorks(
		val cycles: ArrayList<ArrayList<ParentWork>>,
		val digest: ArrayList<ArrayList<ParentWork>>
) {
	data class ParentWork(
		@SerializedName("work_id") val id: Int?,
		@SerializedName("work_name") val name: String,
		@SerializedName("work_type") val type: String?,
		@SerializedName("work_type_id") val typeId: Int?,
		@SerializedName("work_type_in") val typeIn: String?,
		@SerializedName("work_year") val year: Int?
	)
}