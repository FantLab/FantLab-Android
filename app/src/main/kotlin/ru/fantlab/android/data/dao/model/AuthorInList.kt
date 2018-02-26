package ru.fantlab.android.data.dao.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.requery.*
import ru.fantlab.android.App
import ru.fantlab.android.data.dao.model.AuthorInListType.ID
import ru.fantlab.android.data.dao.model.AuthorInListType.NAME_SHORT
import ru.fantlab.android.helper.single
import timber.log.Timber

@Entity @Table(name = "author_in_list")
data class AuthorInList(
		@SerializedName("autor_id") @get:Column(name = "author_id") @get:Key var id: Int,
		@get:Column var name: String,
		@get:Column var nameOrig: String,
		@get:Column var nameShort: String
) : Persistable, Parcelable {

	constructor(parcel: Parcel) : this(
			parcel.readInt(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString())

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(id)
		parcel.writeString(name)
		parcel.writeString(nameOrig)
		parcel.writeString(nameShort)
	}

	@Transient
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
							.where(ID.eq(author.id))
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
			.orderBy(NAME_SHORT.asc())
			.get()
			.observable()
			.toList()
			.single()
}
