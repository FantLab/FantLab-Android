package ru.fantlab.android.data.dao.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.requery.Column
import io.requery.Entity
import ru.fantlab.android.App
import ru.fantlab.android.data.dao.model.Login.USER_ID
import java.util.*

@Entity
abstract class AbstractUser() : Parcelable {

	@JvmField
	@Column
	var userId: Int? = null

	@JvmField
	@Column
	var login: String? = null

	@JvmField
	@Column
	var avatar: String? = null

	@JvmField
	@Column
	var fio: String? = null

	@JvmField
	@Column
	var sex: String? = null

	@JvmField
	@SerializedName("birthday")
	@Column
	var birthDay: Date? = null

	@JvmField
	@Column
	var userClass: Int? = null

	@JvmField
	@Column
	var className: String? = null

	@JvmField
	@Column
	var level: Float? = null

	@JvmField
	@Column
	var location: String? = null

	@JvmField
	@Column
	var countryName: String? = null

	@JvmField
	@Column
	var countryId: Int? = null

	@JvmField
	@Column
	var cityName: String? = null

	@JvmField
	@Column
	var cityId: Int? = null

	@JvmField
	@Column
	var dateOfReg: Date? = null

	@JvmField
	@Column
	var dateOfLastAction: Date? = null

	@JvmField
	@Column
	var userTimer: Long? = null

	@JvmField
	@Column
	var sign: String? = null

	@JvmField
	@SerializedName("markcount")
	@Column
	var markCount: Int? = null

	@JvmField
	@SerializedName("responsecount")
	@Column
	var responseCount: Int? = null

	@JvmField
	@SerializedName("descriptioncount")
	@Column
	var descriptionCount: Int? = null

	@JvmField
	@SerializedName("classifcount")
	@Column
	var classifCount: Int? = null

	@JvmField
	@SerializedName("votecount")
	@Column
	var voteCount: Int? = null

	@JvmField
	@SerializedName("topiccount")
	@Column
	var topicCount: Int? = null

	@JvmField
	@SerializedName("messagecount")
	@Column
	var messageCount: Int? = null

	@JvmField
	@Column
	var bookcaseCount: Int? = null

	@JvmField
	@SerializedName("curator_autors")
	@Column
	var curatorAuthors: Int? = null

	@JvmField
	@Column
	var ticketsCount: Int? = null

	@JvmField
	@SerializedName("autor_id")
	@Column
	var authorId: Int? = null

	@JvmField
	@SerializedName("autor_name")
	@Column
	var authorName: String? = null

	@JvmField
	@SerializedName("autor_name_orig")
	@Column
	var authorNameOrig: String? = null

	@JvmField
	@SerializedName("autor_is_opened")
	@Column
	var authorIsOpened: Int? = null

	@JvmField
	@Column
	var blogId: Int? = null

	@JvmField
	@Column
	var block: Int? = null

	@JvmField
	@Column
	var dateOfBlock: Date? = null

	@JvmField
	@Column
	var dateOfBlockEnd: Date? = null

	constructor(parcel: Parcel) : this() {
		userId = parcel.readValue(Int::class.java.classLoader) as? Int
		login = parcel.readString()
		avatar = parcel.readString()
		fio = parcel.readString()
		sex = parcel.readString()
		userClass = parcel.readValue(Int::class.java.classLoader) as? Int
		className = parcel.readString()
		level = parcel.readValue(Float::class.java.classLoader) as? Float
		location = parcel.readString()
		countryName = parcel.readString()
		countryId = parcel.readValue(Int::class.java.classLoader) as? Int
		cityName = parcel.readString()
		cityId = parcel.readValue(Int::class.java.classLoader) as? Int
		userTimer = parcel.readValue(Long::class.java.classLoader) as? Long
		sign = parcel.readString()
		markCount = parcel.readValue(Int::class.java.classLoader) as? Int
		responseCount = parcel.readValue(Int::class.java.classLoader) as? Int
		descriptionCount = parcel.readValue(Int::class.java.classLoader) as? Int
		classifCount = parcel.readValue(Int::class.java.classLoader) as? Int
		voteCount = parcel.readValue(Int::class.java.classLoader) as? Int
		topicCount = parcel.readValue(Int::class.java.classLoader) as? Int
		messageCount = parcel.readValue(Int::class.java.classLoader) as? Int
		bookcaseCount = parcel.readValue(Int::class.java.classLoader) as? Int
		curatorAuthors = parcel.readValue(Int::class.java.classLoader) as? Int
		ticketsCount = parcel.readValue(Int::class.java.classLoader) as? Int
		authorId = parcel.readValue(Int::class.java.classLoader) as? Int
		authorName = parcel.readString()
		authorNameOrig = parcel.readString()
		authorIsOpened = parcel.readValue(Int::class.java.classLoader) as? Int
		blogId = parcel.readValue(Int::class.java.classLoader) as? Int
		block = parcel.readValue(Int::class.java.classLoader) as? Int
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(userId)
		parcel.writeString(login)
		parcel.writeString(avatar)
		parcel.writeString(fio)
		parcel.writeString(sex)
		parcel.writeValue(userClass)
		parcel.writeString(className)
		parcel.writeValue(level)
		parcel.writeString(location)
		parcel.writeString(countryName)
		parcel.writeValue(countryId)
		parcel.writeString(cityName)
		parcel.writeValue(cityId)
		parcel.writeValue(userTimer)
		parcel.writeString(sign)
		parcel.writeValue(markCount)
		parcel.writeValue(responseCount)
		parcel.writeValue(descriptionCount)
		parcel.writeValue(classifCount)
		parcel.writeValue(voteCount)
		parcel.writeValue(topicCount)
		parcel.writeValue(messageCount)
		parcel.writeValue(bookcaseCount)
		parcel.writeValue(curatorAuthors)
		parcel.writeValue(ticketsCount)
		parcel.writeValue(authorId)
		parcel.writeString(authorName)
		parcel.writeString(authorNameOrig)
		parcel.writeValue(authorIsOpened)
		parcel.writeValue(blogId)
		parcel.writeValue(block)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<User> {
		override fun createFromParcel(parcel: Parcel): User {
			return User(parcel)
		}

		override fun newArray(size: Int): Array<User?> {
			return arrayOfNulls(size)
		}
	}
}

fun User.save() {
	if (getUser(this.userId!!) != null) {
		App.dataStore.toBlocking().update(this)
	} else {
		App.dataStore.toBlocking().insert(this)
	}
}

fun getUser(id: Int): User? {
	return App.dataStore
			.select(User::class.java)
			.where(USER_ID.eq(id))
			.get()
			.firstOrNull()
}
