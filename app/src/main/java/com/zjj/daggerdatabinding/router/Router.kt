package com.zjj.daggerdatabinding.router

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Created by zjj on 17/10/19.
 */
object Router{
    fun router(context: Context ,uri : String){
        val intent = Intent()
        intent.data = Uri.parse(uri)
        intent.action = Intent.ACTION_VIEW
        context.startActivity(intent)
    }
}