package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class BlogArticle(
		@SerializedName("article")
		val article: Article
) : Parcelable {
	@Parcelize
	data class Article(
			@SerializedName("id")
			val id: String,
			@SerializedName("title")
			val title: String,
			@SerializedName("creation")
			val creation: Creation,
			@SerializedName("text")
			val text: String,
			@SerializedName("tags")
			val tags: String?,
			@SerializedName("stats")
			val stats: Stats
	) : Parcelable {
		@Parcelize
		data class Creation(
				@SerializedName("user")
				val user: User,
				@SerializedName("date")
				val date: String
		) : Parcelable {
			@Parcelize
			data class User(
					@SerializedName("id")
					val id: String,
					@SerializedName("login")
					val login: String,
					@SerializedName("gender")
					val gender: String,
					@SerializedName("avatar")
					val avatar: String
			) : Parcelable
		}

		@Parcelize
		data class Stats(
				@SerializedName("commentCount")
				val commentCount: String?,
				@SerializedName("likeCount")
				val likeCount: String?,
				@SerializedName("viewCount")
				val viewCount: String?
		) : Parcelable
	}
}