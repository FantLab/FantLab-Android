package org.odddev.fantlab.autors.autor

import android.support.annotation.Keep
import org.odddev.fantlab.core.models.Author
import org.odddev.fantlab.core.models.AuthorAward
import org.odddev.fantlab.core.models.Work

@Keep
data class AuthorInfo(
		val author: Author,
		val works: List<Work>,
		val awards: List<AuthorAward>
)