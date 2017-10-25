package com.zjj.daggerdatabinding.router

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.ImageView
import com.zjj.daggerdatabinding.ui.activity.ImageActivity
import com.zjj.daggerdatabinding.ui.MainActivity



/**
 * Created by zjj on 17/10/19.
 */
 object JumpUtil{
    /**
     * 点击大图
     */
    fun startIMGActivity(context: Context, imageView: ImageView, url : String){
        val intent = Intent(context, ImageActivity :: class.java)
        intent.putExtra(ClientUri.IMG,url)
        if(Build.VERSION.SDK_INT > 21) {
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity, imageView, "img").toBundle())
        }else{
            context.startActivity(intent)
        }
    }
}

