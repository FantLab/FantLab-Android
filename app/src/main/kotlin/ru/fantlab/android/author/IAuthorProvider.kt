package ru.fantlab.android.author

import io.reactivex.Flowable
import ru.fantlab.android.author.models.Author

interface IAuthorProvider {

	fun getAuthor(id: Int): Flowable<Author>
}
