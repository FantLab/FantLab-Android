package ru.fantlab.android.data.dao.model
import com.google.gson.annotations.SerializedName

data class Forums(
    @SerializedName("forumBlocks")
    val forumBlocks: ArrayList<ForumBlock>
) {
    data class ForumBlock(
        @SerializedName("id")
        val id: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("forums")
        val forums: List<Forum>
    ) {
        data class Forum(
            @SerializedName("id")
            val id: Int,
            @SerializedName("title")
            val title: String,
            @SerializedName("forumDescription")
            val forumDescription: String,
            @SerializedName("moderators")
            val moderators: List<Moderator>,
            @SerializedName("stats")
            val stats: Stats,
            @SerializedName("lastMessage")
            val lastMessage: LastMessage
        ) {
            data class Stats(
                @SerializedName("topicCount")
                val topicCount: Int,
                @SerializedName("messageCount")
                val messageCount: Int
            )

            data class Moderator(
                @SerializedName("id")
                val id: Int,
                @SerializedName("login")
                val login: String,
                @SerializedName("gender")
                val gender: String,
                @SerializedName("avatar")
                val avatar: String
            )

            data class LastMessage(
                @SerializedName("id")
                val id: Int,
                @SerializedName("topic")
                val topic: Topic,
                @SerializedName("user")
                val user: User,
                @SerializedName("text")
                val text: String,
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

                data class Topic(
                    @SerializedName("id")
                    val id: Int,
                    @SerializedName("title")
                    val title: String
                )
            }
        }
    }
}