package com.zjj.daggerdatabinding.mvp.contract

import com.zjj.daggerdatabinding.base.BaseView
import com.zjj.daggerdatabinding.bean.VideoHttpResponse
import com.zjj.daggerdatabinding.bean.VideoRes
import rx.Observable

/**
 * Created by zjj on 17/10/26.
 */
interface VideoInfoContract{

    interface View : BaseView {
        fun showContent(ret: VideoRes)

        fun refreshFaild(msg : String)

        fun hidLoading()

        fun collected()

        fun disCollect()

    }
    interface Model{

        fun getData(dataId : String): Observable<VideoHttpResponse<VideoRes>>
    }
    interface Presenter{

        fun getDetailData(dataId : String)

        fun setDetailId(dataId: String)
    }
}