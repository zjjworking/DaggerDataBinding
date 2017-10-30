package com.zjj.daggerdatabinding.mvp.presenter

import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Created by zjj on 17/10/17.
 * 基于Rx的Presenter封装,控制订阅的生命周期
 */
open class BasePresenter{
    var compositeSubscription = CompositeSubscription()

    protected fun addSubscription(subscription: Subscription) {
        compositeSubscription.add(subscription)
    }

    fun unSubscribe() {
        if(compositeSubscription.hasSubscriptions()){
            compositeSubscription.unsubscribe()
        }
    }
}