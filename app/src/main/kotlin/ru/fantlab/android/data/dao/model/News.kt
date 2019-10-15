package ru.fantlab.android.data.dao.model
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class News(
		@SerializedName("author")
		val author: String,
		@SerializedName("category")
		val category: String,
		@SerializedName("date")
		val date: String,
		@SerializedName("description")
		val description: String,
		@SerializedName("id")
		val id: Int,
		@SerializedName("image")
		val image: String,
		@SerializedName("news_date_iso")
		val newsDateIso: String,
		@SerializedName("news_id")
		val newsId: String,
		@SerializedName("news_text")
		val newsText: String,
		@SerializedName("title")
		val title: String,
		@SerializedName("type")
		val type: String,
		@SerializedName("url")
		val url: String
) : Parcelable