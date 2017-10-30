package com.zjj.daggerdatabinding.mvp.contract

import com.zjj.daggerdatabinding.mvp.presenter.BasePresenter
import com.zjj.daggerdatabinding.base.BaseView
import com.zjj.daggerdatabinding.bean.*
import rx.Observable


/**
 * Created by zjj on 17/10/26.
 */
interface CommentContract {

    interface View : BaseView {

        fun refreshFaild(msg: String)

        fun showContent(list: List<VideoType>)

        fun showMoreContent(list: List<VideoType>)
    }
    interface Model {

        fun getData(page: String,id : String): Observable<VideoHttpResponse<VideoRes>>
    }
    interface Presenter {

        fun onRefresh()

        fun loadMore()

        fun setMediaId(id: String)

    }
}