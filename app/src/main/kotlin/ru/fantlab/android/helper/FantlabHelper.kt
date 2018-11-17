package ru.fantlab.android.helper

object FantlabHelper {

	object User {
		val classRanges = arrayOf(200, 800, 2000, 4000, 7000, 10000, 15000, -1)
	}

	object Authors {
		val ignoreList = listOf(10, 12345, 2000, 7000, 46137)
	}

	enum class Levels(val level: Int) {
		NOVICE(0),
		ACTIVIST(200),
		AUTHORITY(800),
		PHILOSOPHER(2000),
		MASTER(4000),
		GRANDMASTER(7000),
		PEACEKEEPER(10000),
		PEACEMAKER(15000)
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

	val minLevelToVote = Levels.ACTIVIST.level

}