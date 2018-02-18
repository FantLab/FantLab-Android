package ru.fantlab.android.data.dao.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.requery.Column
import io.requery.Entity
import io.requery.Key
import io.requery.Table
import ru.fantlab.android.App
import ru.fantlab.android.data.dao.model.Login.USER_ID
import java.util.*

@Entity @Table(name = "user")
abstract class AbstractUser() : Parcelable {

	@JvmField @Column(name = "id") @Key var userId: Int? = null
	@JvmField @Column var login: String? = null
	@JvmField @Column var avatar: String? = null
	@JvmField @Column var fio: String? = null
	@JvmField @Column var sex: String? = null
	@JvmField @SerializedName("birthday") @Column(name = "birthday") var birthDay: Date? = null
	@JvmField @Column(name = "class") var userClass: Int? = null
	@JvmField @Column(name = "class_name") var className: String? = null
	@JvmField @Column var level: Float? = null
	@JvmField @Column var location: String? = null
	@JvmField @Column(name = "country_name") var countryName: String? = null
	@JvmField @Column(name = "country_id") var countryId: Int? = null
	@JvmField @Column(name = "city_name") var cityName: String? = null
	@JvmField @Column(name = "city_id") var cityId: Int? = null
	@JvmField @Column(name = "reg_date") var dateOfReg: Date? = null
	@JvmField @Column(name = "last_action_date") var dateOfLastAction: Date? = null
	@JvmField @Column(name = "timer") var userTimer: Long? = null
	@JvmField @Column var sign: String? = null
	@JvmField @SerializedName("markcount") @Column(name = "mark_count") var markCount: Int? = null
	@JvmField @SerializedName("responsecount") @Column(name = "response_count") var responseCount: Int? = null
	@JvmField @SerializedName("descriptioncount") @Column(name = "description_count") var descriptionCount: Int? = null
	@JvmField @SerializedName("classifcount") @Column(name = "classif_count") var classifCount: Int? = null
	@JvmField @SerializedName("votecount") @Column(name = "vote_count") var voteCount: Int? = null
	@JvmField @SerializedName("topiccount") @Column(name = "topic_count") var topicCount: Int? = null
	@JvmField @SerializedName("messagecount") @Column(name = "message_count") var messageCount: Int? = null
	@JvmField @Column(name = "bookcase_count") var bookcaseCount: Int? = null
	@JvmField @SerializedName("curator_autors") @Column(name = "curator_authors") var curatorAuthors: Int? = null
	@JvmField @Column(name = "ticket_count") var ticketsCount: Int? = null
	@JvmField @SerializedName("autor_id") @Column(name = "author_id") var authorId: Int? = null
	@JvmField @SerializedName("autor_name") @Column(name = "author_name") var authorName: String? = null
	@JvmField @SerializedName("autor_name_orig") @Column(name = "author_name_orig") var authorNameOrig: String? = null
	@JvmField @SerializedName("autor_is_opened") @Column(name = "author_is_opened") var authorIsOpened: Int? = null
	@JvmField @Column(name = "blog_id") var blogId: Int? = null
	@JvmField @Column(name = "blocked") var block: Int? = null
	@JvmField @Column(name = "block_start_date") var dateOfBlock: Date? = null
	@JvmField @Column(name = "block_end_date") var dateOfBlockEnd: Date? = null

	constructor(parcel: Parcel) : this() {
		userId = parcel.readValue(Int::class.java.classLoader) as? Int
		login = parcel.readString()
		avatar = parcel.readString()
		fio = parcel.readString()
		sex = parcel.readString()
		val tmpBirthDay = parcel.readLong()
		birthDay = if (tmpBirthDay == -1L) null else Date(tmpBirthDay)
		userClass = parcel.readValue(Int::class.java.classLoader) as? Int
		className = parcel.readString()
		level = parcel.readValue(Float::class.java.classLoader) as? Float
		location = parcel.readString()
		countryName = parcel.readString()
		countryId = parcel.readValue(Int::class.java.classLoader) as? Int
		cityName = parcel.readString()
		cityId = parcel.readValue(Int::class.java.classLoader) as? Int
		val tmpDateOfReg = parcel.readLong()
		dateOfReg = if (tmpDateOfReg == -1L) null else Date(tmpDateOfReg)
		val tmpDateOfLastAction = parcel.readLong()
		dateOfLastAction = if (tmpDateOfLastAction == -1L) null else Date(tmpDateOfLastAction)
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
		val tmpDateOfBlock = parcel.readLong()
		dateOfBlock = if (tmpDateOfBlock == -1L) null else Date(tmpDateOfBlock)
		val tmpDateOfBlockEnd = parcel.readLong()
		dateOfBlockEnd = if (tmpDateOfBlockEnd == -1L) null else Date(tmpDateOfBlockEnd)
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(userId)
		parcel.writeString(login)
		parcel.writeString(avatar)
		parcel.writeString(fio)
		parcel.writeString(sex)
		parcel.writeLong(if (birthDay != null) birthDay!!.time else -1)
		parcel.writeValue(userClass)
		parcel.writeString(className)
		parcel.writeValue(level)
		parcel.writeString(location)
		parcel.writeString(countryName)
		parcel.writeValue(countryId)
		parcel.writeString(cityName)
		parcel.writeValue(cityId)
		parcel.writeLong(if (dateOfReg != null) dateOfReg!!.time else -1)
		parcel.writeLong(if (dateOfLastAction != null) dateOfLastAction!!.time else -1)
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
		parcel.writeLong(if (dateOfBlock != null) dateOfBlock!!.time else -1)
		parcel.writeLong(if (dateOfBlockEnd != null) dateOfBlockEnd!!.time else -1)
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
