package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Award(
		@SerializedName("award_close") val awardClose: String,
		@SerializedName("award_id") val awardId: String,
		@SerializedName("award_type") val awardType: String,
		@SerializedName("comment") val comment: String,
		@SerializedName("compiler") val compiler: String,
		@SerializedName("contests") val contests: List<Contest>?,
		@SerializedName("copyright") val copyright: String,
		@SerializedName("copyright_link") val copyrightLink: String,
		@SerializedName("country_id") val countryId: String,
		@SerializedName("country_name") val countryName: String,
		@SerializedName("curator_id") val curatorId: String,
		@SerializedName("description") val description: String,
		@SerializedName("homepage") val homepage: String,
		@SerializedName("is_opened") val isOpened: Int,
		@SerializedName("lang_id") val langId: String,
		@SerializedName("max_date") val maxDate: String,
		@SerializedName("min_date") val minDate: String,
		@SerializedName("name") val name: String,
		@SerializedName("nominations") val nominations: List<Nominations>?,
		@SerializedName("non_fantastic") val nonFantastic: String,
		@SerializedName("notes") val notes: String,
		@SerializedName("process_status") val processStatus: String,
		@SerializedName("rusname") val rusname: String,
		@SerializedName("show_in_list") val showInList: String
) : Parcelable {
	@Keep
	@Parcelize
	data class Contest(
			@SerializedName("award_id") val awardId: String,
			@SerializedName("award_name") val awardName: String,
			@SerializedName("award_rusname") val awardRusname: String,
			@SerializedName("contest_id") val contestId: String,
			@SerializedName("contest_works") val contestWorks: List<ContestWork>?,
			@SerializedName("date") val date: String,
			@SerializedName("description") val description: String,
			@SerializedName("description_length") val descriptionLength: String,
			@SerializedName("name") val name: String,
			@SerializedName("nameyear") val nameyear: String,
			@SerializedName("non_winner_count") val nonWinnerCount: String,
			@SerializedName("number") val number: String,
			@SerializedName("place") val place: String?,
			@SerializedName("short_description") val shortDescription: String
	) : Parcelable

	@Keep
	@Parcelize
	data class ContestWork(
			@SerializedName("autor_rusname") val autorRusname: String,
			@SerializedName("award_opened") val awardOpened: String,
			@SerializedName("contest_id") val contestId: Int,
			@SerializedName("contest_work_id") val contestWorkId: Int,
			@SerializedName("contest_year") val contestYear: String,
			@SerializedName("cw_link_id") val cwLinkId: Int?,
			@SerializedName("cw_link_type") val cwLinkType: String,
			@SerializedName("cw_name") val cwName: String?,
			@SerializedName("cw_number") val cwNumber: String,
			@SerializedName("cw_postfix") val cwPostfix: String,
			@SerializedName("cw_prefix") val cwPrefix: String?,
			@SerializedName("cw_rusname") val cwRusname: String,
			@SerializedName("cw_winner") val cwWinner: String,
			@SerializedName("nomination_id") val nominationId: String,
			@SerializedName("nomination_name") val nominationName: String?,
			@SerializedName("nomination_number") val nominationNumber: String,
			@SerializedName("nomination_rusname") val nominationRusname: String,
			@SerializedName("work_rusname") val workRusname: String?
	) : Parcelable

	@Keep
	@Parcelize
	data class Nominations(
			@SerializedName("book_count") val bookCount: String,
			@SerializedName("description") val description: String,
			@SerializedName("description_length") val descriptionLength: String,
			@SerializedName("name") val name: String,
			@SerializedName("nomination_id") val nominationId: String,
			@SerializedName("number") val number: String,
			@SerializedName("rusname") val rusname: String
	) : Parcelable

}
