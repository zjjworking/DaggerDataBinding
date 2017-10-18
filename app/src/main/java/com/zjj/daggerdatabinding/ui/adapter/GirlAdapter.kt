package com.zjj.daggerdatabinding.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zjj.daggerdatabinding.base.BaseBindingAdapter
import com.zjj.daggerdatabinding.base.DataBoundViewHolder
import com.zjj.daggerdatabinding.bean.FuckGoods
import com.zjj.daggerdatabinding.databinding.ItemGirlBinding

/**
 * Created by zjj on 17/10/18.
 */
class GirlAdapter(private val mList: List<FuckGoods>) : BaseBindingAdapter<ItemGirlBinding>() {
    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<ItemGirlBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.girl = mList[holder.adapterPosition]


        holder.binding.executePendingBindings()
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataBoundViewHolder<ItemGirlBinding> {
        return DataBoundViewHolder(
                ItemGirlBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


}