package com.zjj.daggerdatabinding.contract

import com.zjj.daggerdatabinding.base.BaseView
import com.zjj.daggerdatabinding.bean.FuckGoods
import com.zjj.daggerdatabinding.bean.JsonResult
import rx.Observable


/**
 * Created by zjj on 17/10/17.
 *
 */
interface RandomContract{
    interface View : BaseView{
        fun onRandom(goods: FuckGoods)
    }
    interface Model{
        fun getRandom(type: String): Observable<JsonResult<List<FuckGoods>>>
    }
    interface Presenter{
        fun getRandom(type:String)
    }
}