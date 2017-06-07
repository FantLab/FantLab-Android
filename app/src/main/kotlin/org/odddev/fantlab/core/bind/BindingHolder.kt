package org.odddev.fantlab.core.bind

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * @author Ivan Zolotarev
 * @since 05.06.17
 */
class BindingHolder<B : ViewDataBinding>(var binding: B) : RecyclerView.ViewHolder(binding.root)
