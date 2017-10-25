package com.zjj.daggerdatabinding.api

import com.zjj.daggerdatabinding.bean.FuckGoods
import com.zjj.daggerdatabinding.bean.JsonResult
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable
import com.zjj.daggerdatabinding.bean.VideoRes
import retrofit2.http.Headers
import retrofit2.http.Query


/**
 * Created by zjj on 17/10/17.
 */
interface Api{

    /**
     * Android所有数据
     */
    @GET("data/Android/10/{page}")
    fun getAndroidData(@Path("page") page:Int): Observable<JsonResult<List<FuckGoods>>>

    /**
     * iOS所有数据
     */
    @GET("data/iOS/10/{page}")
    fun getiOSData(@Path("page") page:Int):Observable<JsonResult<List<FuckGoods>>>

    /**
     * iOS所有数据
     */
    @GET("data/福利/10/{page}")
    fun getGirlData(@Path("page") page:Int):Observable<JsonResult<List<FuckGoods>>>


    /**
     * 手气不错
     */

    @GET("random/data/{type}/1")
    fun getRandom(@Path("type") type:String):Observable<JsonResult<List<FuckGoods>>>




    /**
     * 首页
     *
     * @return
     */
    @GET("homePageApi/homePage.do")
    fun getHomePage(): Observable<JsonResult<VideoRes>>

    /**
     * 影片详情
     *
     * @param mediaId 影片id
     * @return
     */
    @GET("videoDetailApi/videoDetail.do")
    fun getVideoInfo(@Query("mediaId") mediaId: String): Observable<JsonResult<VideoRes>>

}