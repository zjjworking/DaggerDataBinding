package com.zjj.daggerdatabinding.contract

import com.zjj.daggerdatabinding.base.BaseView
import com.zjj.daggerdatabinding.bean.FuckGoods
import com.zjj.daggerdatabinding.bean.JsonResult
import rx.Observable

/**
 * Created by zjj on 17/10/17.
 */
interface FuckGoodsContract {
    interface View : BaseView{
        /**
         * 设置数据
         */
        fun  setData(results: List<FuckGoods>)
        /**
         * 刷新失败
         */
        fun refreshFaild(msg: String)

    }

    interface Model {

        fun getData(page: Int,type:String): Observable<JsonResult<List<FuckGoods>>>
    }

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