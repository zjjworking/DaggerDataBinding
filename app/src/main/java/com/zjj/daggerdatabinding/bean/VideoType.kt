package com.zjj.daggerdatabinding.bean

import com.google.gson.annotations.SerializedName



/**
 * Created by zjj on 17/10/19.
 */
data class VideoType (
    var title: String,
    var moreURL: String,
    var pic: String,
    var dataId: String,
    var airTime: String,
    var score: String,
    var description: String,
    var msg: String,
    var phoneNumber: String,
    var userPic: String,
    var time: String,
    var likeNum: String,
    @SerializedName("childList")
    var childList: MutableList<VideoInfo>
    ){
}