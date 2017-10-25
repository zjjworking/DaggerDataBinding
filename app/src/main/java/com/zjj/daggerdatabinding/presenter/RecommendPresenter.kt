package com.zjj.daggerdatabinding.presenter

import android.util.Log
import com.zjj.daggerdatabinding.contract.RecommendContract
import com.zjj.daggerdatabinding.model.RecommendModel
import com.zjj.daggerdatabinding.utils.RxUtil
import javax.inject.Inject
import com.zjj.daggerdatabinding.bean.JsonResult
import com.zjj.daggerdatabinding.bean.VideoHttpResponse
import com.zjj.daggerdatabinding.bean.VideoRes
import com.zjj.daggerdatabinding.utils.StringUtils
import rx.android.schedulers.AndroidSchedulers


/**
 * Created by zjj on 17/10/19.
 */
class RecommendPresenter
@Inject constructor(private val mModel: RecommendModel,
                    private val mView : RecommendContract.View) : RecommendContract.Presenter,BasePresenter(){
    override fun getData() {
        addSubscription(mModel.getData().observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    res->
                    if(res != null){
                        mView.showContent(res.ret)
                    }
                },{
                    e->
                    Log.e("zjj","error android Presenter" + e.message)
                    mView.refreshFaild(StringUtils.getErrorMsg(e.message.toString()))
                }))
    }

}