package com.zjj.daggerdatabinding.model


import com.zjj.daggerdatabinding.api.SecondApi
import com.zjj.daggerdatabinding.bean.VideoHttpResponse
import com.zjj.daggerdatabinding.bean.VideoRes
import com.zjj.daggerdatabinding.contract.RecommendContract
import rx.Observable
import javax.inject.Inject

/**
 * Created by zjj on 17/10/19.
 */
class RecommendModel
@Inject constructor(private val api: SecondApi) : RecommendContract.Model{
    override fun getData(): Observable<VideoHttpResponse<VideoRes>> = api.getHomePage()

}