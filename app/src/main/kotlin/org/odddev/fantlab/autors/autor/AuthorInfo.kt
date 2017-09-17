package org.odddev.fantlab.autors.autor

import android.support.annotation.Keep
import android.util.SparseArray
import org.odddev.fantlab.core.models.Author
import org.odddev.fantlab.core.models.Nomination
import org.odddev.fantlab.core.models.Work

@Keep
data class AuthorInfo(
		val author: Author,
		val nominations: List<Nomination>,
		val works: SparseArray<List<Work>>
)
