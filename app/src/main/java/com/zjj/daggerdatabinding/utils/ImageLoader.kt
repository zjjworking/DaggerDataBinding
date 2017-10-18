package com.zjj.daggerdatabinding.utils

import android.app.Activity
import android.content.Context
import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import jp.wasabeef.glide.transformations.CropCircleTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Created by zjj on 17/10/17.
 * 图片加载
 */
object ImageLoader {
//    @BindingAdapter("load_image")
    fun load(iv: ImageView , url: String) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
        Glide.with(iv.context).load(url).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv)
    }

    fun load(activity: Activity, url: String, iv: ImageView) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
        if (!activity.isDestroyed) {
            Glide.with(activity).load(url).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv)
        }
    }

    fun loadAll(context: Context, url: String, iv: ImageView) {    //缓存剪裁后的图片
        Glide.with(context).load(url).crossFade().skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.RESULT).into(iv)
    }

    fun loadAll(activity: Activity, url: String, iv: ImageView) {    //不缓存，全部从网络加载
        if (!activity.isDestroyed) {
            Glide.with(activity).load(url).crossFade().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv)
        }
    }

    //圆形裁剪
    fun loadCircle(imageView: ImageView,url: String?) =
            Glide.with(imageView.context).load(url).crossFade()
                    .bitmapTransform(CropCircleTransformation(imageView.context))
                    .into(imageView)
    //圆角
    fun loadCaryscale(imageView: ImageView,url: String?,radius : Int,margin : Int) =
            Glide.with(imageView.context)
                    .load(url)
                    .bitmapTransform(RoundedCornersTransformation(imageView.context,radius,margin, RoundedCornersTransformation.CornerType.ALL))
    .into(imageView);
}
