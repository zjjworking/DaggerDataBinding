package com.zjj.daggerdatabinding.component

import com.zjj.daggerdatabinding.App.App
import com.zjj.daggerdatabinding.mvp.contract.VideoInfoContract
import com.zjj.daggerdatabinding.mvp.model.VideoInfoModel
import com.zjj.daggerdatabinding.ui.activity.VideoInfoActivity
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Singleton

/**
 * Created by zjj on 17/10/22.
 */
@Subcomponent(modules = arrayOf(VideoInfoModule::class))
interface VideoInfoComponent {
    fun inject(activity: VideoInfoActivity)
}

@Module
class VideoInfoModule(private val mView: VideoInfoContract.View) {
    @Provides
    fun getView() = mView
}