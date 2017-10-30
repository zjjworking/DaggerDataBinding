package com.zjj.daggerdatabinding.component

import com.zjj.daggerdatabinding.mvp.contract.RandomContract
import com.zjj.daggerdatabinding.ui.MainActivity
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by zjj on 17/10/18.
 */
@Subcomponent(modules = arrayOf(RandomModule::class))
interface RandomComponent {
    fun inject(activity: MainActivity)
}

@Module
class RandomModule(private val mView: RandomContract.View) {
    @Provides
    fun getView() = mView
}