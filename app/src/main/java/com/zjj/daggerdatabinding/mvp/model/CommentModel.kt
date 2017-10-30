package com.zjj.daggerdatabinding.mvp.model

import com.zjj.daggerdatabinding.api.SecondApi
import com.zjj.daggerdatabinding.bean.VideoHttpResponse
import com.zjj.daggerdatabinding.bean.VideoRes
import com.zjj.daggerdatabinding.mvp.contract.CommentContract
import com.zjj.daggerdatabinding.mvp.contract.VideoInfoContract
import rx.Observable
import javax.inject.Inject

/**
 * Created by zjj on 17/10/26.
 */
class CommentModel
@Inject constructor(private val api: SecondApi) : CommentContract.Model{
    override fun getData(page: String, id: String): Observable<VideoHttpResponse<VideoRes>> =
            api.getCommentList(id,page)
}