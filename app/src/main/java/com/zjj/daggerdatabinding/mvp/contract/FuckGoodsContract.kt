package com.zjj.daggerdatabinding.mvp.contract

import com.zjj.daggerdatabinding.base.BaseView
import com.zjj.daggerdatabinding.bean.FuckGoods
import com.zjj.daggerdatabinding.bean.JsonResult
import rx.Observable

/**
 * Created by zjj on 17/10/17.
 */
interface FuckGoodsContract {
    //V层主要是做数据相关展示的
    interface View : BaseView{
        /**
         * 设置数据
         */
        fun  setData(results: List<FuckGoods>)
        /**
         * 刷新失败
         */
        fun refreshFaild(msg: String)
        /**
         * 刷新
         */
        open fun onRefresh()

    }
    //数据处理，如：数据库操作
    interface Model {

        fun getData(page: Int,type:String): Observable<JsonResult<List<FuckGoods>>>
    }
    //和View交互相关操作，从M层获取各种数据
    interface Presenter {
        /**
         * 获取数据
         */
        open fun getData(page: Int, type: String)

        /**
         * 刷新
         */
        open fun onRefresh()

        /**
         * 获取更多
         */
        open fun loadMore()
    }
}