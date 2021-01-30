package ru.fantlab.android.data.dao.model
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class WorkType(
    @SerializedName("work_types")
    val workTypeItems: List<WorkTypeItem> = listOf()
) {
    @Keep
    data class WorkTypeItem(
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("image")
        val image: String = "",
        @SerializedName("title")
        val title: Title = Title()
    ) {
        @Keep
        data class Title(
            @SerializedName("en")
            val en: String = "",
            @SerializedName("rus")
            val rus: String = ""
        )
    }
}
/*
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
)*/