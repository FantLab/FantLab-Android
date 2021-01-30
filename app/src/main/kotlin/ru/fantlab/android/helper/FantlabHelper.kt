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
		WORK_TYPE_NOVEL(1),
		WORK_TYPE_SHORTSTORY(45),
		WORK_TYPE_CYCLE(4)
		/*WORK_TYPE_UNKNOWN(0),
		WORK_TYPE_NOVEL(1),
		WORK_TYPE_COMPILATION(2),
		WORK_TYPE_SERIES(3),
		WORK_TYPE_VERSE(4),
		WORK_TYPE_OTHER(5),
		WORK_TYPE_FAIRY_TALE(6),
		WORK_TYPE_ESSAY(7),
		WORK_TYPE_ARTICLE(8),
		WORK_TYPE_EPIC_NOVEL(9),
		WORK_TYPE_ANTHOLOGY(10),
		WORK_TYPE_PLAY(11),
		WORK_TYPE_SCREENPLAY(12),
		WORK_TYPE_DOCUMENTARY(13),
		WORK_TYPE_MICROTALE(14),
		WORK_TYPE_DISSERTATION(15),
		WORK_TYPE_MONOGRAPH(16),
		WORK_TYPE_EDUCATIONAL_PUBLICATION(17),
		WORK_TYPE_ENCYCLOPEDIA(18),
		WORK_TYPE_MAGAZINE(19),
		WORK_TYPE_POEM(20),
		WORK_TYPE_POETRY(21),
		WORK_TYPE_PROSE_VERSE(22),
		WORK_TYPE_COMIC_BOOK(23),
		WORK_TYPE_MANGA(24),
		WORK_TYPE_GRAPHIC_NOVEL(25),
		WORK_TYPE_NOVELETTE(26),
		WORK_TYPE_STORY(27),
		WORK_TYPE_FEATURE_ARTICLE(28),
		WORK_TYPE_REPORTAGE(29),
		WORK_TYPE_CONDITIONAL_SERIES(30),
		WORK_TYPE_EXCERPT(31),
		WORK_TYPE_INTERVIEW(32),
		WORK_TYPE_REVIEW(33),
		WORK_TYPE_POPULAR_SCIENCE_BOOK(34),
		WORK_TYPE_ARTBOOK(35),
		WORK_TYPE_LIBRETTO(36)*/
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