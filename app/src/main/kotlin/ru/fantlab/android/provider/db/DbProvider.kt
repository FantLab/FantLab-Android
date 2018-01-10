package ru.fantlab.android.provider.db

import android.content.Context
import io.requery.Persistable
import io.requery.android.sqlite.DatabaseSource
import io.requery.reactivex.ReactiveEntityStore
import io.requery.reactivex.ReactiveSupport
import io.requery.sql.EntityDataStore
import io.requery.sql.TableCreationMode
import ru.fantlab.android.BuildConfig
import ru.fantlab.android.data.dao.model.Models

object DbProvider {

	fun initDataStore(context: Context, version: Int): ReactiveEntityStore<Persistable> {
		val model = Models.DEFAULT
		val source = DatabaseSource(context, model, "Fantlab-DB", version)
		val configuration = source.configuration
		if (BuildConfig.DEBUG) {
			source.setTableCreationMode(TableCreationMode.CREATE_NOT_EXISTS)
		}
		return ReactiveSupport.toReactiveStore(EntityDataStore(configuration))
	}
}