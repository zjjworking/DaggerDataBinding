package com.zjj.daggerdatabinding.ui.adapter

import android.view.ViewGroup
import com.zjj.daggerdatabinding.base.BaseDataBindingAdapter
import com.zjj.daggerdatabinding.base.DataBoundNewViewHolder
import com.zjj.daggerdatabinding.bean.VideoInfo
import com.zjj.daggerdatabinding.databinding.ItemRelatedBinding
import com.zjj.daggerdatabinding.databinding.ItemVideoBinding

/**
 * Created by zjj on 17/10/26.
 */
class RelatedAdapter(private val mList : List<VideoInfo>, layoutResId: Int) : BaseDataBindingAdapter<ItemRelatedBinding, VideoInfo>(layoutResId,mList){
    override fun convert(binding: ItemRelatedBinding, item: VideoInfo) {
        binding.data = item
    }
}