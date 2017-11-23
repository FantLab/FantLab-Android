package org.odddev.fantlab.authors

import org.odddev.fantlab.author.models.Author

interface IAuthorsActions {

	fun onAuthorClicked(author: Author)
}