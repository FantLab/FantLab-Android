package ru.fantlab.android.provider.rest

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.rx.rx_object
import ru.fantlab.android.data.dao.model.Edition

object DataManager {

	fun getEdition(
			id: Int,
			showContent: Boolean,
			showAdditionalImages: Boolean
	) = "/edition/$id".toAbsolutePath()
			.httpGet(listOf(
					"content" to showContent.toInt(),
					"images_plus" to showAdditionalImages.toInt()
			))
			.rx_object(Edition.Deserializer())
}

fun String.toAbsolutePath() = "https://api.fantlab.ru$this"

fun Boolean.toInt(): Int = if (this) 1 else 0