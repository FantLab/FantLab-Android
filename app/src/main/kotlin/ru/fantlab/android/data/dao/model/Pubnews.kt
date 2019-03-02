package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Pubnews(
		@SerializedName("all_pages_access_to_user_class")
		val allPagesAccessToUserClass: Int,
		@SerializedName("has_news_plans")
		val hasNewsPlans: HasNewsPlans,
		@SerializedName("image_url")
		val imageUrl: String,
		@SerializedName("interval_days")
		val intervalDays: Int,
		@SerializedName("lang")
		val lang: Int,
		@SerializedName("last_read_date")
		val lastReadDate: String?,
		@SerializedName("mode")
		val mode: String,
		@SerializedName("objects")
		val objects: List<Object>,
		@SerializedName("objects_in_page")
		val objectsInPage: Int,
		@SerializedName("page")
		val page: Int,
		@SerializedName("page_count")
		val pageCount: Int,
		@SerializedName("page_position")
		val pagePosition: PagePosition,
		@SerializedName("pub_id")
		val pubId: String,
		@SerializedName("pub_name")
		val pubName: String,
		@SerializedName("publisher_list")
		val publisherList: List<Publisher>,
		@SerializedName("pubnews_communities")
		val pubnewsCommunities: List<PubnewsCommunity>,
		@SerializedName("sort")
		val sort: String,
		@SerializedName("total_count")
		val totalCount: Int
) : Parcelable {
	@Keep
	@Parcelize
	data class Object(
			@SerializedName("autors")
			val autors: String,
			@SerializedName("correct_date")
			val correctDate: String,
			@SerializedName("date")
			val date: String,
			@SerializedName("description")
			val description: String,
			@SerializedName("edition_id")
			val editionId: String,
			@SerializedName("first_edition")
			val firstEdition: String,
			@SerializedName("labirint_available")
			val labirintAvailable: String,
			@SerializedName("labirint_cost")
			val labirintCost: String?,
			@SerializedName("labirint_id")
			val labirintId: String?,
			@SerializedName("name")
			val name: String,
			@SerializedName("ozon_available")
			val ozonAvailable: String,
			@SerializedName("ozon_cost")
			val ozonCost: String?,
			@SerializedName("ozon_id")
			val ozonId: String?,
			@SerializedName("pic_orig")
			val picOrig: String,
			@SerializedName("plan_description")
			val planDescription: String,
			@SerializedName("preread")
			val preread: String,
			@SerializedName("publisher")
			val publisher: String,
			@SerializedName("release_date")
			val releaseDate: String,
			@SerializedName("series")
			val series: String,
			@SerializedName("sort_string")
			val sortString: String?,
			@SerializedName("subscribed")
			val subscribed: String?,
			@SerializedName("the_only_work")
			val theOnlyWork: theOnlyWork?,
			@SerializedName("type_name")
			val typeName: String
	) : Parcelable

	@Keep
	@Parcelize
	data class HasNewsPlans(
			@SerializedName("autplans0_new")
			val autplans0New: String?,
			@SerializedName("autplans1_new")
			val autplans1New: String?,
			@SerializedName("pubnews0_new")
			val pubnews0New: String?,
			@SerializedName("pubnews1_new")
			val pubnews1New: String?,
			@SerializedName("pubplans0_new")
			val pubplans0New: String,
			@SerializedName("pubplans1_new")
			val pubplans1New: String?
	) : Parcelable

	@Keep
	@Parcelize
	data class PubnewsCommunity(
			@SerializedName("blog_id")
			val blogId: String,
			@SerializedName("name")
			val name: String,
			@SerializedName("subscribed")
			val subscribed: String?
	) : Parcelable

	@Keep
	@Parcelize
	data class PagePosition(
			@SerializedName("end")
			val end: Int,
			@SerializedName("start")
			val start: Int
	) : Parcelable

	@Keep
	@Parcelize
	data class Publisher(
			@SerializedName("cnt")
			val cnt: String,
			@SerializedName("name")
			val name: String,
			@SerializedName("publisher_id")
			val publisherId: String
	) : Parcelable

	@Keep
	@Parcelize
	data class theOnlyWork(
			@SerializedName("markcount")
			val markcount: String?,
			@SerializedName("midmark")
			val midmark: String?,
			@SerializedName("name_show_im")
			val nameShowIm: String,
			@SerializedName("name_show_rod")
			val nameShowRod: String,
			@SerializedName("responsecount")
			val responsecount: String?,
			@SerializedName("work_id")
			val workId: String
	) : Parcelable

}