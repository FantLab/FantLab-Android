package ru.fantlab.android.provider.storage

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import ru.fantlab.android.App
import ru.fantlab.android.data.dao.model.WorkType
import ru.fantlab.android.data.dao.response.WorkTypesResponse
import ru.fantlab.android.helper.observe
import ru.fantlab.android.helper.single
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import timber.log.Timber
import java.io.File
import java.io.InputStream

object WorkTypesProvider {

	private const val FILENAME = "worktypes.json"
	private var WORKTYPES: List<WorkType>? = null

	fun init(): Disposable = loadWorkTypesLocal().single()
			.subscribe({
				deserializeWorkTypes(it)
				loadWorkTypesExternal()
			}, { Timber.d(it) }
			)

	private fun loadWorkTypesExternal(): Disposable = DataManager.getWorkTypes().single()
			.subscribe({ deserializeWorkTypes(it) }, { Timber.d(it) })

	private fun loadWorkTypesAssets(): String {
		return inputStreamToString(App.instance.assets.open(FILENAME))
	}

	private fun loadWorkTypesLocal(): Single<String> {
		val file = File(App.instance.filesDir.toString() + FILENAME)
		return Single.just(if (file.exists()) (inputStreamToString(file.inputStream())) else loadWorkTypesAssets())
	}

	private fun saveWorkTypesLocal(workTypes: String) = File(App.instance.filesDir.toString() + FILENAME).bufferedWriter().use { out -> out.write(workTypes) }

	private fun deserializeWorkTypes(workTypesData: String): Disposable =
			Observable.just(WorkTypesResponse.Deserializer().deserialize(workTypesData)).observe()
					.subscribe({
						setWorkTypes(it.types)
						saveWorkTypesLocal(workTypesData)
					}, { Timber.d(it) })

	private fun inputStreamToString(stream: InputStream): String = stream.bufferedReader().use { it.readText() }

	fun getWorkTypes(): List<WorkType>? = WORKTYPES

	fun getCoverByType(type: String) = PROTOCOL_HTTPS + ":" + WORKTYPES?.find { it.workType == type }?.workTypeImage

	fun getCoverByTypeId(typeId: Int) = PROTOCOL_HTTPS + ":" + WORKTYPES?.find { it.workTypeId == typeId }?.workTypeImage

	fun getCoverByTypeName(typeName: String) = PROTOCOL_HTTPS + ":" + WORKTYPES?.find { it.workTypeName == typeName }?.workTypeImage

	private fun setWorkTypes(workTypes: ArrayList<WorkType>) {
		WORKTYPES = workTypes
	}
}
