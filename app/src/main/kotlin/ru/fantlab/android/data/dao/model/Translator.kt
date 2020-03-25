package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Translator (
    val age: Int,
    val articles: ArrayList<String>?,
    val biography: String?,
    val anons: String?,
    @SerializedName("biography_notes") val biographyNotes: String,
    @SerializedName("biography_source") val biographySource: String,
    val birthday: String,
    val countries: ArrayList<TranslatorCountry>,
    @SerializedName("curator_id") val curatorId: Int,
    @SerializedName("curator_name") val curatorName: String,
    val deathday: String,
    @SerializedName("has_advanced_awards") val advancedAwards: Int,
    val id: Int,
    val name: String,
    @SerializedName("name_orig") val nameOriginal: String,
    @SerializedName("name_rp") val nameGenitive: String,
    @SerializedName("name_short") val nameShort: String,
    @SerializedName("name_short_rp") val nameShortGenitive: String,
    @SerializedName("name_sort") val nameSort: String,
    val notes: String?,
    @SerializedName("person_type") val personType: String,
    val photo: Int,
    @SerializedName("pseudo_id") val pseudoId: Int,
    @SerializedName("pseudo_name") val pseudoName: String?,
    val sex: Int,
    @SerializedName("show_in_list") val showInList: Int,
    @SerializedName("translated_from") val translatorFrom: String,
    @SerializedName("translated_to") val translatorTo: String,
    val image: String?,
    @SerializedName("image_preview") val preview: String?
) : Parcelable {
    @Keep
    @Parcelize
    data class TranslatorCountry(
            @SerializedName("country_id") val id: Int,
            val name: String
    ) : Parcelable

    @Keep
    @Parcelize
    data class TranslationAward(
            @SerializedName("award_icon") val awardIcon: String,
            @SerializedName("award_id") val awardId: Int,
            @SerializedName("award_in_list") val awardInList: Int,
            @SerializedName("award_is_opened") val isAwardOpened: Int,
            @SerializedName("award_name") val awardName: String,
            @SerializedName("award_rusname") val awardRusName: String,
            @SerializedName("contest_id") val contestId: Int,
            @SerializedName("contest_name") val contestName: String,
            @SerializedName("contest_year") val contestYear: Int,
            @SerializedName("cw_id") val id: Int,
            @SerializedName("cw_is_winner") val isWinner: Int,
            @SerializedName("cw_postfix") val postfix: String?,
            @SerializedName("cw_prefix") val prefix: String?,
            @SerializedName("nomination_id") val nominationId: Int?,
            @SerializedName("nomination_name") val nominationName: String?,
            @SerializedName("nomination_rusname") val nominationRusName: String?,
            @SerializedName("work_id") val workId: Int,
            @SerializedName("work_rusname") val workRusName: String,
            @SerializedName("work_year") val workYear: String?
    ) : Parcelable

    @Keep
    @Parcelize
    data class TranslatedWork(
            val author: AuthorCard,
            val editions: ArrayList<EditionCard>,
            val work: WorkCard
    ) : Parcelable

    class AwardsConverter {
        fun convert(awards: ArrayList<TranslationAward>): ArrayList<Nomination> {
            val res: ArrayList<Nomination> = arrayListOf()
            awards.forEach { award ->
                val nomination = Nomination(
                        award.awardIcon,
                        award.awardId,
                        award.awardInList,
                        award.isAwardOpened,
                        award.awardName,
                        award.awardRusName,
                        award.contestId,
                        award.contestName,
                        award.contestYear,
                        award.id,
                        award.isWinner,
                        award.postfix,
                        award.prefix,
                        award.nominationId ?: 0,
                        award.nominationName ?: "",
                        award.nominationRusName ?: ""
                )
                res.add(nomination)
            }
            return res
        }
    }
}