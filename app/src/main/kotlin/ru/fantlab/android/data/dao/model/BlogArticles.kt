package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class BlogArticles(
    @SerializedName("articles")
    val articles: List<Article>,
    @SerializedName("pages")
    val pages: Pages
) : Parcelable {
    @Parcelize
    data class Article(
        @SerializedName("id")
        val id: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("creation")
        val creation: Creation,
        @SerializedName("stats")
        val stats: Stats,
        @SerializedName("tags")
        val tags: String?
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
                val avatar: String?
            ) : Parcelable
        }

        @Parcelize
        data class Stats(
            @SerializedName("likeCount")
            val likeCount: String?,
            @SerializedName("commentCount")
            val commentCount: String?,
            @SerializedName("viewsCount")
            val viewsCount: String?
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