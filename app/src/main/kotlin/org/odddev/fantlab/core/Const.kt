package org.odddev.fantlab.core

class Const {

	companion object {
		val BASE_URL = "https://api.fantlab.ru"
		val IMAGES_URL = "https://fantlab.ru/images"

		@JvmStatic
		fun getImagesUrl() = IMAGES_URL
	}
}