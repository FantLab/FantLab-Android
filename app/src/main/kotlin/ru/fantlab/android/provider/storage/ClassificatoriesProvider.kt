package ru.fantlab.android.provider.storage

import ru.fantlab.android.App
import ru.fantlab.android.data.dao.response.ClassificatoriesResponse
import java.io.InputStream

internal object ClassificatoriesProvider {

	private val DIR = "class/"

	fun loadClasses(classif: String): ClassificatoriesResponse {
		val stream = App.instance.assets.open("$DIR$classif.json")
		return ClassificatoriesResponse.Deserializer().deserialize(inputStreamToString(stream))
	}

	private fun inputStreamToString(stream: InputStream): String {
		return stream.bufferedReader().use { it.readText() }
	}

}