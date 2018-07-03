package ru.fantlab.android.provider.storage

import android.arch.persistence.room.Room
import android.content.Context
import ru.fantlab.android.data.db.response.ResponseDatabase

object DbProvider {

	lateinit var responseDatabase: ResponseDatabase
		private set

	fun initDatabase(context: Context) {
		responseDatabase =
				Room.databaseBuilder(context, ResponseDatabase::class.java, "response")
						//.allowMainThreadQueries()
						.build()
	}
}