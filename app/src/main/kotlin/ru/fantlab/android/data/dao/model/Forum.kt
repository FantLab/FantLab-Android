package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import proguard.annotation.Keep

@Keep
@Parcelize
data class Forum(
		@SerializedName("topics")
		val topics: List<Topic>,
		@SerializedName("pages")
		val pages: Pages
) : Parcelable {
	@Parcelize
	data class Topic(
			@SerializedName("id")
			val id: Int,
			@SerializedName("title")
			val title: String,
			@SerializedName("topicType")
			val topicType: String,
			@SerializedName("creation")
			val creation: Creation,
			@SerializedName("isClosed")
			val isClosed: Boolean?,
			@SerializedName("isPinned")
			val isPinned: Boolean?,
			@SerializedName("stats")
			val stats: Stats,
			@SerializedName("lastMessage")
			val lastMessage: LastMessage
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
					val avatar: String
			) : Parcelable
		}

		@Parcelize
		data class LastMessage(
				@SerializedName("id")
				val id: Int,
				@SerializedName("user")
				val user: User,
				@SerializedName("text")
				val text: String,
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
					val avatar: String
			) : Parcelable
		}

		@Parcelize
		data class Stats(
				@SerializedName("messageCount")
				val messageCount: Int,
				@SerializedName("viewCount")
				val viewCount: Int
		) : Parcelable
	}

	@Parcelize
	data class Pages(
			@SerializedName("current")
			val current: Int,
			@SerializedName("count")
			val count: Int
	) : Parcelable
}