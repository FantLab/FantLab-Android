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
		@SerializedName("pinnedMessage")
		val pinnedMessage: PinnedMessage,
		@SerializedName("messages")
		val messages: List<Message>,
		@SerializedName("pages")
		val pages: Pages
) : Parcelable {
	@Parcelize
	data class Topic(
			@SerializedName("id")
			val id: String,
			@SerializedName("title")
			val title: String
	) : Parcelable

	@Parcelize
	data class Forum(
			@SerializedName("id")
			val id: String,
			@SerializedName("title")
			val title: String
	) : Parcelable

	@Parcelize
	data class PinnedMessage(
			@SerializedName("id")
			val id: String,
			@SerializedName("creation")
			val creation: Creation,
			@SerializedName("text")
			val text: String,
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
					val avatar: String,
					@SerializedName("class")
					val classX: String,
					@SerializedName("sign")
					val sign: String
			) : Parcelable
		}

		@Parcelize
		class Stats(
		) : Parcelable
	}

	@Parcelize
	data class Message(
			@SerializedName("id")
			val id: String,
			@SerializedName("creation")
			val creation: Creation,
			@SerializedName("text")
			var text: String,
			@SerializedName("stats")
			val stats: Stats,
			@SerializedName("IsCensored")
			val isCensored: Boolean?
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
					val avatar: String,
					@SerializedName("class")
					val classX: String,
					@SerializedName("sign")
					val sign: String?
			) : Parcelable
		}

		@Parcelize
		class Stats(
		) : Parcelable
	}

	@Parcelize
	data class Pages(
			@SerializedName("current")
			val current: String,
			@SerializedName("count")
			val count: String
	) : Parcelable
}