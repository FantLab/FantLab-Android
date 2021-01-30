package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class SearchWork(
        @SerializedName("all_autor_name")
        val allAutorName: String = "",
        @SerializedName("all_autor_rusname")
        val allAutorRusname: String = "",
        @SerializedName("altname")
        val altname: String = "",
        @SerializedName("autor1_id")
        val autor1Id: Int = 0,
        @SerializedName("autor1_is_opened")
        val autor1IsOpened: Int = 0,
        @SerializedName("autor1_rusname")
        val autor1Rusname: String = "",
        @SerializedName("autor2_id")
        val autor2Id: Int = 0,
        @SerializedName("autor2_is_opened")
        val autor2IsOpened: Int = 0,
        @SerializedName("autor2_rusname")
        val autor2Rusname: String = "",
        @SerializedName("autor3_id")
        val autor3Id: Int = 0,
        @SerializedName("autor3_is_opened")
        val autor3IsOpened: Int = 0,
        @SerializedName("autor3_rusname")
        val autor3Rusname: String = "",
        @SerializedName("autor4_id")
        val autor4Id: Int = 0,
        @SerializedName("autor4_is_opened")
        val autor4IsOpened: Int = 0,
        @SerializedName("autor4_rusname")
        val autor4Rusname: String = "",
        @SerializedName("autor5_id")
        val autor5Id: Int = 0,
        @SerializedName("autor5_is_opened")
        val autor5IsOpened: Int = 0,
        @SerializedName("autor5_rusname")
        val autor5Rusname: String = "",
        @SerializedName("autor_id")
        val autorId: Int = 0,
        @SerializedName("autor_is_opened")
        val autorIsOpened: Int = 0,
        @SerializedName("autor_rusname")
        val autorRusname: String = "",
        @SerializedName("doc")
        val doc: Int = 0,
        @SerializedName("fullname")
        val fullname: String = "",
        @SerializedName("keywords")
        val keywords: String = "",
        @SerializedName("level")
        val level: Int = 0,
        @SerializedName("markcount")
        val markcount: Int = 0,
        @SerializedName("midmark")
        val midmark: List<Double> = listOf(),
        @SerializedName("name")
        val name: String = "",
        @SerializedName("name_eng")
        val nameEng: String = "",
        @SerializedName("name_show_im")
        val nameShowIm: String = "",
        @SerializedName("nearest_parent_work_id")
        val nearestParentWorkId: Int = 0,
        @SerializedName("parent_work_id")
        val parentWorkId: Int = 0,
        @SerializedName("parent_work_id_present")
        val parentWorkIdPresent: Int = 0,
        @SerializedName("pic_edition_id")
        val picEditionId: Int = 0,
        @SerializedName("pic_edition_id_auto")
        val picEditionIdAuto: Int = 0,
        @SerializedName("rating")
        val rating: List<Double> = listOf(),
        @SerializedName("rusname")
        val rusname: String = "",
        @SerializedName("weight")
        val weight: Int = 0,
        @SerializedName("work_id")
        val workId: Int = 0,
        @SerializedName("work_type_id")
        val workTypeId: Int = 0,
        @SerializedName("year")
        val year: Int = 0
) : Parcelable