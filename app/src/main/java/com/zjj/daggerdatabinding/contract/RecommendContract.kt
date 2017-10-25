package com.zjj.daggerdatabinding.contract

import com.zjj.daggerdatabinding.base.BaseView
import com.zjj.daggerdatabinding.bean.FuckGoods
import com.zjj.daggerdatabinding.bean.JsonResult
import com.zjj.daggerdatabinding.bean.VideoHttpResponse
import com.zjj.daggerdatabinding.bean.VideoRes
import rx.Observable

/**
 * Created by zjj on 17/10/19.
 */
interface RecommendContract{
    interface View : BaseView {
        fun showContent(ret: VideoRes)

        fun refreshFaild(msg : String)

        fun onRefresh()

    }
    interface Model{
        fun getData(): Observable<VideoHttpResponse<VideoRes>>
    }
    interface Presenter{

        fun getData()

    }
}