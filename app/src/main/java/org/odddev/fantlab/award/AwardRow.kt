package org.odddev.fantlab.award

import android.os.Parcelable
import io.requery.Entity
import io.requery.Key
import io.requery.Persistable

/**
 * @author Ivan Zolotarev
 * @since 16.05.17
 */

@Entity
interface AwardRow : Parcelable, Persistable {

    @get:Key
    val id: Int

    val awardId: String

    val country: String

    val description: String

    val maxDate: String

    val minDate: String

    val name: String

    val rusName: String

    val language: Int

    val type: Int
}
