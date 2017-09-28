package org.odddev.fantlab.core.models

import android.support.annotation.Keep

@Keep
data class Work(
		// work_id
		val workId: Int,
		// position_level
		val blockId: Int? = null,
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
		// work_root_saga -> work_id
		val rootWorkId: Int? = null,
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
