package org.odddev.fantlab.authors

import android.arch.persistence.room.*

@Entity(tableName = "works")
data class Work(

		@PrimaryKey
		@ColumnInfo(name = "work_id")
		val workId: Int,

		@ColumnInfo(name = "autor_id")
		val authorId: Int,

		@ColumnInfo(name = "autor2_id")
		val author2Id: Int?,

		@ColumnInfo(name = "autor3_id")
		val author3Id: Int?,

		@ColumnInfo(name = "autor4_id")
		val author4Id: Int?,

		@ColumnInfo(name = "autor5_id")
		val author5Id: Int?,

		@ColumnInfo(name = "rusname")
		val rusName: String?,

		@ColumnInfo
		val name: String?,

		@ColumnInfo(name = "altname")
		val altName: String?,

		@ColumnInfo(name = "parent_work_id")
		val parentWorkId: Int?,

		@ColumnInfo(name = "genre_id")
		val genreId: Int?,

		@ColumnInfo(name = "work_type_id")
		val workTypeId: Int,

		@ColumnInfo(name = "for_children")
		val forChildren: Boolean,

		@ColumnInfo
		val year: Int?,

		@ColumnInfo(name = "year_of_write")
		val yearOfWrite: Int?,

		@ColumnInfo
		val description: String?,

		@ColumnInfo(name = "descr_autor")
		val descriptionAuthor: String?,

		@ColumnInfo(name = "descr_user_id")
		val descriptionUserId: Int?,

		@ColumnInfo(name = "is_bonus")
		val isBonus: Boolean,

		@ColumnInfo(name = "bonus_text")
		val bonusText: String?,

		@ColumnInfo(name = "add_info")
		val addInfo: String?,

		@ColumnInfo
		val comment: String?,

		@ColumnInfo(name = "not_finished")
		val notFinished: Boolean,

		@ColumnInfo(name = "is_plan")
		val isPlan: Boolean,

		@ColumnInfo
		val published: Boolean,

		@ColumnInfo(name = "cycle_full")
		val cycleFull: Boolean?,

		@ColumnInfo(name = "cycle_type")
		val cycleType: Int?,

		@ColumnInfo(name = "has_lp")
		val hasLp: Boolean,

		@ColumnInfo(name = "download_file")
		val downloadFile: String?,

		@ColumnInfo(name = "file_type")
		@TypeConverters(FileTypeConverter::class)
		val fileType: FileType?,

		@ColumnInfo(name = "public_download")
		val publicDownload: Boolean,

		@ColumnInfo(name = "changer_uid")
		val changerUid: Int?,

		@ColumnInfo(name = "work_add1")
		val workAdd1: String,

		@ColumnInfo(name = "work_add2")
		val workAdd2: String,

		@ColumnInfo(name = "work_add3")
		val workAdd3: String,

		@ColumnInfo(name = "voter_count")
		val voterCount: Int,

		@ColumnInfo(name = "is_fiction")
		val isFiction: Boolean,

		@ColumnInfo(name = "art_id")
		val artId: Int?,

		@ColumnInfo(name = "art2_id")
		val art2Id: Int?,

		@ColumnInfo(name = "art3_id")
		val art3Id: Int?,

		@ColumnInfo(name = "art4_id")
		val art4Id: Int?,

		@ColumnInfo(name = "art5_id")
		val art5Id: Int?,

		@ColumnInfo(name = "film_id")
		val filmId: Int?,

		@ColumnInfo(name = "pic_edition_id_auto")
		val picEditionIdAuto: Int?,

		@ColumnInfo(name = "pic_edition_id")
		val picEditionId: Int?
) {
	enum class FileType(val fileType: String?) {
		FB2("fb2"),
		ZIP("zip")
	}

	class FileTypeConverter {

		@TypeConverter
		fun toFileType(fileType: String) =
				when (fileType) {
					"fb2" -> FileType.FB2
					"zip" -> FileType.ZIP
					else -> null
				}

		@TypeConverter
		fun toString(fileType: FileType?) = fileType?.fileType
	}
}