package com.zjj.daggerdatabinding.bean

import android.graphics.drawable.Drawable
import java.io.Serializable

/**
 * Created by zjj on 17/10/17.
 */
data class AppInfo(
        val appClass: String,
        val appIcon : Drawable,
        val appLable : String,
        val appPackage : String
) : Serializable{
    companion object {
        val serialVersionUID = 1L
    }
}