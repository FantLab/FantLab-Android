package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class BlogAnons(
		@SerializedName("blog_id") val blogId: Int,
		@SerializedName("subscribers_count") val subscribersCount: Int,
		val topics: ArrayList<BlogTopic>
) : Parcelable {
	@Keep
	@Parcelize
	data class BlogTopic(
			@SerializedName("date_of_add") val addDate: String,
			@SerializedName("head_topic") val title: String,
			@SerializedName("is_opened") val isOpened: Int,
			@SerializedName("topic_id") val id: Int
	) : Parcelable
}