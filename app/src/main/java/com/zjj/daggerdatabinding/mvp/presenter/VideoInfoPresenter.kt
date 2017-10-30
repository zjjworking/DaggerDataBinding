package com.zjj.daggerdatabinding.mvp.presenter

import android.util.Log
import com.zjj.daggerdatabinding.mvp.contract.VideoInfoContract
import com.zjj.daggerdatabinding.mvp.model.VideoInfoModel
import com.zjj.daggerdatabinding.utils.StringUtils
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import com.zjj.daggerdatabinding.bean.VideoRes
import com.zjj.daggerdatabinding.utils.RxUtil
import org.simple.eventbus.EventBus
import java.util.concurrent.TimeUnit


/**
 * Created by zjj on 17/10/26.
 */

class VideoInfoPresenter
@Inject constructor(private val mModel: VideoInfoModel,
                    private val mView : VideoInfoContract.View) : VideoInfoContract.Presenter,BasePresenter(){


    val WAIT_TIME :Long = 200
    var result: VideoRes? = null
    var dataId = ""

    override fun setDetailId(dataId: String) {
        this.dataId = dataId
        getDetailData(dataId)
        putMediaId()
    }
    override fun getDetailData(dataId: String) {
        addSubscription(mModel.getData(dataId).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    res->
                    if(res != null){
                        result = res.ret
                        postData()
                        mView.showContent(res.ret)
                    }
                },{
                    e->
                    Log.e("zjj","error android Presenter" + e.message)
                    mView.refreshFaild(StringUtils.getErrorMsg(e.message.toString()))
                },{
                    mView.hidLoading()
                }))
    }
    fun postData(){
        addSubscription(Observable.timer(WAIT_TIME,TimeUnit.MILLISECONDS)
                .compose(RxUtil.rxSchedulerHelper<Long>())
                .subscribe { along->
                    EventBus.getDefault().post(result, Refresh_Video_Info)
                }
        )
    }
    fun putMediaId(){
        addSubscription(Observable.timer(WAIT_TIME,TimeUnit.MILLISECONDS)
                .compose(RxUtil.rxSchedulerHelper<Long>())
                .subscribe { along->
                    EventBus.getDefault().post(dataId, Put_DataId)
                })
    }
    companion object {
        const val Refresh_Video_Info = "Refresh_Video_Info"
        const val Put_DataId = "Put_DataId"
       const val Refresh_Collection_List = "Refresh_Collection_List"
       const val Refresh_History_List = "Refresh_History_List"
    }
}