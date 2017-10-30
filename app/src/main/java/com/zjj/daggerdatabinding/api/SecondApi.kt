package com.zjj.daggerdatabinding.api

import com.zjj.daggerdatabinding.bean.VideoHttpResponse
import com.zjj.daggerdatabinding.bean.VideoRes
import retrofit2.http.GET
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

    /**
     * 获取评论列表
     * @param mediaId
     * @param pnum
     * @return
     */
    @GET("Commentary/getCommentList.do")
    fun getCommentList(@Query("mediaId") mediaId: String, @Query("pnum") pnum: String): Observable<VideoHttpResponse<VideoRes>>
}