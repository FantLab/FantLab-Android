package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ForumTopic(
		@SerializedName("topic")
		val topic: Topic,
		@SerializedName("forum")
		val forum: Forum,
		@SerializedName("messages")
		val messages: List<Message>
) : Parcelable {
	@Parcelize
	data class Forum(
			@SerializedName("id")
			val id: Int,
			@SerializedName("title")
			val title: String
	) : Parcelable

	@Parcelize
	data class Message(
			@SerializedName("id")
			val id: Int,
			@SerializedName("creation")
			val creation: Creation,
			@SerializedName("text")
			val text: String,
			@SerializedName("is_censored")
			val isCensored: Boolean,
			@SerializedName("is_moder_tag_works")
			val isModerTagWorks: Boolean,
			@SerializedName("stats")
			val stats: Stats?
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
					val id: Int,
					@SerializedName("login")
					val login: String,
					@SerializedName("gender")
					val gender: String,
					@SerializedName("avatar")
					val avatar: String,
					@SerializedName("class")
					val classX: Int,
					@SerializedName("sign")
					val sign: String?
			) : Parcelable
		}

		@Parcelize
		class Stats(
				@SerializedName("rating")
				val rating: Int?
		) : Parcelable
	}

	@Parcelize
	data class Topic(
			@SerializedName("id")
			val id: Int,
			@SerializedName("title")
			val title: String
	) : Parcelable
}