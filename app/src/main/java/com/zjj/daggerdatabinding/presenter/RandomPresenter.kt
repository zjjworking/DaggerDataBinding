package com.zjj.daggerdatabinding.presenter

import android.util.Log
import com.zjj.daggerdatabinding.contract.RandomContract
import com.zjj.daggerdatabinding.model.RandomModel
import com.zjj.daggerdatabinding.utils.StringUtils
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by zjj on 17/10/18.
 */
class RandomPresenter
@Inject constructor(private val mModel: RandomModel,
                    private val mView: RandomContract.View) : RandomContract.Presenter, BasePresenter() {
    override fun getRandom(type: String) {
        addSubscription(mModel.getRandom(type).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    res ->
                    if (!res.error) {
                        mView.onRandom(res.results[0])
                    }
                },{e ->
                    Log.e("zjj","error android Presenter" + e.message)
                    mView.showError(StringUtils.getErrorMsg(e.message.toString()))

                }, {}))
    }
}