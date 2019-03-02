package ru.fantlab.android.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.fantlab.android.data.db.response.Response
import ru.fantlab.android.data.db.response.ResponseDao
import ru.fantlab.android.data.db.response.Search
import ru.fantlab.android.data.db.response.SearchDao

@Database(entities = [
	Response::class,
	Search::class
], version = 1)
abstract class MainDatabase : RoomDatabase() {

	abstract fun responseDao(): ResponseDao

	abstract fun searchDao(): SearchDao
}