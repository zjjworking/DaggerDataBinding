package com.zjj.daggerdatabinding.mvp.presenter

import android.util.Log
import com.zjj.daggerdatabinding.mvp.contract.RecommendContract
import com.zjj.daggerdatabinding.mvp.model.RecommendModel
import javax.inject.Inject
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