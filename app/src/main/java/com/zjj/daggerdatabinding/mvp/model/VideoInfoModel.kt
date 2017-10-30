package com.zjj.daggerdatabinding.mvp.model

import com.zjj.daggerdatabinding.api.SecondApi
import com.zjj.daggerdatabinding.bean.VideoHttpResponse
import com.zjj.daggerdatabinding.bean.VideoRes
import com.zjj.daggerdatabinding.mvp.contract.RecommendContract
import com.zjj.daggerdatabinding.mvp.contract.VideoInfoContract
import rx.Observable
import javax.inject.Inject

/**
 * Created by zjj on 17/10/26.
 */
class VideoInfoModel
@Inject constructor(private val api: SecondApi) : VideoInfoContract.Model{
    override fun getData(dataId: String): Observable<VideoHttpResponse<VideoRes>> =
            api.getVideoInfo(dataId)

}