package org.odddev.fantlab.core.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import org.odddev.fantlab.author.models.*

@Database(
		entities = [
			(Author::class),
			(AuthorPseudonym::class),
			(AuthorStat::class),
			(Work::class),
			(WorkLinks::class)
		],
		version = 1
)
abstract class MainDatabase : RoomDatabase() {

	abstract fun authorDao(): AuthorDao

	abstract fun authorPseudonymDao(): AuthorPseudonymDao

	abstract fun authorStatDao(): AuthorStatDao
}