package ru.fantlab.android.old.author

import io.reactivex.Flowable
import ru.fantlab.android.old.author.models.Author

interface IAuthorProvider {

	fun getAuthor(id: Int): Flowable<Author>
}
