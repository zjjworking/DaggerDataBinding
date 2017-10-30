package com.zjj.daggerdatabinding.mvp.presenter

import android.util.Log
import com.zjj.daggerdatabinding.bean.VideoHttpResponse
import com.zjj.daggerdatabinding.bean.VideoRes
import com.zjj.daggerdatabinding.mvp.contract.CommentContract
import com.zjj.daggerdatabinding.mvp.model.CommentModel
import com.zjj.daggerdatabinding.utils.RxUtil
import com.zjj.daggerdatabinding.utils.StringUtils
import java.net.URLDecoder
import javax.inject.Inject

/**
 * Created by zjj on 17/10/26.
 */
class CommentPresenter
@Inject constructor(private val mModel: CommentModel,
                    private val mView : CommentContract.View) : CommentContract.Presenter,BasePresenter(){
    var page = 1
    var id : String = ""
    override fun onRefresh() {
        page = 1
        if(!id.equals("")){
            getComment(id)
        }
    }

    override fun loadMore() {
        page++
        if(id.equals("")){
            getComment(id)
        }
    }

    override fun setMediaId(id: String) {
            this.id = id
    }
    fun getComment(mediaId : String){
        addSubscription(mModel.getData(page.toString(),mediaId)
                .compose(RxUtil.rxSchedulerHelper<VideoHttpResponse<VideoRes>>())
                .subscribe ({ res->
                    if(res != null) {
                        if (page == 1) {
                            mView.showContent(res.ret.list)
                        } else {
                            mView.showMoreContent(res.ret.list)
                        }
                    }
                },{e->
                    Log.e("zjj","error android Presenter" + e.message)
                    mView.refreshFaild(StringUtils.getErrorMsg(e.message.toString()))
                }))
    }
}