package com.zjj.daggerdatabinding.module

import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Created by zjj on 17/10/17.
 */
@Module
class AppModule(private val context: Context){
    @Provides
    fun provideContext() = context
}