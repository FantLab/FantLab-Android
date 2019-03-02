package ru.fantlab.android.data.dao.model
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Publishers(
		val publishers: ArrayList<Publisher>
): Parcelable {
	@Keep
	@Parcelize
	data class Publisher(
			@SerializedName("alt_names")
			val altNames: String,
			@SerializedName("audiopub")
			val audiopub: Int,
			@SerializedName("city")
			val city: String,
			@SerializedName("closed")
			val closed: String,
			@SerializedName("country_id")
			val countryId: String,
			@SerializedName("country_name")
			val countryName: String?,
			@SerializedName("curator_id")
			val curatorId: String,
			@SerializedName("date_of_add")
			val dateOfAdd: String,
			@SerializedName("date_of_change")
			val dateOfChange: String?,
			@SerializedName("descr")
			val descr: String,
			@SerializedName("description")
			val description: String?,
			@SerializedName("edition_count")
			val editionCount: String,
			@SerializedName("editions_count")
			val editionsCount: String,
			@SerializedName("epub")
			val epub: Int,
			@SerializedName("is_fant")
			val isFant: String,
			@SerializedName("letter")
			val letter: String,
			@SerializedName("mark")
			val mark: String,
			@SerializedName("name")
			val name: String,
			@SerializedName("nopub")
			val nopub: String,
			@SerializedName("order")
			val order: Int,
			@SerializedName("process_status")
			val processStatus: String,
			@SerializedName("publisher_id")
			val publisherId: String,
			@SerializedName("rkp")
			val rkp: String,
			@SerializedName("site")
			val site: String?,
			@SerializedName("user_id_of_add")
			val userIdOfAdd: String,
			@SerializedName("user_id_of_change")
			val userIdOfChange: String?,
			@SerializedName("user_login_of_add")
			val userLoginOfAdd: String,
			@SerializedName("user_login_of_change")
			val userLoginOfChange: String?,
			@SerializedName("year_close")
			val yearClose: String?,
			@SerializedName("year_open")
			val yearOpen: String?
	) : Parcelable
}