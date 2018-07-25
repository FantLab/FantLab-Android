package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Smile
import ru.fantlab.android.ui.widgets.ForegroundImageView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class SmileViewHolder private constructor(view: View, adapter: BaseRecyclerAdapter<*, *, *>)
    : BaseViewHolder<Smile>(view, adapter) {

    @BindView(R.id.smile) lateinit var smileView: ForegroundImageView

    override fun bind(smile: Smile) {
        Glide.with(itemView.context)
                .load("file:///android_asset/smiles/${smile.id}.gif")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(smileView)
    }

    companion object {
        fun newInstance(parent: ViewGroup, adapter: BaseRecyclerAdapter<*, *, *>): SmileViewHolder {
            return SmileViewHolder(getView(parent, R.layout.smile_row_item), adapter)
        }
    }
}