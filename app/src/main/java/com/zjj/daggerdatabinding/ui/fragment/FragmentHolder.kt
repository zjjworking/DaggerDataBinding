package com.zjj.daggerdatabinding.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zjj.daggerdatabinding.base.BaseBindingFragment
import com.zjj.daggerdatabinding.databinding.FragmentAboutBinding

/**
 * Created by zjj on 17/10/18.
 */
class FragmentHolder : BaseBindingFragment<FragmentAboutBinding>() {
    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentAboutBinding {
        return FragmentAboutBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        val text = "Kotlin \n"
                .plus("Dagger 2\n")
                .plus("Rxjava\n")
                .plus("Retrofit 2\n")
                .plus("OkHttp 3\n")
                .plus("DataBinding\n")
                .plus("DeepLinkDispatch\n")
                .plus("Gson\n")
                .plus("Glide")
        mBinding?.tvThank?.text = text

    }


}