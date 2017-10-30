package com.zjj.daggerdatabinding.bean

import android.text.TextUtils
import com.google.gson.annotations.SerializedName



/**
 * Created by zjj on 17/10/19.
 */
data class VideoRes(
        @SerializedName("list") var list: List<VideoType>,
    var title: String,
    var score: String,
    var videoType: String,
    var region: String,
    var airTime: String,
    var director: String,
    var actors: String,
    var pic: String,
    var description: String,
    var smoothURL: String,
    var SDURL: String,
    var HDURL: String)
{
    val videoUrl: String
        get() = if (!TextUtils.isEmpty(HDURL))
            HDURL
        else if (!TextUtils.isEmpty(SDURL))
            SDURL
        else if (!TextUtils.isEmpty(smoothURL))
            smoothURL
        else
            ""
}
