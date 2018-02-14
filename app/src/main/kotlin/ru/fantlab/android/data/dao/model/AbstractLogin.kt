package ru.fantlab.android.data.dao.model

import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import io.requery.Column
import io.requery.Entity
import ru.fantlab.android.App
import ru.fantlab.android.helper.PrefGetter
import java.util.*

@Entity
abstract class AbstractLogin {

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

	@JvmField
	@Column
	var token: String? = null

	companion object {

		fun getUser(): Login? {
			return App.dataStore
					.select(Login::class.java)
					.get()
					.firstOrNull()
		}

		fun logout() {
			val login = getUser() ?: return
			App.dataStore.toBlocking().delete(login)
		}

		fun saveLogin(userModel: Login): Observable<Boolean> {
			return Observable.fromPublisher { s ->
				userModel.token = PrefGetter.getToken()
				App.dataStore
						.toBlocking()
						.delete(Login::class.java)
						.where(Login.USER_ID.eq(userModel.userId))
						.get()
						.value()
				App.dataStore
						.toBlocking()
						.insert(userModel)
				s.onNext(true)
				s.onComplete()
			}
		}
	}
}