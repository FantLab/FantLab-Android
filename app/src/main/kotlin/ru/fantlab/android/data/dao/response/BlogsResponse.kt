package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.Blogs
import ru.fantlab.android.provider.rest.DataManager

data class BlogsResponse(
		val blogs: Pageable<Blogs.Blog>
) {
	class Deserializer : ResponseDeserializable<BlogsResponse> {

		private val blogs: ArrayList<Blogs.Blog> = arrayListOf()

		override fun deserialize(content: String): BlogsResponse {
			val blogsBlock = DataManager.gson.fromJson(content, Blogs::class.java)

			val array = blogsBlock.blogs
			array.map {
				blogs.add(it)
			}

			val pagesBlock = blogsBlock.pages
			val totalCount = pagesBlock.count

			val blogs = Pageable(totalCount, pagesBlock.current, blogs)

			return BlogsResponse(blogs)
		}
	}
}