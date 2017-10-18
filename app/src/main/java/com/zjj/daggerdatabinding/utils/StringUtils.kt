package com.zjj.daggerdatabinding.utils

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import android.text.TextUtils
import java.util.*


/**
 * Created by zjj on 17/10/18.
 */
object StringUtils {
    /**
     * 去掉特殊字符
     *
     * @param s
     * @return
     */
    fun removeOtherCode(s: String): String {
        var s = s
        if (TextUtils.isEmpty(s))
            return ""
        s = s.replace("\\<.*?>|\\n".toRegex(), "")
        return s
    }

    /**
     * 判断非空
     *
     * @param str
     * @return
     */
    fun isEmpty(str: String): String {
        return if (TextUtils.isEmpty(str)) "" else str
    }

    /**
     * 根据Url获取catalogId
     *
     * @param url
     * @return
     */
    fun getCatalogId(url: String): String {
        var catalogId = ""
        if (!TextUtils.isEmpty(url) && url.contains("="))
            catalogId = url.substring(url.lastIndexOf("=") + 1)
        return catalogId
    }

    fun getRandomNumber(min: Int, max: Int): Int {
        return Random().nextInt(max) % (max - min + 1) + min
    }

    fun getErrorMsg(msg: String): String {
        var msg = msg
        if (msg.contains("*")) {
            msg = msg.substring(msg.indexOf("*") + 1)
            return msg
        } else
            return ""
    }

//    fun setIconDrawable(mContext: Context, view: TextView, icon: IIcon, size: Int, padding: Int) {
//        view.setCompoundDrawablesWithIntrinsicBounds(IconicsDrawable(mContext)
//                .icon(icon)
//                .color(Color.WHITE)
//                .sizeDp(size), null, null, null)
//        view.compoundDrawablePadding = ScreenUtil.dip2px(mContext, padding)
//    }
}
