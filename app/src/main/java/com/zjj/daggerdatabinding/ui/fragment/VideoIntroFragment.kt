package com.zjj.daggerdatabinding.ui.fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zjj.daggerdatabinding.R
import com.zjj.daggerdatabinding.base.BaseBindingFragment
import com.zjj.daggerdatabinding.bean.VideoInfo
import com.zjj.daggerdatabinding.databinding.FragmentVideoIntroBinding
import com.zjj.daggerdatabinding.ui.adapter.RelatedAdapter
import com.zjj.daggerdatabinding.utils.ScreenUtil
import com.jude.easyrecyclerview.decoration.SpaceDecoration
import com.zjj.daggerdatabinding.bean.VideoRes
import com.zjj.daggerdatabinding.databinding.IntroHeaderBinding
import com.zjj.daggerdatabinding.mvp.presenter.VideoInfoPresenter
import com.zjj.daggerdatabinding.utils.StringUtils
import org.simple.eventbus.Subscriber


/**
 * Created by zjj on 17/10/26.
 */
class VideoIntroFragment : BaseBindingFragment<FragmentVideoIntroBinding>() {

    private lateinit var adapter: RelatedAdapter

    private lateinit var binding : IntroHeaderBinding

    private var mList = ArrayList<VideoInfo>()

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentVideoIntroBinding {
       binding = IntroHeaderBinding.inflate(inflater)
        return FragmentVideoIntroBinding.inflate(inflater,container,false)
    }

    override fun initView() {
        adapter = RelatedAdapter(mList, R.layout.item_related)
        mBinding.recyclerView.adapter = adapter
        val gridLayoutManager  = GridLayoutManager(context,3)
        val count = gridLayoutManager.spanCount
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if(position == 0){
                    return count
                }
                return 1
            }
        }
        with(mBinding) {
            recyclerView.setLayoutManager(gridLayoutManager)
            val itemDecoration = SpaceDecoration(ScreenUtil.dip2px(context, 8f))
            itemDecoration.setPaddingEdgeSide(true)
            itemDecoration.setPaddingStart(true)
            itemDecoration.setPaddingHeaderFooter(false)
            recyclerView.addItemDecoration(itemDecoration)
            recyclerView.swipeToRefresh.isRefreshing = false

        }
        adapter.addHeaderView(binding.root)
    }
    @Subscriber(tag = VideoInfoPresenter.Refresh_Video_Info)
    fun setData(videoInfo : VideoRes){
        val dir = "导演：" + StringUtils.removeOtherCode(videoInfo.director)
        val act = "主演：" + StringUtils.removeOtherCode(videoInfo.actors)
        val des = dir + "\n" + act + "\n" + "简介：" + StringUtils.removeOtherCode(videoInfo.description)
        binding.tvExpand.setText(des)
        if (videoInfo.list.size > 1)
            adapter.addData(videoInfo.list[1].childList)
        else
            adapter.addData(videoInfo.list[0].childList)
    }
}