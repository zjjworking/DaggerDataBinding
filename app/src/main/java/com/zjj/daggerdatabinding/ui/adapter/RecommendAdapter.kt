package com.zjj.daggerdatabinding.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zjj.daggerdatabinding.base.*
import com.zjj.daggerdatabinding.bean.VideoInfo
import com.zjj.daggerdatabinding.databinding.ItemVideoBinding

/**
 * Created by zjj on 17/10/20.
 */
class RecommendAdapter(private val mList : List<VideoInfo>, layoutResId: Int) : BaseDataBindingAdapter<ItemVideoBinding, VideoInfo>(layoutResId,mList){
    override fun convert(binding: ItemVideoBinding, item: VideoInfo) {
        binding.videoInfo = item
    }

    override fun onCreateDefViewHolder(parent: ViewGroup?, viewType: Int): DataBoundNewViewHolder<ItemVideoBinding> {
        return super.onCreateDefViewHolder(parent, viewType)
    }
}