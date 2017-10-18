package com.zjj.daggerdatabinding.presenter

import android.util.Log
import com.zjj.daggerdatabinding.contract.FuckGoodsContract
import com.zjj.daggerdatabinding.model.FuckGoodsModel
import com.zjj.daggerdatabinding.utils.StringUtils
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by zjj on 17/10/17.
 */
class FuckGoodsPresenter
@Inject constructor(private val mModel: FuckGoodsModel,
                    private val mView: FuckGoodsContract.View)
    :FuckGoodsContract.Presenter,BasePresenter(){

    override fun onRefresh() {

    }

    override fun loadMore() {
    }

    override fun getData(page: Int,type: String){
        addSubscription(mModel.getData(page,type).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    res ->
                    if (!res.error){
                        mView.setData(res.results)
                    }
                },{e ->
                    Log.e("zjj","error android Presenter" + e.message)
                    mView.refreshFaild(StringUtils.getErrorMsg(e.message.toString()))}))
    }

}