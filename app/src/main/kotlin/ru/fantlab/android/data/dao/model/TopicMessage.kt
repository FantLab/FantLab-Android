package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopicMessage(
		@SerializedName("message")
		val message: ForumTopic.Message
) : Parcelable