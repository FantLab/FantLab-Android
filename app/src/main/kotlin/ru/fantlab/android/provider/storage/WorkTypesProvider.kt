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
import timber.log.Timber
import java.io.File
import java.io.InputStream

object WorkTypesProvider {

	private const val FILENAME = "worktypes.json"
	private var WORKTYPES: List<WorkType.WorkTypeItem> = listOf()

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
		return Single.just(if (file.exists() && file.length() > 0) (inputStreamToString(file.inputStream())) else loadWorkTypesAssets())
	}

	private fun saveWorkTypesLocal(workTypes: String) = File(App.instance.filesDir.toString() + FILENAME).bufferedWriter().use { out -> out.write(workTypes) }

	private fun deserializeWorkTypes(workTypesData: String): Disposable =
			Observable.just(WorkTypesResponse.Deserializer().deserialize(workTypesData)).observe()
					.subscribe({
						if (it.types.isEmpty()) {
							loadWorkTypesLocal()
						} else {
							setWorkTypes(it.types)
							saveWorkTypesLocal(workTypesData)
						}
					}, { Timber.d(it) })

	private fun inputStreamToString(stream: InputStream): String = stream.bufferedReader().use { it.readText() }

	fun getWorkTypes(): List<WorkType.WorkTypeItem> = WORKTYPES

	fun getCoverByTypeId(typeId: Int) = WORKTYPES.find { it.id == typeId }?.image ?: ""

	fun getCoverByTypeName(typeName: String, isRussian: Boolean = true) = WORKTYPES.find { it.title.rus == typeName }?.image ?: ""

	private fun setWorkTypes(workTypeItems: ArrayList<WorkType.WorkTypeItem>) {
		WORKTYPES = workTypeItems
	}
}
