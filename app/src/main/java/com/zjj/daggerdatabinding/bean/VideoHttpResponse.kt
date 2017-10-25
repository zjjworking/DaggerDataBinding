package com.zjj.daggerdatabinding.bean

/**
 * Created by zjj on 17/10/24.
 */
data class VideoHttpResponse<T>(val error :Boolean,
                           val code :Int,
                           val msg : String,
                           val ret:T) {

}