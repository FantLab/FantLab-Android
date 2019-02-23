package ru.fantlab.android.data.dao

import android.content.Context
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.data.dao.model.Pubnews
import ru.fantlab.android.data.dao.model.Pubplans

object ContextMenuBuilder {

	fun buildForMarks(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.change_mark), R.drawable.ic_star, "revote"),
						ContextMenus.MenuItem(context.getString(R.string.delete_mark), R.drawable.ic_delete, "delete")
				)
		items.add(ContextMenus(context.getString(R.string.select_action), actions, "main"))

		return items
	}

	fun buildForResponses(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.vote), R.drawable.ic_star, "votes", true)
				)
		items.add(ContextMenus(context.getString(R.string.select_action), actions, "main"))

		val marks =
				arrayListOf(
						ContextMenus.MenuItem("+1", R.drawable.ic_thumb_up, "vote"),
						ContextMenus.MenuItem("-1", R.drawable.ic_thumb_down, "vote")
				)
		items.add(ContextMenus(context.getString(R.string.select_mark), marks, "votes"))

		return items
	}

	fun buildForResponse(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem("+1", R.drawable.ic_thumb_up, "vote"),
						ContextMenus.MenuItem("-1", R.drawable.ic_thumb_down, "vote")
				)
		items.add(ContextMenus(context.getString(R.string.select_mark), actions, "votes"))

		return items
	}

	fun buildForProfile(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.show_profile), R.drawable.ic_profile, "profile"),
						ContextMenus.MenuItem(context.getString(R.string.send_message), R.drawable.ic_message, "message", true)
				)
		items.add(ContextMenus(context.getString(R.string.select_action), actions, "main"))

		return items
	}

	fun buildForResponseSorting(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_date), R.drawable.ic_time_small, "BY_DATE"),
						ContextMenus.MenuItem(context.getString(R.string.sort_rating), R.drawable.ic_thumb_up_small, "BY_RATING"),
						ContextMenus.MenuItem(context.getString(R.string.sort_mark), R.drawable.ic_star_small, "BY_MARK")
				)
		items.add(ContextMenus(context.getString(R.string.select_sort), actions, "main"))

		return items
	}

	fun buildForAwardsSorting(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_name), R.drawable.ic_title, "BY_NAME"),
						ContextMenus.MenuItem(context.getString(R.string.sort_country), R.drawable.ic_location, "BY_COUNTRY"),
						ContextMenus.MenuItem(context.getString(R.string.sort_type), R.drawable.ic_type_small, "BY_TYPE"),
						ContextMenus.MenuItem(context.getString(R.string.sort_lang), R.drawable.ic_language, "BY_LANG")
				)
		items.add(ContextMenus(context.getString(R.string.select_sort), actions, "main"))

		return items
	}

	fun buildForMarksCharts(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.markchart_1), R.drawable.ic_chart, "chart"),
						ContextMenus.MenuItem(context.getString(R.string.markchart_2), R.drawable.ic_chart, "chart")
				)
		items.add(ContextMenus(context.getString(R.string.select_chart), actions, "main"))

		return items
	}

	fun buildForAuthorsSorting(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions = arrayListOf<ContextMenus.MenuItem>()
		val alphabetIdsArray = context.resources.getStringArray(R.array.alphabet_array_id)
		context.resources.getStringArray(R.array.alphabet_array).mapIndexed { index, title ->
			actions.add(ContextMenus.MenuItem(title, null, alphabetIdsArray[index]))
		}

		items.add(ContextMenus(context.getString(R.string.alphabet_authors), actions, "main"))

		return items
	}

	fun buildForPublishersSorting(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_name), R.drawable.ic_title, "sort"),
						ContextMenus.MenuItem(context.getString(R.string.sort_editions_count), R.drawable.ic_edition, "sort"),
						ContextMenus.MenuItem(context.getString(R.string.sort_country), R.drawable.ic_location, "sort"),
						ContextMenus.MenuItem(context.getString(R.string.sort_city), R.drawable.ic_home, "sort")
				)
		items.add(ContextMenus(context.getString(R.string.select_sort), actions, "sort"))

		return items
	}

	fun buildForPublishersFilter(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_country), R.drawable.ic_location, "countries"),
						ContextMenus.MenuItem(context.getString(R.string.sort_category), R.drawable.ic_type_small, "category")
				)
		items.add(ContextMenus(context.getString(R.string.select_filter), actions, "filter"))

		val countries = ArrayList<ContextMenus.MenuItem>()
		val countriesIdsArray = context.resources.getStringArray(R.array.countries_array_id)
		context.resources.getStringArray(R.array.countries_array).mapIndexed { index, title ->
			countries.add(ContextMenus.MenuItem(title, null, countriesIdsArray[index]))
		}

		val categories = ArrayList<ContextMenus.MenuItem>()
		val categoriesIdsArray = context.resources.getStringArray(R.array.categories_array_id)
		context.resources.getStringArray(R.array.categories_array).mapIndexed { index, title ->
			categories.add(ContextMenus.MenuItem(title, null, categoriesIdsArray[index]))
		}

		items.add(ContextMenus(context.getString(R.string.select_country), countries, "countries"))
		items.add(ContextMenus(context.getString(R.string.select_category), categories, "category"))

		return items
	}

	fun buildForPubnewsSorting(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_date), R.drawable.ic_time_small, "sort"),
						ContextMenus.MenuItem(context.getString(R.string.sort_type), R.drawable.ic_type_small, "sort"),
						ContextMenus.MenuItem(context.getString(R.string.sort_publisher), R.drawable.ic_publishers, "sort"),
						ContextMenus.MenuItem(context.getString(R.string.sort_author), R.drawable.ic_person, "sort"),
						ContextMenus.MenuItem(context.getString(R.string.sort_name), R.drawable.ic_title, "sort")
				)
		items.add(ContextMenus(context.getString(R.string.select_sort), actions, "sort"))

		return items
	}

	fun buildForPubnewsFilter(context: Context, publishersList: List<Pubnews.Publisher>): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_lang), R.drawable.ic_language, "lang"),
						ContextMenus.MenuItem(context.getString(R.string.sort_publisher), R.drawable.ic_publishers, "publisher")
				)
		items.add(ContextMenus(context.getString(R.string.select_filter), actions, "filter"))

		val langs = ArrayList<ContextMenus.MenuItem>()
		val langIdsArray = context.resources.getStringArray(R.array.lang_array_id)
		context.resources.getStringArray(R.array.lang_array).mapIndexed { index, title ->
			langs.add(ContextMenus.MenuItem(title, null, langIdsArray[index]))
		}

		val categories = ArrayList<ContextMenus.MenuItem>()
		publishersList.mapIndexed { index, publisher ->
			categories.add(ContextMenus.MenuItem(publisher.name, null, publisher.publisherId))
		}

		items.add(ContextMenus(context.getString(R.string.select_country), langs, "lang"))
		items.add(ContextMenus(context.getString(R.string.select_category), categories, "publisher"))

		return items
	}

	fun buildForPubplansSorting(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_correct), R.drawable.ic_correct, "sort"),
						ContextMenus.MenuItem(context.getString(R.string.sort_date), R.drawable.ic_time_small, "sort"),
						ContextMenus.MenuItem(context.getString(R.string.sort_type), R.drawable.ic_type_small, "sort"),
						ContextMenus.MenuItem(context.getString(R.string.sort_publisher), R.drawable.ic_publishers, "sort"),
						ContextMenus.MenuItem(context.getString(R.string.sort_author), R.drawable.ic_person, "sort"),
						ContextMenus.MenuItem(context.getString(R.string.sort_name), R.drawable.ic_title, "sort")
				)
		items.add(ContextMenus(context.getString(R.string.select_sort), actions, "sort"))

		return items
	}

	fun buildForPubplansFilter(context: Context, publishersList: List<Pubplans.Publisher>): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_lang), R.drawable.ic_language, "lang"),
						ContextMenus.MenuItem(context.getString(R.string.sort_publisher), R.drawable.ic_publishers, "publisher")
				)
		items.add(ContextMenus(context.getString(R.string.select_filter), actions, "filter"))

		val langs = ArrayList<ContextMenus.MenuItem>()
		val langIdsArray = context.resources.getStringArray(R.array.lang_array_id)
		context.resources.getStringArray(R.array.lang_array).mapIndexed { index, title ->
			langs.add(ContextMenus.MenuItem(title, null, langIdsArray[index]))
		}

		val categories = ArrayList<ContextMenus.MenuItem>()
		publishersList.mapIndexed { index, publisher ->
			categories.add(ContextMenus.MenuItem(publisher.name, null, publisher.publisherId))
		}

		items.add(ContextMenus(context.getString(R.string.select_country), langs, "lang"))
		items.add(ContextMenus(context.getString(R.string.select_category), categories, "publisher"))

		return items
	}

	fun buildForAutplansSorting(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_correct), R.drawable.ic_correct, "sort"),
						ContextMenus.MenuItem(context.getString(R.string.sort_author), R.drawable.ic_person, "sort"),
						ContextMenus.MenuItem(context.getString(R.string.sort_name), R.drawable.ic_title, "sort")
				)
		items.add(ContextMenus(context.getString(R.string.select_sort), actions, "sort"))

		return items
	}

	fun buildForAutplansFilter(context: Context): ArrayList<ContextMenus> {
		val items = ArrayList<ContextMenus>()

		val actions =
				arrayListOf(
						ContextMenus.MenuItem(context.getString(R.string.sort_lang), R.drawable.ic_language, "lang")
				)
		items.add(ContextMenus(context.getString(R.string.select_filter), actions, "filter"))

		val langs = ArrayList<ContextMenus.MenuItem>()
		val langIdsArray = context.resources.getStringArray(R.array.lang_array_id)
		context.resources.getStringArray(R.array.lang_array).mapIndexed { index, title ->
			langs.add(ContextMenus.MenuItem(title, null, langIdsArray[index]))
		}

		items.add(ContextMenus(context.getString(R.string.select_country), langs, "lang"))

		return items
	}

}