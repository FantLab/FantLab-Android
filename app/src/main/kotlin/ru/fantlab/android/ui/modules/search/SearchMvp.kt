package ru.fantlab.android.ui.modules.search

import android.support.annotation.IntRange
import android.support.v4.view.ViewPager
import android.widget.AutoCompleteTextView
import ru.fantlab.android.data.dao.model.SearchHistory
import ru.fantlab.android.ui.base.mvp.BaseMvp
import java.util.*

/**
 * Created by Kosh on 08 Dec 2016, 8:19 PM
 */

interface SearchMvp {

	interface View : BaseMvp.View {

		fun onNotifyAdapter(query: SearchHistory?)

		fun onSetCount(count: Int, @IntRange(from = 0, to = 3) index: Int)
	}

	interface Presenter : BaseMvp.Presenter {

		fun getHints(): ArrayList<SearchHistory>

		fun onSearchClicked(viewPager: ViewPager, editText: AutoCompleteTextView)
	}
}
