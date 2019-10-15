package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class MarkMini(
		@SerializedName("mark") val mark: Int,
		@SerializedName("user_id") val user_id: Int,
		@SerializedName("user_work_response_id") val user_work_response_id: Int,
		@SerializedName("user_work_classif_flag") val user_work_classif_flag: Int,
		@SerializedName("work_id") val work_id: Int?,
		@SerializedName("work_type_id") val work_type_id: Int
) : Parcelable