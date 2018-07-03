package ru.fantlab.android.data.db.response

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Maybe

@Dao
interface ResponseDao {

	@Query("SELECT * FROM response WHERE url LIKE :url AND api_version LIKE :apiVersion LIMIT 1")
	fun get(url: String, apiVersion: String): Maybe<Response>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun save(response: Response)
}