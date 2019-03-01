package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import android.support.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class BookcaseEdition (
    val name: String,
    val bookcase_item_id: Int,
    val autors: String,
    val edition_id: Int,
    val publisher: String,
    val year: Int,
    val item_comment: String
) : Parcelable