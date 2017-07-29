package org.odddev.fantlab.autors.autor

interface IAutorActions {

	fun showBiography()

	fun showAwards()

	fun showAward(award: AutorFull.Award)

	fun showWorks()

	fun showWork(work: AutorFull.Work)
}