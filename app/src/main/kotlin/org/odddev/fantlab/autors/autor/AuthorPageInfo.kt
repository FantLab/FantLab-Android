package org.odddev.fantlab.autors.autor

import android.support.annotation.Keep
import android.util.SparseArray
import org.odddev.fantlab.core.models.*

@Keep
data class AuthorPageInfo(
		val authors: List<Author>,
		val laResume: List<LaResume>,
		val nominations: List<Nomination>,
		val pseudonyms: List<Pseudonym>,
		val sites: List<Site>,
		val works: SparseArray<List<Work>>,
		val workAuthors: List<WorkAuthor>
)
