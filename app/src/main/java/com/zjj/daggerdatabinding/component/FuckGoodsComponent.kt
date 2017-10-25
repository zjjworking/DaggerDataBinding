package com.zjj.daggerdatabinding.component

import com.zjj.daggerdatabinding.contract.FuckGoodsContract
import com.zjj.daggerdatabinding.ui.fragment.AndroidFragment
import com.zjj.daggerdatabinding.ui.fragment.GirlFragment
import com.zjj.daggerdatabinding.ui.fragment.IOSFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by zjj on 17/10/17.
 * 这里将Contract关联 ,注册页面
 */
@Subcomponent(modules = arrayOf(FuckGoodsModule::class))
interface FuckGoodsComponent {
    fun inject(fragment: AndroidFragment)
    fun inject(fragment: IOSFragment)

    fun inject(fragment: GirlFragment)

}

@Module
class FuckGoodsModule(private val mView: FuckGoodsContract.View){
    @Provides
    fun getView() = mView
}