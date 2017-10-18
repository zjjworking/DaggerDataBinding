package com.zjj.daggerdatabinding.model

import com.zjj.daggerdatabinding.api.Api
import com.zjj.daggerdatabinding.bean.FuckGoods
import com.zjj.daggerdatabinding.bean.JsonResult
import com.zjj.daggerdatabinding.contract.RandomContract
import rx.Observable
import javax.inject.Inject

/**
 * Created by zjj on 17/10/18.
 */
class RandomModel
@Inject constructor(private val api:Api) : RandomContract.Model{

    override fun getRandom(type:String): Observable<JsonResult<List<FuckGoods>>> {
        return api.getRandom(type)
    }
}