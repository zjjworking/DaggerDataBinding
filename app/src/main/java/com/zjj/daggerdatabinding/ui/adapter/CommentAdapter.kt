package com.zjj.daggerdatabinding.ui.adapter

import com.zjj.daggerdatabinding.base.BaseDataBindingAdapter
import com.zjj.daggerdatabinding.bean.VideoInfo
import com.zjj.daggerdatabinding.bean.VideoType
import com.zjj.daggerdatabinding.databinding.ItemCommentBinding

/**
 * Created by zjj on 17/10/26.
 */
class CommentAdapter(private val mList : List<VideoType>, layoutResId: Int) : BaseDataBindingAdapter<ItemCommentBinding, VideoType>(layoutResId,mList){
    override fun convert(binding: ItemCommentBinding, item: VideoType) {
        binding.data = item
    }
}