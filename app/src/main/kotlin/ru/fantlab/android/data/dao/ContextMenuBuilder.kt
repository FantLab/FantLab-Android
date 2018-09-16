package ru.fantlab.android.data.dao

import android.content.Context
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ContextMenus

class ContextMenuBuilder{
	companion object {

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

		fun buildForResponses(context: Context): ArrayList<ContextMenus>{
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

		fun buildForProfile(context: Context): ArrayList<ContextMenus>{
			val items = ArrayList<ContextMenus>()

			val actions =
					arrayListOf(
							ContextMenus.MenuItem(context.getString(R.string.show_profile), R.drawable.ic_profile, "profile"),
							ContextMenus.MenuItem(context.getString(R.string.send_message), R.drawable.ic_message, "message", true)
					)
			items.add(ContextMenus(context.getString(R.string.select_action), actions, "main"))

			return items
		}

		fun buildForResponseSorting(context: Context): ArrayList<ContextMenus>{
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

		fun buildForAwardsSorting(context: Context): ArrayList<ContextMenus>{
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

	}
}