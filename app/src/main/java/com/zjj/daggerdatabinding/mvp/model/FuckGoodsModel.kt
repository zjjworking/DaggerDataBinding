package com.zjj.daggerdatabinding.mvp.model

import com.zjj.daggerdatabinding.api.Api
import com.zjj.daggerdatabinding.bean.FuckGoods
import com.zjj.daggerdatabinding.bean.JsonResult
import com.zjj.daggerdatabinding.mvp.contract.FuckGoodsContract
import com.zjj.daggerdatabinding.ui.fragment.AndroidFragment
import com.zjj.daggerdatabinding.ui.fragment.GirlFragment
import com.zjj.daggerdatabinding.ui.fragment.IOSFragment
import rx.Observable
import javax.inject.Inject

/**
 * Created by zjj on 17/10/17.
 */
class FuckGoodsModel
@Inject constructor(private val api:Api) : FuckGoodsContract.Model{
    override fun getData(page: Int, type: String): Observable<JsonResult<List<FuckGoods>>> {
        when(type){
            AndroidFragment.ANDROID -> return api.getAndroidData(page)
            IOSFragment.IOS -> return api.getiOSData(page)
            GirlFragment.GIRL -> return api.getGirlData(page)
            else -> return api.getAndroidData(page)
        }
    }

}