package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Autplans(
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
		val lastReadDate: String,
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
		@SerializedName("pubnews_communities")
		val pubnewsCommunities: List<PubnewsCommunity>,
		@SerializedName("sort")
		val sort: String,
		@SerializedName("total_count")
		val totalCount: Int
) : Parcelable {
	@Keep
	@Parcelize
	data class PubnewsCommunity(
			@SerializedName("blog_id")
			val blogId: String,
			@SerializedName("name")
			val name: String,
			@SerializedName("subscribed")
			val subscribed: String
	) : Parcelable

	@Keep
	@Parcelize
	data class Object(
			@SerializedName("add_info")
			val addInfo: String,
			@SerializedName("autors")
			val autors: Autors,
			@SerializedName("date_of_add")
			val dateOfAdd: String,
			@SerializedName("description")
			val description: String,
			@SerializedName("name")
			val name: String,
			@SerializedName("rusname")
			val rusname: String,
			@SerializedName("saga")
			val saga: Saga,
			@SerializedName("sort_string")
			val sortString: String,
			@SerializedName("subscription_id")
			val subscriptionId: String,
			@SerializedName("work_id")
			val workId: String,
			@SerializedName("work_type")
			val workType: String,
			@SerializedName("year")
			val year: String?
	) : Parcelable {
		@Keep
		@Parcelize
		data class Autors(
				@SerializedName("autor2_id")
				val autor2Id: String,
				@SerializedName("autor2_is_opened")
				val autor2IsOpened: String,
				@SerializedName("autor2_name")
				val autor2Name: String,
				@SerializedName("autor2_rusname")
				val autor2Rusname: String,
				@SerializedName("autor2_rusname_rp")
				val autor2RusnameRp: String,
				@SerializedName("autor2_shortrusname")
				val autor2Shortrusname: String,
				@SerializedName("autor3_id")
				val autor3Id: String?,
				@SerializedName("autor3_is_opened")
				val autor3IsOpened: String?,
				@SerializedName("autor3_name")
				val autor3Name: String?,
				@SerializedName("autor3_rusname")
				val autor3Rusname: String?,
				@SerializedName("autor3_rusname_rp")
				val autor3RusnameRp: String?,
				@SerializedName("autor3_shortrusname")
				val autor3Shortrusname: String?,
				@SerializedName("autor4_id")
				val autor4Id: String?,
				@SerializedName("autor4_is_opened")
				val autor4IsOpened: String?,
				@SerializedName("autor4_name")
				val autor4Name: String?,
				@SerializedName("autor4_rusname")
				val autor4Rusname: String?,
				@SerializedName("autor4_rusname_rp")
				val autor4RusnameRp: String?,
				@SerializedName("autor4_shortrusname")
				val autor4Shortrusname: String?,
				@SerializedName("autor5_id")
				val autor5Id: String?,
				@SerializedName("autor5_is_opened")
				val autor5IsOpened: String?,
				@SerializedName("autor5_name")
				val autor5Name: String?,
				@SerializedName("autor5_rusname")
				val autor5Rusname: String?,
				@SerializedName("autor5_rusname_rp")
				val autor5RusnameRp: String?,
				@SerializedName("autor5_shortrusname")
				val autor5Shortrusname: String?,
				@SerializedName("autor_id")
				val autorId: String,
				@SerializedName("autor_is_opened")
				val autorIsOpened: String,
				@SerializedName("autor_name")
				val autorName: String,
				@SerializedName("autor_rusname")
				val autorRusname: String,
				@SerializedName("autor_rusname_rp")
				val autorRusnameRp: String,
				@SerializedName("autor_shortrusname")
				val autorShortrusname: String
		) : Parcelable

		@Keep
		@Parcelize
		data class Saga(
				@SerializedName("name")
				val name: String,
				@SerializedName("rusname")
				val rusname: String,
				@SerializedName("work_id")
				val workId: String,
				@SerializedName("work_type")
				val workType: String?
		) : Parcelable
	}

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
			val pubplans0New: String?,
			@SerializedName("pubplans1_new")
			val pubplans1New: String?
	) : Parcelable
}