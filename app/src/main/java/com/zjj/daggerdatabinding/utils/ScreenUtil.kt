package com.zjj.daggerdatabinding.utils

import android.content.Context
import android.util.DisplayMetrics



/**
 * Created by zjj on 17/10/18.
 * 计算dp px
 */
object ScreenUtil {
    /**
     * dpתpx
     */
    fun dip2px(ctx: Context, dpValue: Float): Int {
        val scale = ctx.getResources().getDisplayMetrics().density
        return (dpValue * scale + 0.5f).toInt()
    }


    /**
     * pxתdp
     */
    fun px2dip(ctx: Context, pxValue: Float): Int {
        val scale = ctx.getResources().getDisplayMetrics().density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * screenWidth
     *
     * @param context
     * @return
     */
    fun getScreenWidth(context: Context): Int {
        val dm = context.getResources().getDisplayMetrics()
        return dm.widthPixels
    }

    /**
     * screenHeight
     *
     * @param context
     * @return
     */
    fun getScreenHeight(context: Context): Int {
        val dm = context.getResources().getDisplayMetrics()
        return dm.heightPixels
    }
}
