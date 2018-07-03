package ru.fantlab.android.data.db.response

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [
	(Response::class)
], version = 1)
abstract class ResponseDatabase : RoomDatabase() {

	abstract fun responseDao(): ResponseDao
}