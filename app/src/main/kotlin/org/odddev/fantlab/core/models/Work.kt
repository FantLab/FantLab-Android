package org.odddev.fantlab.core.models

import android.support.annotation.Keep

@Keep
data class Work(
		// work_id
		val id: Int,
		// authors = {id, name, type}
		val authors: List<String>? = null,                    // по-хорошему завести отдельную таблицу для связи
		// public_download_file
		val canDownload: Boolean? = null,
		// deep
		val deep: Int? = null,                                // используется в cycles_blocks
		// work_description
		val description: String? = null,
		// work_lp
		val hasLp: Boolean? = null,                           // есть лингвопрофиль
		// val_midmark
		val midMark: Float? = null,
		// work_name
		val name: String? = null,
		// work_name_alt
		val nameAlt: String? = null,
		// work_name_bonus
		val nameBonus: String? = null,
		// work_name_orig
		val nameOrig: String? = null,
		// work_notfinished
		val notFinished: Boolean? = null,
		// plus
		val plus: Boolean? = null,                            // используется в cycles_blocks
		// position_index
		val positionIndex: Int? = null,                       // используется в works_blocks
		// position_is_node
		val positionIsNode: Boolean? = null,                  // используется в works_blocks
		// position_level
		val positionLevel: Int? = null,                       // используется в works_blocks
		// position_show_in_biblio
		val positionShowInBiblio: Boolean? = null,            // используется в works_blocks
		// position_show_subworks_in_biblio
		val positionShowSubWorksInBiblio: Boolean? = null,    // используется в works_blocks
		// work_preparing
		val preparing: Boolean? = null,                       // в планах
		// work_published
		val published: Boolean? = null,
		// publish_status
		val publishStatus: String? = null,
		// val_midmark_rating
		val rating: Float? = null,
		// val_responsecount
		val responseCount: Int? = null,
		// work_root_saga = {prefix, work_id, work_name, work_type, work_type_id, work_type_in}
		val rootSaga: List<String>? = null,                   // входит в ...
		// work_type_id
		val type: Int? = null,
		// work_year_of_write
		val writeYear: Int? = null,
		// work_year
		val year: Int? = null,
		// val_voters
		val voters: Int? = null
		//val forChildren: Boolean? = null                    // что за параметр?
)
