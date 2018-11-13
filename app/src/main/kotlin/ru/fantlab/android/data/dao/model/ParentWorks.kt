package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class ParentWorks(
		val cycles: ArrayList<ArrayList<ParentWork>>,
		val digest: ArrayList<ArrayList<ParentWork>>
) : Parcelable {
	@Keep
	@Parcelize
	data class ParentWork(
			@SerializedName("work_id") val id: Int?,
			@SerializedName("work_name") val name: String,
			@SerializedName("work_type") val type: String?,
			@SerializedName("work_type_id") val typeId: Int?,
			@SerializedName("work_type_in") val typeIn: String?,
			@SerializedName("work_year") val year: Int?
	) : Parcelable
}