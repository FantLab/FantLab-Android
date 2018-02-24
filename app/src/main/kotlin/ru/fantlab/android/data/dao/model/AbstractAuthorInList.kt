package ru.fantlab.android.data.dao.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.requery.Column
import io.requery.Entity
import io.requery.Key
import io.requery.Table

@Entity @Table(name = "author_in_list")
abstract class AbstractAuthorInList() : Parcelable {

	@JvmField @SerializedName("autor_id") @Column(name = "author_id") @Key var id: Int? = null
	@JvmField @Column var name: String? = null
	@JvmField @Column var nameOrig: String? = null
	@JvmField @Column var nameShort: String? = null

	constructor(parcel: Parcel) : this() {
		id = parcel.readValue(Int::class.java.classLoader) as? Int
		name = parcel.readString()
		nameOrig = parcel.readString()
		nameShort = parcel.readString()
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(id)
		parcel.writeString(name)
		parcel.writeString(nameOrig)
		parcel.writeString(nameShort)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<AuthorInList> {
		override fun createFromParcel(parcel: Parcel): AuthorInList {
			return AuthorInList(parcel)
		}

		override fun newArray(size: Int): Array<AuthorInList?> {
			return arrayOfNulls(size)
		}
	}
}