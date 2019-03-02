package ru.fantlab.android.provider.storage

import android.content.Context
import androidx.room.Room
import ru.fantlab.android.data.db.MainDatabase

object DbProvider {

	lateinit var mainDatabase: MainDatabase
		private set

	fun initDatabase(context: Context) {
		mainDatabase =
				Room.databaseBuilder(context, MainDatabase::class.java, "main")
						.allowMainThreadQueries()
						.build()
	}
}