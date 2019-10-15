package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class MarksStatistics(
		@SerializedName("classif_count") val classificationCount: Int,
		@SerializedName("marks_count") val markCount: Int,
		@SerializedName("marks_stats_marks") val marksStats: ArrayList<MarksStat>,
		@SerializedName("marks_stats_works") val worksStats: ArrayList<WorksStat>
) : Parcelable {
	@Keep
	@Parcelize
	data class MarksStat(
			val mark: Int,
			@SerializedName("mark_count") val markCount: Int
	) : Parcelable

	@Keep
	@Parcelize
	data class WorksStat(
			@SerializedName("mark_avg") val markAverage: Float,
			@SerializedName("mark_count") val markCount: Int,
			@SerializedName("work_type") val workType: String,
			@SerializedName("work_type_id") val workTypeId: Int
	) : Parcelable
}