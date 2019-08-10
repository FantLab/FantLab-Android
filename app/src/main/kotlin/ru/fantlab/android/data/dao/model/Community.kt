package ru.fantlab.android.data.dao.model
import com.google.gson.annotations.SerializedName
import proguard.annotation.Keep


@Keep
data class Community(
    @SerializedName("main")
    val main: List<Main>,
    @SerializedName("additional")
    val additional: List<Additional>
) {
    data class Main(
        @SerializedName("id")
        val id: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("community_description")
        val communityDescription: String,
        @SerializedName("avatar")
        val avatar: String,
        @SerializedName("stats")
        val stats: Stats,
        @SerializedName("last_article")
        val lastArticle: LastArticle
    ) {
        data class Stats(
            @SerializedName("article_count")
            val articleCount: Int,
            @SerializedName("subscriber_count")
            val subscriberCount: Int
        )

        data class LastArticle(
            @SerializedName("id")
            val id: Int,
            @SerializedName("title")
            val title: String,
            @SerializedName("user")
            val user: User,
            @SerializedName("date")
            val date: String
        ) {
            data class User(
                @SerializedName("id")
                val id: Int,
                @SerializedName("login")
                val login: String,
                @SerializedName("gender")
                val gender: String,
                @SerializedName("avatar")
                val avatar: String
            )
        }
    }

    data class Additional(
        @SerializedName("id")
        val id: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("community_description")
        val communityDescription: String,
        @SerializedName("avatar")
        val avatar: String,
        @SerializedName("stats")
        val stats: Stats,
        @SerializedName("last_article")
        val lastArticle: LastArticle
    ) {
        data class Stats(
            @SerializedName("article_count")
            val articleCount: Int,
            @SerializedName("subscriber_count")
            val subscriberCount: Int
        )

        data class LastArticle(
            @SerializedName("id")
            val id: Int,
            @SerializedName("title")
            val title: String,
            @SerializedName("user")
            val user: User,
            @SerializedName("date")
            val date: String
        ) {
            data class User(
                @SerializedName("id")
                val id: Int,
                @SerializedName("login")
                val login: String,
                @SerializedName("gender")
                val gender: String,
                @SerializedName("avatar")
                val avatar: String
            )
        }
    }
}