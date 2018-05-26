package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MarksStatistics(
		@SerializedName("marks_count") val markCount: Int,
		@SerializedName("classif_count") val classificationCount: Int,
		@SerializedName("marks_stats_marks") val marksStats: MarksStats,
		@SerializedName("marks_stats_works") val worksStats: ArrayList<WorksStats>
) : Parcelable {
	@Parcelize
	data class MarksStats(
			@SerializedName("1") val countOf1: Int,
			@SerializedName("2") val countOf2: Int,
			@SerializedName("3") val countOf3: Int,
			@SerializedName("4") val countOf4: Int,
			@SerializedName("5") val countOf5: Int,
			@SerializedName("6") val countOf6: Int,
			@SerializedName("7") val countOf7: Int,
			@SerializedName("8") val countOf8: Int,
			@SerializedName("9") val countOf9: Int,
			@SerializedName("10") val countOf10: Int
	) : Parcelable

	@Parcelize
	data class WorksStats(
			@SerializedName("mark_avg") val markAverage: Float,
			@SerializedName("mark_sum") val markCount: Int,
			@SerializedName("work_type") val workType: String,
			@SerializedName("work_type_id") val workTypeId: Int
	) : Parcelable
}