package com.zjj.daggerdatabinding.base

import android.databinding.ViewDataBinding
import android.view.View
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by zjj on 17/10/23.
 */
//class DataBoundNewViewHolder<T : ViewDataBinding>(val binding:T) : BaseViewHolder(binding.root) {
//}

class DataBoundNewViewHolder<Binding : ViewDataBinding>(view: View) : BaseViewHolder(view) {
    var binding: Binding? = null
}
