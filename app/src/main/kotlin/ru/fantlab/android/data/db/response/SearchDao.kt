package ru.fantlab.android.data.db.response

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single

@Dao
interface SearchDao {

	@Query("SELECT search_query FROM search WHERE user_id == :userId")
	fun get(userId: Int): Single<List<String>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun save(search: Search)
}