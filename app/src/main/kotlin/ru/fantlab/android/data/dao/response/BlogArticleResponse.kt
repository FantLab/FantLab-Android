package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import ru.fantlab.android.data.dao.model.BlogArticle
import ru.fantlab.android.provider.rest.DataManager

data class BlogArticleResponse(
		val article: BlogArticle
) {
	class Deserializer : ResponseDeserializable<BlogArticleResponse> {

		override fun deserialize(content: String): BlogArticleResponse {
			val article = DataManager.gson.fromJson(content, BlogArticle::class.java)
			return BlogArticleResponse(article)
		}
	}
}