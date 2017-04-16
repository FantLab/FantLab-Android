package org.odddev.fantlab.autors

/**
 * @author kenrube
 * *
 * @since 16.04.17
 */

class AutorsResponse(val list: List<Autor>) {

    fun getAutorsList(): List<Autor> = list
}