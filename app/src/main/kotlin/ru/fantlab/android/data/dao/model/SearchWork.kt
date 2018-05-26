package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchWork(
		@SerializedName("all_autor_name") val allAuthorName: String,
		@SerializedName("all_autor_rusname") val allAuthorRusName: String,
		@SerializedName("altname") val altName: String,
		@SerializedName("autor1_id") val author1Id: Int,
		@SerializedName("autor1_is_opened") val author1IsOpened: Int,
		@SerializedName("autor1_rusname") val author1RusName: String,
		@SerializedName("autor2_id") val author2Id: Int,
		@SerializedName("autor2_is_opened") val author2IsOpened: Int,
		@SerializedName("autor2_rusname") val author2RusName: String,
		@SerializedName("autor3_id") val author3Id: Int,
		@SerializedName("autor3_is_opened") val author3IsOpened: Int,
		@SerializedName("autor3_rusname") val author3RusName: String,
		@SerializedName("autor4_id") val author4Id: Int,
		@SerializedName("autor4_is_opened") val author4IsOpened: Int,
		@SerializedName("autor4_rusname") val author4RusName: String,
		@SerializedName("autor5_id") val author5Id: Int,
		@SerializedName("autor5_is_opened") val author5IsOpened: Int,
		@SerializedName("autor5_rusname") val author5RusName: String,
		@SerializedName("autor_id") val authorId: Int,
		@SerializedName("autor_is_opened") val authorIsOpened: Int,
		@SerializedName("autor_rusname") val authorRusName: String,
		@SerializedName("fullname") val fullName: String,
		@SerializedName("markcount") val markCount: Int,
		@SerializedName("midmark") val midMark: ArrayList<Float>,
		val name: String,
		@SerializedName("name_eng") val typeName: String,
		@SerializedName("name_show_im") val typeRusName: String,
		@SerializedName("parent_work_id") val parentWorkId: Int,
		@SerializedName("parent_work_id_present") val parentWorkIdPresent: Int,
		@SerializedName("pic_edition_id") val pictureEditionId: String,
		@SerializedName("pic_edition_id_auto") val pictureEditionIdAuto: String,
		@SerializedName("rusname") val rusName: String,
		@SerializedName("work_id") val id: Int,
		val year: Int
) : Parcelable