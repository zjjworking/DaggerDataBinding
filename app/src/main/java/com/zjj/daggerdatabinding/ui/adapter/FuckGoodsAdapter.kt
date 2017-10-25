package com.zjj.daggerdatabinding.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zjj.daggerdatabinding.base.BaseBindingAdapter
import com.zjj.daggerdatabinding.base.BaseDataBindingAdapter
import com.zjj.daggerdatabinding.base.DataBoundViewHolder
import com.zjj.daggerdatabinding.bean.FuckGoods
import com.zjj.daggerdatabinding.databinding.ItemFuckgoodsBinding

/**
 * Created by zjj on 17/10/17.
 */
class FuckGoodsAdapter(private val mList: List<FuckGoods>, layoutResId: Int) : BaseDataBindingAdapter<ItemFuckgoodsBinding,FuckGoods>(layoutResId,mList) {
    override fun convert(binding: ItemFuckgoodsBinding, item: FuckGoods) {
        binding.fuckgoods = item
    }
}