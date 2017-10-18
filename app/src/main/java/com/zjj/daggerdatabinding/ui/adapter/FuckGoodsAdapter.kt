package com.zjj.daggerdatabinding.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zjj.daggerdatabinding.base.BaseBindingAdapter
import com.zjj.daggerdatabinding.base.DataBoundViewHolder
import com.zjj.daggerdatabinding.bean.FuckGoods
import com.zjj.daggerdatabinding.databinding.ItemFuckgoodsBinding

/**
 * Created by zjj on 17/10/17.
 */
class FuckGoodsAdapter(private val mList: List<FuckGoods>) : BaseBindingAdapter<ItemFuckgoodsBinding>() {
    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<ItemFuckgoodsBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.fuckgoods = mList[position]
        holder.binding.executePendingBindings()
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataBoundViewHolder<ItemFuckgoodsBinding> {
        return DataBoundViewHolder(
                ItemFuckgoodsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
}