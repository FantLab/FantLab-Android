package ru.fantlab.android.helper

object FantlabHelper {

	object User {
		val classRanges = arrayOf(200, 800, 2000, 4000, 7000, 10000, 15000, -1)
	}

	object Authors {
		val ignoreList = listOf(10, 100, 12345, 2000, 7000, 46137)
	}

	object Attaches {
		val MaxAttachCountPerMessage = 10
	}

	enum class Levels(val `class`: Int) {
		NOVICE(0),
		ACTIVIST(1),
		AUTHORITY(2),
		PHILOSOPHER(3),
		MASTER(4),
		GRANDMASTER(5),
		PEACEKEEPER(6),
		PEACEMAKER(7)
	}

	enum class WorkType(val id: Int) {
		NOVEL(1),
		SHORTSTORY(45),
		CYCLE(4)
	}

	enum class ClassificatorTypes(val tag: String) {
		TYPE_GENRE("genres"),
		TYPE_AGE("age"),
		TYPE_CHR("characteristics"),
		TYPE_LINE("linearity"),
		TYPE_LOCATE("locate"),
		TYPE_STORY("story_moves"),
		TYPE_TIME("time")
	}

	data class PublishersSort<A, B, C>(
			var sortBy: A,
			var filterCountry: B,
			var filterCategory: C
	)

	data class PubnewsSort<A, B, C>(
			var sortBy: A,
			var filterLang: B,
			var filterPublisher: C
	)

	data class PubplansSort<A, B, C>(
			var sortBy: A,
			var filterLang: B,
			var filterPublisher: C
	)

	data class AutPlansSort<A, B>(
			var sortBy: A,
			var filterLang: B
	)

	data class ProfileMarksSort<A, B>(
			var sortBy: A,
			var filterCategory: B
	)

	fun classToName(`class`: String): String? {
		return 	when (`class`) {
			"USERCLASS_BEGINNER"  -> "Новичок"
			"USERCLASS_ACTIVIST" -> "Активист"
			"USERCLASS_AUTHORITY" -> "Авторитет"
			"USERCLASS_PHILOSOPHER" -> "Философ"
			"USERCLASS_MASTER" -> "Магистр"
			"USERCLASS_GRANDMASTER" -> "Гранд-мастер"
			"USERCLASS_PEACEKEEPER" -> "Миродержец"
			"USERCLASS_PEACEMAKER" -> "Миротворец"
			else -> this.toString()
		}
	}

	var currentUserId: Int = -1

	val minLevelToVote = FantlabHelper.User.classRanges[Levels.NOVICE.`class`]

	var classNeededToSet = listOf(0, 2, 3, 6)

}