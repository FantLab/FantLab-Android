package org.odddev.fantlab.home

/**
 * @author Ivan Zolotarev
 * @since 27.05.17
 */
interface IActionsHandler {

	fun openAutor(id: Int, name: String)

	fun showBiography(bio: String)
}