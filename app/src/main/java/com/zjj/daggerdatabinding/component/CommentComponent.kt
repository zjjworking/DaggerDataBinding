package com.zjj.daggerdatabinding.component

import com.zjj.daggerdatabinding.mvp.contract.CommentContract
import com.zjj.daggerdatabinding.ui.fragment.VideoCommentFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by zjj on 17/10/26.
 */
@Subcomponent(modules = arrayOf(CommentModule::class))

interface CommentComponent {
    fun inject(fragment: VideoCommentFragment)
}

@Module
class CommentModule(private val mView: CommentContract.View) {
    @Provides
    fun getView() = mView
}