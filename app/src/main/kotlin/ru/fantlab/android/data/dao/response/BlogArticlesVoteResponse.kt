package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import ru.fantlab.android.data.dao.model.BlogArticle
import ru.fantlab.android.provider.rest.DataManager

data class BlogArticlesVoteResponse(
		val likesCount: String
) {
	class Deserializer : ResponseDeserializable<BlogArticlesVoteResponse> {

		override fun deserialize(content: String): BlogArticlesVoteResponse {
			val article = DataManager.gson.fromJson(content, BlogArticle.Article.Stats::class.java)
			return BlogArticlesVoteResponse(article.likeCount ?: "0")
		}
	}
}