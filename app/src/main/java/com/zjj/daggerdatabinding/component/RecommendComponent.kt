package com.zjj.daggerdatabinding.component

import com.zjj.daggerdatabinding.mvp.contract.RecommendContract
import com.zjj.daggerdatabinding.ui.fragment.RecommendFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by zjj on 17/10/20.
 */
@Subcomponent(modules = arrayOf(RecommendModule::class))
interface RecommendComponent {
    fun inject(fragment: RecommendFragment)
}

@Module
class RecommendModule(private val mView: RecommendContract.View) {
    @Provides
    fun getView() = mView
}