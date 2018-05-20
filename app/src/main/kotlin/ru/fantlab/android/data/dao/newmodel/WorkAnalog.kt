package ru.fantlab.android.data.dao.newmodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WorkAnalog(
		@SerializedName("altname") val altName: String,
		@SerializedName("analog_pair_id") val analogPairId: Int,
		@SerializedName("autor1_id") val author1Id: Int,
		@SerializedName("autor1_name") val author1Name: String,
		@SerializedName("autor1_rusname") val author1RusName: String,
		@SerializedName("autor2_name") val author2Name: String?,
		@SerializedName("autor2_rusname") val author2RusName: String?,
		@SerializedName("autor3_name") val author3Name: String?,
		@SerializedName("autor3_rusname") val author3RusName: String?,
		@SerializedName("autor4_name") val author4Name: String?,
		@SerializedName("autor4_rusname") val author4RusName: String?,
		@SerializedName("autor5_name") val author5Name: String?,
		@SerializedName("autor5_rusname") val author5RusName: String?,
		@SerializedName("markcount") val voteCount: Int,
		val name: String,
		@SerializedName("name_show_im") val type: String,
		@SerializedName("responses_count") val responseCount: Int,
		@SerializedName("rusname") val rusName: String,
		@SerializedName("work_id") val id: Int,
		@SerializedName("work_markcount") val markCount: Int,
		@SerializedName("work_marks_sum") val marksSum: Int,
		val year: Int?
) : Parcelable