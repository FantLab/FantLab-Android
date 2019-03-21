package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class WorkAnalog(
		@SerializedName("creators") val creators: Creators,
		@SerializedName("description") val description: String,
		@SerializedName("description_author") val descriptionAuthor: String,
		@SerializedName("id") val id: Int,
		@SerializedName("image") val image: String?,
		@SerializedName("image_preview") val imagePreview: String?,
		@SerializedName("name") val name: String,
		@SerializedName("name_orig") val nameOrig: String,
		@SerializedName("name_type") val nameType: String,
		@SerializedName("name_type_id") val nameTypeId: Int,
		@SerializedName("name_type_icon") val nameTypeIcon: String,
		@SerializedName("published") val published: Int,
		@SerializedName("saga") val saga: Saga?,
		@SerializedName("stat") val stat: Stat,
		@SerializedName("title") val title: String,
		@SerializedName("type") val type: String,
		@SerializedName("url") val url: String,
		@SerializedName("year") val year: String?
) : Parcelable {
	@Keep
	@Parcelize
	data class Creators(
			@SerializedName("authors") val authors: List<Author>
	) : Parcelable {
		@Parcelize
		data class Author(
				@SerializedName("id") val id: Int,
				@SerializedName("is_opened") val isOpened: Int,
				@SerializedName("name") val name: String,
				@SerializedName("name_orig") val nameOrig: String,
				@SerializedName("type") val type: String
		) : Parcelable
	}
	@Keep
	@Parcelize
	data class Saga(
			@SerializedName("id") val id: Int,
			@SerializedName("name") val name: String,
			@SerializedName("name_type") val nameType: String,
			@SerializedName("type") val type: String?
	) : Parcelable
	@Keep
	@Parcelize
	data class Stat(
			@SerializedName("rating") val rating: String,
			@SerializedName("responses") val responses: String,
			@SerializedName("voters") val voters: String
	) : Parcelable
}