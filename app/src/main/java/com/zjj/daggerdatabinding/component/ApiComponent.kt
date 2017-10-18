package com.zjj.daggerdatabinding.component

import com.zjj.daggerdatabinding.App.App
import com.zjj.daggerdatabinding.module.ApiModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by zjj on 17/10/17.
 * 所有注册方法 在这里添加
 */
@Singleton
@Component(modules = arrayOf(ApiModule::class))
interface ApiComponent{

    fun inject(app: App)


    fun plus(module: FuckGoodsModule):FuckGoodsComponent
    fun plus(module: RandomModule):RandomComponent
}