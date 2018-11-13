package ru.fantlab.android.provider.markdown.extension.smiles

import ru.fantlab.android.App
import ru.fantlab.android.data.dao.model.Smile
import java.io.IOException

object SmileManager {

	private const val PATH = "smiles.json"
	private var ALL_SMILES: List<Smile>? = null

	fun load() {
		try {
			val stream = App.instance.assets.open(PATH)
			val smiles = SmileLoader.loadSmiles(stream)
			ALL_SMILES = smiles
			stream.close()
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}

	fun getAll(): List<Smile>? {
		return ALL_SMILES
	}
}