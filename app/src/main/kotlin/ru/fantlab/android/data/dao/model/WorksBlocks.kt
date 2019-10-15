package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import ru.fantlab.android.provider.rest.DataManager

@Keep
@Parcelize
data class WorksBlocks(
		val worksBlocks: ArrayList<WorksBlock>
) : Parcelable {
	@Keep
	@Parcelize
	data class WorksBlock(
			val id: Int,
			val list: ArrayList<Work>,
			val name: String,
			val title: String
	) : Parcelable

	@Keep
	@Parcelize
	data class Work(
			val authors: ArrayList<Author>,
			val children: ArrayList<ChildWork>?,
			@SerializedName("lang_id") val languageId: Int,
			@SerializedName("position_index") val positionIndex: Int?,
			@SerializedName("position_is_node") val positionIsNode: Int,
			@SerializedName("position_level") val positionLevel: Int,
			@SerializedName("position_show_in_biblio") val positionShowInBiblio: Int,
			@SerializedName("position_show_subworks_in_biblio") val positionShowSubWorksInBiblio: Int,
			@SerializedName("publish_status") val publishStatus: String,
			@SerializedName("val midmark_by_weight") val rating: Float?,
			@SerializedName("val_responsecount") val responseCount: Int?,
			@SerializedName("val_voters") val votersCount: String?,
			@SerializedName("work_description") val description: String?,
			@SerializedName("work_id") val id: Int?,
			@SerializedName("work_lp") val hasLinguaProfile: Int?,
			@SerializedName("work_name") val name: String?,
			@SerializedName("work_name_alt") val nameAlt: String,
			@SerializedName("work_name_bonus") val nameBonus: String?,
			@SerializedName("work_name_orig") val nameOrig: String?,
			@SerializedName("work_notfinished") val notFinished: Int,
			@SerializedName("work_preparing") val preparing: Int,
			@SerializedName("work_published") val published: Int,
			@SerializedName("work_root_saga") val rootSagas: ArrayList<WorkRootSaga>?,
			@SerializedName("work_type") val type: String?,
			@SerializedName("work_type_id") val typeId: Int,
			@SerializedName("work_type_name") val typeName: String?,
			@SerializedName("work_year") val year: Int?,
			@SerializedName("work_year_of_write") val yearOfWrite: Int?
	) : Parcelable

	@Keep
	@Parcelize
	data class Author(
			val id: Int,
			val name: String,
			val type: String
	) : Parcelable

	class Deserializer : ResponseDeserializable<WorksBlocks> {

		private val worksBlocks: ArrayList<WorksBlock> = arrayListOf()

		override fun deserialize(content: String): WorksBlocks {
			val jsonObject = JsonParser().parse(content).asJsonObject
			jsonObject.entrySet().map {
				val blockObject = it.value.asJsonObject
				val block = DataManager.gson.fromJson(blockObject.toString(), WorksBlock::class.java)
				worksBlocks.add(block)
			}
			return WorksBlocks(worksBlocks)
		}
	}
}