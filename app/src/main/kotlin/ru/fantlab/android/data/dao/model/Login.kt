package ru.fantlab.android.data.dao.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import io.requery.*
import ru.fantlab.android.App
import ru.fantlab.android.data.dao.model.LoginType.USER_ID
import ru.fantlab.android.helper.PrefGetter
import java.util.*

@Entity @Table(name = "login")
data class Login(
		@get:Column(name = "id") @get:Key var userId: Int,
		@get:Column var login: String,
		@get:Column var avatar: String,
		@get:Column var fio: String,
		@get:Column var sex: String,
		@SerializedName("birthday") @get:Column(name = "birthday") var birthDay: Date?,
		@get:Column(name = "class") var userClass: Int,
		@get:Column(name = "class_name") var className: String,
		@get:Column var level: Float,
		@get:Column var location: String?,
		@get:Column(name = "country_name") var countryName: String?,
		@get:Column(name = "country_id") var countryId: Int?,
		@get:Column(name = "city_name") var cityName: String?,
		@get:Column(name = "city_id") var cityId: Int?,
		@get:Column(name = "reg_date") var dateOfReg: Date,
		@get:Column(name = "last_action_date") var dateOfLastAction: Date,
		@get:Column(name = "timer") var userTimer: Long,
		@get:Column var sign: String?,
		@SerializedName("markcount") @get:Column(name = "mark_count") var markCount: Int,
		@SerializedName("responsecount") @get:Column(name = "response_count") var responseCount: Int,
		@SerializedName("descriptioncount") @get:Column(name = "description_count") var descriptionCount: Int,
		@SerializedName("classifcount") @get:Column(name = "classif_count") var classifCount: Int,
		@SerializedName("votecount") @get:Column(name = "vote_count") var voteCount: Int,
		@SerializedName("topiccount") @get:Column(name = "topic_count") var topicCount: Int,
		@SerializedName("messagecount") @get:Column(name = "message_count") var messageCount: Int,
		@get:Column(name = "bookcase_count") var bookcaseCount: Int,
		@SerializedName("curator_autors") @get:Column(name = "curator_authors") var curatorAuthors: Int,
		@get:Column(name = "ticket_count") var ticketsCount: Int,
		@SerializedName("autor_id") @get:Column(name = "author_id") var authorId: Int?,
		@SerializedName("autor_name") @get:Column(name = "author_name") var authorName: String?,
		@SerializedName("autor_name_orig") @get:Column(name = "author_name_orig") var authorNameOrig: String?,
		@SerializedName("autor_is_opened") @get:Column(name = "author_is_opened") var authorIsOpened: Int?,
		@get:Column(name = "blog_id") var blogId: Int?,
		@get:Column(name = "blocked") var block: Int,
		@get:Column(name = "block_start_date") var dateOfBlock: Date?,
		@get:Column(name = "block_end_date") var dateOfBlockEnd: Date?,
		@get:Column var token: String?
) : Persistable, Parcelable {

	constructor(parcel: Parcel) : this(
			parcel.readInt(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readLong().let { if (it == -1L) null else Date(it) },
			parcel.readInt(),
			parcel.readString(),
			parcel.readFloat(),
			parcel.readString(),
			parcel.readString(),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readString(),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			Date(parcel.readLong()),
			Date(parcel.readLong()),
			parcel.readLong(),
			parcel.readString(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readString(),
			parcel.readString(),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readInt(),
			parcel.readLong().let { if (it == -1L) null else Date(it) },
			parcel.readLong().let { if (it == -1L) null else Date(it) },
			parcel.readString())

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(userId)
		parcel.writeString(login)
		parcel.writeString(avatar)
		parcel.writeString(fio)
		parcel.writeString(sex)
		parcel.writeLong(birthDay?.time ?: -1L)
		parcel.writeInt(userClass)
		parcel.writeString(className)
		parcel.writeFloat(level)
		parcel.writeString(location)
		parcel.writeString(countryName)
		parcel.writeValue(countryId)
		parcel.writeString(cityName)
		parcel.writeValue(cityId)
		parcel.writeLong(dateOfReg.time)
		parcel.writeLong(dateOfLastAction.time)
		parcel.writeLong(userTimer)
		parcel.writeString(sign)
		parcel.writeInt(markCount)
		parcel.writeInt(responseCount)
		parcel.writeInt(descriptionCount)
		parcel.writeInt(classifCount)
		parcel.writeInt(voteCount)
		parcel.writeInt(topicCount)
		parcel.writeInt(messageCount)
		parcel.writeInt(bookcaseCount)
		parcel.writeInt(curatorAuthors)
		parcel.writeInt(ticketsCount)
		parcel.writeValue(authorId)
		parcel.writeString(authorName)
		parcel.writeString(authorNameOrig)
		parcel.writeValue(authorIsOpened)
		parcel.writeValue(blogId)
		parcel.writeInt(block)
		parcel.writeLong(dateOfBlock?.time ?: -1L)
		parcel.writeLong(dateOfBlockEnd?.time ?: -1L)
		parcel.writeString(token)
	}

	@Transient
	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Login> {
		override fun createFromParcel(parcel: Parcel): Login {
			return Login(parcel)
		}

		override fun newArray(size: Int): Array<Login?> {
			return arrayOfNulls(size)
		}
	}
}

fun Login.saveLoggedUser(): Observable<Boolean> {
	return Observable.fromPublisher { s ->
		this.token = PrefGetter.getToken()
		App.dataStore
				.toBlocking()
				.delete(Login::class.java)
				.where(USER_ID.eq(this.userId))
				.get()
				.value()
		App.dataStore
				.toBlocking()
				.insert(this)
		s.onNext(true)
		s.onComplete()
	}
}

fun getLoggedUser(): Login? {
	return App.dataStore
			.select(Login::class.java)
			.get()
			.firstOrNull()
}

fun logout() {
	val loggedUser = getLoggedUser() ?: return
	App.dataStore.toBlocking().delete(loggedUser)
}
