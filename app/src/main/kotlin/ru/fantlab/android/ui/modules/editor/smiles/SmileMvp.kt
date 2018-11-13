package ru.fantlab.android.ui.modules.editor.smiles

import ru.fantlab.android.data.dao.model.Smile
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface SmileMvp {

	interface View : BaseMvp.View, BaseViewHolder.OnItemClickListener<Smile> {
		fun clearAdapter()
		fun onAddSmile(smile: Smile)
	}

	interface Presenter {
		fun onLoadSmile()
	}

	interface SmileCallback {
		fun onSmileAdded(smile: Smile?)
	}
}