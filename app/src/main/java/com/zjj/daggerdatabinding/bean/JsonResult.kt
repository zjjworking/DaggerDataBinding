package com.zjj.daggerdatabinding.bean

/**
 * Created by zjj on 17/10/17.
 */
class JsonResult<T>(val error :Boolean,
                    val code :Int,
                    val msg : String,
                    val results:T){
}