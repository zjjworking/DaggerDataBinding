package com.zjj.daggerdatabinding.module

import android.content.Context
import dagger.Module
import dagger.Provides
import android.content.SharedPreferences
import javax.inject.Singleton
import android.preference.PreferenceManager
import javax.inject.Named


/**
 * Created by zjj on 17/10/17.
 */
@Module
class AppModule(private val context: Context){
    @Provides
    fun provideContext() = context

    @Singleton
    @Provides
    @Named("default")
    fun providerDefaultSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Singleton
    @Provides
    @Named("encode")
    fun providerEncodeSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences("encode", Context.MODE_PRIVATE)
    }

}