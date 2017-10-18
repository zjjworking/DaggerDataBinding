package com.zjj.daggerdatabinding.App

import android.content.Context
import android.widget.Toast

/**
 * Created by zjj on 17/10/17.
 */
fun Context.getMainComponent() = App.instance.apiComponent
fun Context.toast(msg:String,length:Int = Toast.LENGTH_SHORT){
    Toast.makeText(this,msg,length).show()
}