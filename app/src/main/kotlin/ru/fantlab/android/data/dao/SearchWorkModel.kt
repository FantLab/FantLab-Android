package ru.fantlab.android.data.dao

import com.google.gson.annotations.SerializedName

data class SearchWorkModel(
		@SerializedName("all_autor_name")
		var allAuthorName: String,
		@SerializedName("all_autor_rusname")
		var allAuthorRusName: String,
		@SerializedName("altname")
		var altName: String,
		@SerializedName("autor1_id")
		var author1Id: Int,
		@SerializedName("autor1_is_opened")
		var author1IsOpened: Int,
		@SerializedName("autor1_rusname")
		var author1RusName: String,
		@SerializedName("autor2_id")
		var author2Id: Int,
		@SerializedName("autor2_is_opened")
		var author2IsOpened: Int,
		@SerializedName("autor2_rusname")
		var author2RusName: String,
		@SerializedName("autor3_id")
		var author3Id: Int,
		@SerializedName("autor3_is_opened")
		var author3IsOpened: Int,
		@SerializedName("autor3_rusname")
		var author3RusName: String,
		@SerializedName("autor4_id")
		var author4Id: Int,
		@SerializedName("autor4_is_opened")
		var author4IsOpened: Int,
		@SerializedName("autor4_rusname")
		var author4RusName: String,
		@SerializedName("autor5_id")
		var author5Id: Int,
		@SerializedName("autor5_is_opened")
		var author5IsOpened: Int,
		@SerializedName("autor5_rusname")
		var author5RusName: String,
		@SerializedName("autor_id")
		var authorId: Int,
		@SerializedName("autor_is_opened")
		var authorIsOpened: Int,
		@SerializedName("autor_rusname")
		var authorRusName: String,
		@SerializedName("fullname")
		var fullName: String,
		var keywords: String,
		var level: Int,
		@SerializedName("markcount")
		var markCount: Int,
		@SerializedName("midmark")
		var midMark: ArrayList<Float>,
		var name: String,
		@SerializedName("name_eng")
		var typeName: String,
		@SerializedName("name_show_im")
		var typeRusName: String,
		var parentWorkId: Int,
		var parentWorkIdPresent: Int,
		var picEditionId: String,
		var picEditionIdAuto: String,
		@SerializedName("rusname")
		var rusName: String,
		var workId: Int,
		var year: Int
)