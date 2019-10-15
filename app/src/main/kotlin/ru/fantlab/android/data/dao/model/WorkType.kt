package ru.fantlab.android.data.dao.model
import com.google.gson.annotations.SerializedName


data class WorkType(
    @SerializedName("work_type_id")
    val workTypeId: Int,
    @SerializedName("work_type")
    val workType: String,
    @SerializedName("work_type_name")
    val workTypeName: String,
    @SerializedName("work_type_icon")
    val workTypeIcon: String,
    @SerializedName("work_type_image")
    val workTypeImage: String
)