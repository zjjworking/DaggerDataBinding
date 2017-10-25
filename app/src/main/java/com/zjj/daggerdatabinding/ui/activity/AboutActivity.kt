package com.zjj.daggerdatabinding.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.airbnb.deeplinkdispatch.DeepLink
import com.zjj.daggerdatabinding.R
import com.zjj.daggerdatabinding.router.ClientUri

/**
 * Created by zjj on 17/10/19.
 */
@DeepLink(ClientUri.ABOUT)
class AboutActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

    }
}