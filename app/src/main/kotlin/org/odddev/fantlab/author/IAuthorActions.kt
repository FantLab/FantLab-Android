package org.odddev.fantlab.author

interface IAuthorActions {

	fun showBiography()

	fun showAwards()

	fun showAward(award: AuthorFull.Award)

	fun showWorks()

	fun showWork(work: AuthorFull.Work)
}