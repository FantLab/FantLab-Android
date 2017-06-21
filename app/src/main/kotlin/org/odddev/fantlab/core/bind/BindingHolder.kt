package org.odddev.fantlab.core.bind

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

class BindingHolder<B : ViewDataBinding>(var binding: B) : RecyclerView.ViewHolder(binding.root)
