package ru.fantlab.android.data.dao.model
import com.google.gson.annotations.SerializedName


data class Blogs(
    @SerializedName("blogs")
    val blogs: List<Blog>,
    @SerializedName("pages")
    val pages: Pages
) {
    data class Pages(
        @SerializedName("current")
        val current: Int,
        @SerializedName("count")
        val count: Int
    )

    data class Blog(
        @SerializedName("id")
        val id: Int,
        @SerializedName("user")
        val user: User,
        @SerializedName("stats")
        val stats: Stats,
        @SerializedName("last_article")
        val lastArticle: LastArticle
    ) {
        data class LastArticle(
            @SerializedName("id")
            val id: Int,
            @SerializedName("title")
            val title: String?,
            @SerializedName("date")
            val date: String
        )

        data class Stats(
            @SerializedName("article_count")
            val articleCount: Int,
            @SerializedName("subscriber_count")
            val subscriberCount: Int
        )

        data class User(
            @SerializedName("id")
            val id: Int,
            @SerializedName("login")
            val login: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("gender")
            val gender: String,
            @SerializedName("avatar")
            val avatar: String
        )
    }
}