package com.zjj.daggerdatabinding.api

import com.zjj.daggerdatabinding.bean.JsonResult
import com.zjj.daggerdatabinding.bean.VideoHttpResponse
import com.zjj.daggerdatabinding.bean.VideoRes
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import rx.Observable
/**
 * Created by zjj on 17/10/22.
 */
/**
 * 首页
 *
 * @return
 */
interface SecondApi {
    @GET("homePageApi/homePage.do")
    fun getHomePage(): Observable<VideoHttpResponse<VideoRes>>

    /**
     * 影片详情
     *
     * @param mediaId 影片id
     * @return
     */
    @GET("videoDetailApi/videoDetail.do")
    fun getVideoInfo(@Query("mediaId") mediaId: String): Observable<VideoHttpResponse<VideoRes>>
}