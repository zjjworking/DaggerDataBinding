package com.zjj.daggerdatabinding.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter

import com.zjj.daggerdatabinding.App.App
import com.zjj.daggerdatabinding.App.getMainComponent
import com.zjj.daggerdatabinding.App.toast
import com.zjj.daggerdatabinding.R
import com.zjj.daggerdatabinding.base.BaseBindingActivity
import com.zjj.daggerdatabinding.bean.FuckGoods
import com.zjj.daggerdatabinding.component.RandomModule
import com.zjj.daggerdatabinding.mvp.contract.RandomContract
import com.zjj.daggerdatabinding.databinding.ActivityMainBinding
import com.zjj.daggerdatabinding.mvp.presenter.RandomPresenter
import com.zjj.daggerdatabinding.router.ClientUri
import com.zjj.daggerdatabinding.router.Router
import com.zjj.daggerdatabinding.ui.fragment.*
import com.zjj.daggerdatabinding.utils.EventUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URLEncoder
import java.util.ArrayList
import javax.inject.Inject

class MainActivity : BaseBindingActivity<ActivityMainBinding>(),RandomContract.View{


    private var firstTime: Long? = 0L
    lateinit var mFragments: MutableList<Fragment>
    @Inject lateinit var mPresenter : RandomPresenter

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityMainBinding {
       return DataBindingUtil.setContentView(this,R.layout.activity_main)
    }
    override fun initView() {
        initFragments()
        getMainComponent().plus(RandomModule(this)).inject(this)
        mBinding.viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int) = mFragments[position]
            override fun getCount() = mFragments.size
        }

        viewPager.offscreenPageLimit = 4

        navigationView.setOnNavigationItemSelectedListener { item ->
            var tab = 0
            when (item.itemId) {
                R.id.menu_about -> tab = 0
                R.id.menu_android -> tab = 1
                R.id.menu_ios -> tab = 2
                R.id.menu_girl -> tab = 3

            }
            viewPager.currentItem = tab
            false
        }


        floatingButton.setOnClickListener {
           when(viewPager.currentItem){
               1 -> mPresenter.getRandom("Android")
               2 -> mPresenter.getRandom("iOS")
               3 -> mPresenter.getRandom("福利")
           }

        }

    }
    override fun onBackPressed() {

        val secondTime = System.currentTimeMillis()
        if (secondTime - firstTime!! > 1500) {
            EventUtil.showToast(this, "再按一次退出")
            firstTime = secondTime
        } else {
            App.instance.exitApp()
        }

    }
    override fun showError(msg: String) {
        EventUtil.showToast(mBinding.root.context, msg);
    }
    private fun initFragments() {
        mFragments = ArrayList()
        mFragments.add(RecommendFragment.newInstance())
        mFragments.add(AndroidFragment.newInstance())
        mFragments.add(IOSFragment.newInstance())
        mFragments.add(GirlFragment.newInstance())

    }

    override fun onRandom(goods: FuckGoods) {
        val url = URLEncoder.encode(goods.url)
        toast("手气不错～")
        Router.router(this, ClientUri.DETAIL + url)
    }
    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String
}
