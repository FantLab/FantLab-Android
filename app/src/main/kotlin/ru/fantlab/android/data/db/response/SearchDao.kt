package ru.fantlab.android.data.db.response

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface SearchDao {

	@Query("SELECT search_query FROM search WHERE user_id == :userId")
	fun get(userId: Int): Single<List<String>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun save(search: Search)
}