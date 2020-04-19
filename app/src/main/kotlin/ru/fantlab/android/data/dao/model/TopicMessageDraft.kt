package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopicMessageDraft(
		@SerializedName("messageDraft")
		val messageDraft: MessageDraft
) : Parcelable {
	@Parcelize
	data class MessageDraft(
			@SerializedName("topicId")
			val topicId: String,
			@SerializedName("creation")
			val creation: Creation,
			@SerializedName("text")
			val text: String
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
					val classX: String
			) : Parcelable
		}
	}
}