package com.zjj.daggerdatabinding.ui.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.FragmentPagerAdapter
import android.text.TextUtils
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import cn.jzvd.JZVideoPlayer
import cn.jzvd.JZVideoPlayerStandard
import com.airbnb.deeplinkdispatch.DeepLink
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import com.zjj.daggerdatabinding.R
import com.zjj.daggerdatabinding.base.BaseBindingActivity
import com.zjj.daggerdatabinding.bean.VideoRes
import com.zjj.daggerdatabinding.databinding.ActivityVideoInfoViewBinding
import com.zjj.daggerdatabinding.mvp.contract.VideoInfoContract
import com.zjj.daggerdatabinding.App.getMainComponent
import com.zjj.daggerdatabinding.component.VideoInfoModule
import com.zjj.daggerdatabinding.mvp.presenter.VideoInfoPresenter
import com.zjj.daggerdatabinding.router.ClientUri
import com.zjj.daggerdatabinding.ui.fragment.VideoCommentFragment
import com.zjj.daggerdatabinding.ui.fragment.VideoIntroFragment
import com.zjj.daggerdatabinding.utils.EventUtil
import com.zjj.daggerdatabinding.utils.ImageLoader
import java.net.URLDecoder
import javax.inject.Inject


/**
 * Created by zjj on 17/10/25.
 */
@DeepLink("${ClientUri.VIDEO_INFO}{${ClientUri.DATAID}}")
class VideoInfoActivity  : BaseBindingActivity<ActivityVideoInfoViewBinding>(), VideoInfoContract.View{

    private lateinit var dataId : String
    private lateinit var animation : Animation

    private lateinit var videoRes : VideoRes

    @Inject lateinit var mPresenter : VideoInfoPresenter

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityVideoInfoViewBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_video_info_view)

    override fun initView() {
        getMainComponent().plus(VideoInfoModule(this)).inject(this)
        animation = AnimationUtils.loadAnimation(applicationContext,R.anim.view_hand)
        if(intent.getBooleanExtra(DeepLink.IS_DEEP_LINK,false)){
            dataId = URLDecoder.decode(intent.extras.getString(ClientUri.DATAID))
        }
        mPresenter.setDetailId(dataId)
        with(mBinding){
            mBinding.commenTitle.rlCollect.visibility = View.VISIBLE
            val adapter = FragmentPagerItemAdapter(
                    supportFragmentManager, FragmentPagerItems.with(applicationContext)
                    .add(R.string.video_intro, VideoIntroFragment::class.java)
                    .add(R.string.video_comment, VideoCommentFragment::class.java)
                    .create())
            viewpager.adapter = adapter
            viewpagertab.setViewPager(viewpager)
            videoplayer.thumbImageView.scaleType = ImageView.ScaleType.CENTER_CROP
            videoplayer.backButton.visibility = View.GONE
            videoplayer.titleTextView.visibility = View.GONE
            videoplayer.tinyBackImageView.visibility = View.GONE
        }
    }




    override fun showContent(ret: VideoRes) {
        this.videoRes = ret
        with(mBinding) {
        commenTitle.titleName.setText(videoRes.title)
        if (!TextUtils.isEmpty(videoRes.pic)) {
            ImageLoader.load(videoplayer.thumbImageView, videoRes.pic)
        }
        if (!TextUtils.isEmpty(videoRes.videoUrl)) {
            videoplayer.setUp(videoRes.videoUrl,JZVideoPlayerStandard.SCREEN_LAYOUT_LIST,videoRes.title)
            videoplayer.onClick(videoplayer.thumbImageView)
        }
    }
    }

    override fun refreshFaild(msg: String) {
        if(!TextUtils.isEmpty(msg)){
            showError(msg)
        }
    }

    override fun hidLoading() {
        mBinding.circleLoading.visibility = View.GONE
    }


    override fun onPause() {
        super.onPause()
        JZVideoPlayer.releaseAllVideos()
    }

    override fun onBackPressed() {
        if(JZVideoPlayer.backPress()){
            return
        }
        super.onBackPressed()
    }
    override fun showError(msg: String) {
        EventUtil.showToast(applicationContext, msg);
    }
    override fun collected() {
        mBinding.commenTitle.ivCollect.setBackgroundResource(R.mipmap.collection_select)
    }

    override fun disCollect() {
        mBinding.commenTitle.ivCollect.setBackgroundResource(R.mipmap.collection)
    }
}