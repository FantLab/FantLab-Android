package ru.fantlab.android.provider.storage.smiles

import org.json.JSONArray
import org.json.JSONObject
import ru.fantlab.android.data.dao.model.Smile
import java.io.InputStream
import java.util.*


internal object SmileLoader {

	fun loadSmiles(stream: InputStream): List<Smile> {
		try {
			val smilesJSON = JSONArray(inputStreamToString(stream))
			val smiles = ArrayList<Smile>(smilesJSON.length())
			for (i in 0 until smilesJSON.length()) {
				val smile = buildSmileFromJSON(smilesJSON.getJSONObject(i))

				if (smile != null) {
					smiles.add(smile)
				}
			}
			return smiles
		} catch (e: Exception) {
			e.printStackTrace()
		}

		return Collections.emptyList()
	}

	private fun inputStreamToString(stream: InputStream): String {
		return stream.bufferedReader().use { it.readText() }
	}

	private fun buildSmileFromJSON(json: JSONObject): Smile? {
		var description = ""
		var id = ""
		if (json.has("id")) {
			id = json.getString("id")
		}
		if (json.has("description")) {
			description = json.getString("description")
		}
		return Smile(id, description)
	}

}