package ru.fantlab.android.data.dao.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.requery.Column
import io.requery.Entity
import io.requery.Key
import io.requery.Table
import ru.fantlab.android.App
import ru.fantlab.android.helper.single
import timber.log.Timber

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

fun List<AuthorInList>.save(): Disposable {
	return Single.fromPublisher<String> { s ->
		try {
			val dataSource = App.dataStore.toBlocking()
			if (!this.isEmpty()) {
				for (author in this) {
					dataSource.delete(AuthorInList::class.java)
							.where(AuthorInList.ID.eq(author.id))
							.get()
							.value()
					dataSource.insert(author)
				}
			}
			s.onNext("")
		} catch (e: Exception) {
			s.onError(e)
		}
		s.onComplete()
	}.single().subscribe({ }, Timber::e)
}

fun getAuthorsList(): Single<List<AuthorInList>> {
	return App.dataStore
			.select(AuthorInList::class.java)
			.orderBy(AuthorInList.NAME_SHORT.asc())
			.get()
			.observable()
			.toList()
			.single()
}
