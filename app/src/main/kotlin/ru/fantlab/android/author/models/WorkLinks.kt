package ru.fantlab.android.author.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
		tableName = "work_links",
		indices = [
			(Index(value = ["parent_work_id"])),
			(Index(value = ["work_id"]))
		]
)
data class WorkLinks(

		@PrimaryKey(autoGenerate = true)
		@ColumnInfo
		val id: Int,

		@ColumnInfo(name = "parent_work_id")
		val parentWorkId: Int,

		@ColumnInfo(name = "work_id")
		val workId: Int,

		@ColumnInfo(name = "group_index")
		val groupIndex: Int?,

		@ColumnInfo(name = "is_bonus")
		val isBonus: Boolean,

		@ColumnInfo(name = "bonus_text")
		val bonusText: String?,

		@ColumnInfo
		val comment: String?
)