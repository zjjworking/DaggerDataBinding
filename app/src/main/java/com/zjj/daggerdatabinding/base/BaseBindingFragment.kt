package com.zjj.daggerdatabinding.base

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.simple.eventbus.EventBus

/**
 * Created by zjj on 17/10/17.
 */
abstract class BaseBindingFragment<B: ViewDataBinding> : Fragment(){
    var isViewPrepared : Boolean = false//标示fragment视图已经初始化完毕
    var hasFetchData : Boolean = false//标示已经触发懒加载数据
    lateinit var mBinding : B
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = createDataBinding(inflater,container,savedInstanceState)
        EventBus.getDefault().register(this)
        initView()
        return mBinding.root

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewPrepared = true
        lazyFetchDataIfPrepared()
    }

    private fun lazyFetchDataIfPrepared(){
        // 用户可见fragment && 没有加载过数据 && 视图已经准备完毕
        if (getUserVisibleHint() && !hasFetchData && isViewPrepared) {
            hasFetchData = true;
            lazyFetchData();
        }
    }

    /**
     * 懒加载的方式获取数据，仅在满足fragment可见和视图已经准备好的时候调用一次
     */
    open fun lazyFetchData() {
        Log.v("BaseBindingFragment", javaClass.name + "------>lazyFetchData")
    }
    abstract fun  createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): B

    abstract fun initView()

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView()
        hasFetchData = false
        isViewPrepared =false
    }

}