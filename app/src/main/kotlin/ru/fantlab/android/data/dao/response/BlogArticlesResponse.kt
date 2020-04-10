package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.BlogArticles
import ru.fantlab.android.data.dao.model.Blogs
import ru.fantlab.android.provider.rest.DataManager

data class BlogArticlesResponse(
		val articles: Pageable<BlogArticles.Article>
) {
	class Deserializer : ResponseDeserializable<BlogArticlesResponse> {

		private val articles: ArrayList<BlogArticles.Article> = arrayListOf()

		override fun deserialize(content: String): BlogArticlesResponse {
			val articlesBlock = DataManager.gson.fromJson(content, BlogArticles::class.java)

			val array = articlesBlock.articles
			array.map {
				articles.add(it)
			}

			val pagesBlock = articlesBlock.pages
			val totalCount = pagesBlock.count

			val blogs = Pageable(totalCount.toInt(), pagesBlock.current.toInt(), articles)

			return BlogArticlesResponse(blogs)
		}
	}
}