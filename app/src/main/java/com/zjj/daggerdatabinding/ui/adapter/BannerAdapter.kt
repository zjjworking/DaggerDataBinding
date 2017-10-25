package com.zjj.daggerdatabinding.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.zjj.daggerdatabinding.bean.VideoInfo
import com.jude.rollviewpager.adapter.StaticPagerAdapter
import com.zjj.daggerdatabinding.R
import com.zjj.daggerdatabinding.utils.ImageLoader

/**
 * Created by zjj on 17/10/20.
 */
class BannerAdapter(private val ctx: Context, private val list: MutableList<VideoInfo>) : StaticPagerAdapter() {

    init {
        removeEmpty(this.list)
    }

    private fun removeEmpty(list: MutableList<VideoInfo>) {
        var i = 0
        while (i < list.size) {
            if (list[i].loadType != "video") {
                list.removeAt(i)
                i--
            }
            i++
        }
    }

    override fun getView(container: ViewGroup, position: Int): View {
        val imageView = ImageView(ctx)
        imageView.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        imageView.setScaleType(ImageView.ScaleType.FIT_XY)
        imageView.setBackgroundResource(R.mipmap.default_320)
        //加载图片
        ImageLoader.load(imageView,list[position].pic)
        //点击事件
        imageView.setOnClickListener( { view->
        })
        return imageView
    }

    override fun getCount(): Int {
        return list.size
    }
}