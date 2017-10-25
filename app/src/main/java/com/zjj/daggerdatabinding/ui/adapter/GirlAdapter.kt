package com.zjj.daggerdatabinding.ui.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.zjj.daggerdatabinding.BR
import com.zjj.daggerdatabinding.base.BaseDataBindingAdapter
import com.zjj.daggerdatabinding.base.DataBoundNewViewHolder
import com.zjj.daggerdatabinding.bean.FuckGoods
import com.zjj.daggerdatabinding.databinding.ItemGirlBinding
import com.zjj.daggerdatabinding.router.JumpUtil

/**
 * Created by zjj on 17/10/18.
 */
class GirlAdapter(private val mList: List<FuckGoods>, layoutResId: Int) : BaseDataBindingAdapter<ItemGirlBinding,FuckGoods>(layoutResId,mList) {

    override fun convert(binding: ItemGirlBinding, item: FuckGoods) {
        binding.girl = item
        binding.ivGirl.setOnClickListener { view->
            val imageView = view as ImageView
            JumpUtil.startIMGActivity(view.context,imageView,item.url)
        }
    }
}