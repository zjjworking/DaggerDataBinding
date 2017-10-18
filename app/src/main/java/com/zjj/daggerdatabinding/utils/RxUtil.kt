package com.zjj.daggerdatabinding.utils

import android.text.TextUtils
import com.zjj.daggerdatabinding.bean.JsonResult
import com.zjj.daggerdatabinding.model.exception.ApiException
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.lang.Exception

/**
 * Created by zjj on 17/10/17.
 */
object RxUtil {

    /**
     * 统一线程处理

     * @param
     * *
     * @return
     */
    fun <T> rxSchedulerHelper(): Observable.Transformer<T, T> {    //compose简化线程
        return Observable.Transformer<T, T> { observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
    }

    /**
     * 统一返回结果处理

     * @param
     * *
     * @return
     */
    fun <T> handleResult(): Observable.Transformer<JsonResult<T>, T> {   //compose判断结果
        return Observable.Transformer<JsonResult<T>, T> { httpResponseObservable ->
            httpResponseObservable.flatMap { response ->
                if (response.code == 200) {
                    createData(response.results)
                } else if (!TextUtils.isEmpty(response.msg)) {
                    Observable.error<T>(ApiException("*" + response.msg))
                } else {
                    Observable.error<T>(ApiException("*" + "服务器返回error"))
                }
            }
        }
    }

    fun <T> handleGankResult(): Observable.Transformer<JsonResult<T>, T> {   //compose判断结果
        return Observable.Transformer<JsonResult<T>, T> { httpResponseObservable ->
            httpResponseObservable.flatMap { tGankHttpResponse ->
                if (!tGankHttpResponse.error) {
                    createData(tGankHttpResponse.results)
                } else {
                    Observable.error<T>(ApiException("服务器返回error"))
                }
            }
        }
    }

    /**
     * 生成Observable

     * @param
     * *
     * @return
     */
    fun <T> createData(t: T): Observable<T> {
        return Observable.create { subscriber ->
            try {
                subscriber.onNext(t)
                subscriber.onCompleted()
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }
}
