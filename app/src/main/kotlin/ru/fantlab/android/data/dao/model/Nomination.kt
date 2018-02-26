package ru.fantlab.android.data.dao.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.requery.*

@Entity @Table(name = "nomination")
data class Nomination(
		@SerializedName("cw_id") @get:Column(name = "contest_year") @get:Key var id: Int,
		@get:Column(name = "award_icon") var awardIcon: String,
		@get:Column(name = "award_id") var awardId: Int,
		@get:Column(name = "award_is_opened") var awardIsOpened: Int,
		@get:Column(name = "award_name") var awardName: String,
		@SerializedName("award_rusname") @get:Column(name = "award_rus_name") var awardRusName: String,
		@get:Column(name = "contest_id") var contestId: Int,
		@get:Column(name = "contest_name") var contestName: String,
		@get:Column(name = "contest_year") var contestYear: Int,
		@SerializedName("cw_is_winner") @get:Column(name = "win") var win: Int,
		@SerializedName("cw_postfix") @get:Column(name = "postfix") var postfix: String,
		@SerializedName("cw_prefix") @get:Column(name = "prefix") var prefix: Int,
		@get:Column(name = "nomination_id") var nominationId: Int,
		@get:Column(name = "nomination_name") var nominationName: String,
		@SerializedName("nomination_rusname") @get:Column(name = "nomination_rus_name") var nominationRusName: String,
		@get:Column(name = "work_id") var workId: Int,
		@get:Column(name = "work_name") var workName: String,
		@get:Column(name = "work_rusname") var workRusName: String,
		@get:Column(name = "work_year") var workYear: String,
		@get:Column(name = "author_id") var authorId: Int?
) : Persistable, Parcelable {

	constructor(parcel: Parcel) : this(
			parcel.readInt(),
			parcel.readString(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readString(),
			parcel.readString(),
			parcel.readInt(),
			parcel.readString(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readString(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readString(),
			parcel.readString(),
			parcel.readInt(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readValue(Int::class.java.classLoader) as? Int)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(id)
		parcel.writeString(awardIcon)
		parcel.writeInt(awardId)
		parcel.writeInt(awardIsOpened)
		parcel.writeString(awardName)
		parcel.writeString(awardRusName)
		parcel.writeInt(contestId)
		parcel.writeString(contestName)
		parcel.writeInt(contestYear)
		parcel.writeInt(win)
		parcel.writeString(postfix)
		parcel.writeInt(prefix)
		parcel.writeInt(nominationId)
		parcel.writeString(nominationName)
		parcel.writeString(nominationRusName)
		parcel.writeInt(workId)
		parcel.writeString(workName)
		parcel.writeString(workRusName)
		parcel.writeString(workYear)
		parcel.writeValue(authorId)
	}

	@Transient
	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Nomination> {
		override fun createFromParcel(parcel: Parcel): Nomination {
			return Nomination(parcel)
		}

		override fun newArray(size: Int): Array<Nomination?> {
			return arrayOfNulls(size)
		}
	}
}