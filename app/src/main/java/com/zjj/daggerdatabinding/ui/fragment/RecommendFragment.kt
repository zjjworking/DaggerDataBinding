package com.zjj.daggerdatabinding.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jude.easyrecyclerview.decoration.SpaceDecoration
import com.jude.rollviewpager.hintview.IconHintView
import com.zjj.daggerdatabinding.App.getMainComponent
import com.zjj.daggerdatabinding.R
import com.zjj.daggerdatabinding.base.BaseBindingFragment
import com.zjj.daggerdatabinding.bean.VideoInfo
import com.zjj.daggerdatabinding.bean.VideoRes
import com.zjj.daggerdatabinding.component.RecommendModule
import com.zjj.daggerdatabinding.mvp.contract.RecommendContract
import com.zjj.daggerdatabinding.databinding.RecommendHeaderBinding
import com.zjj.daggerdatabinding.databinding.ViewRecyclerBinding
import com.zjj.daggerdatabinding.mvp.presenter.RecommendPresenter
import com.zjj.daggerdatabinding.router.ClientUri
import com.zjj.daggerdatabinding.router.Router
import com.zjj.daggerdatabinding.ui.adapter.BannerAdapter
import com.zjj.daggerdatabinding.ui.adapter.RecommendAdapter
import com.zjj.daggerdatabinding.utils.EventUtil
import javax.inject.Inject
import com.zjj.daggerdatabinding.utils.ScreenUtil
import java.net.URLEncoder


/**
 * Created by zjj on 17/10/19.
 */
class RecommendFragment : BaseBindingFragment<ViewRecyclerBinding>(),RecommendContract.View{


    private var mList = ArrayList<VideoInfo>()

    private var bannerList = ArrayList<VideoInfo>()

    private lateinit var mAdapter: RecommendAdapter

    private lateinit var bannerAdapter : BannerAdapter


    @Inject lateinit var mPresenter: RecommendPresenter

    private var Intdy :Int = 0

    private lateinit var binding :RecommendHeaderBinding

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): ViewRecyclerBinding {
         binding = RecommendHeaderBinding.inflate(inflater)
        return ViewRecyclerBinding.inflate(inflater,container,false)
    }

    override fun initView() {
        context.getMainComponent().plus(RecommendModule(this)).inject(this)
        mAdapter = RecommendAdapter(mList,R.layout.item_video)
        bannerAdapter = BannerAdapter(context,bannerList)

        mAdapter.addHeaderView(binding.root)
        binding.banner.setHintView(IconHintView(context, R.mipmap.ic_page_indicator_focused, R.mipmap.ic_page_indicator, ScreenUtil.dip2px(context, 10f)))
        binding.banner.setHintPadding(0, 0, 0, ScreenUtil.dip2px(context, 8f))
        binding.banner.setAdapter(bannerAdapter)
        binding.banner.setPlayDelay(2000)

        with(mBinding){
            toolbar.visibility = View.GONE
            tvTitle.text = "精选"
            recyclerView.adapter = mAdapter
            recyclerView.setLayoutManager(LinearLayoutManager(context))
            recyclerView.setErrorView(R.layout.view_error)
            val itemDecoration = SpaceDecoration(ScreenUtil.dip2px(context,8f))
            itemDecoration.setPaddingEdgeSide(true)
            itemDecoration.setPaddingStart(true)
            itemDecoration.setPaddingHeaderFooter(false)
            recyclerView.addItemDecoration(itemDecoration)
            toolbar.setOnClickListener { v->
                if(EventUtil.isFastDoubleClick){
                    Intdy = 0
                    recyclerView.scrollToPosition(0)
                }
            }
            recyclerView.setRefreshListener {
                onRefresh()
            }
            recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    Intdy += dy
                    if(Intdy <= ScreenUtil.dip2px(context,150f)){
                        Handler().postAtTime({
                            val percentage  = (Intdy / ScreenUtil.dip2px(context, 150f)).toFloat()
                            toolbar.setAlpha(percentage)
                            if (percentage > 0)
                                toolbar.visibility = View.VISIBLE
                            else
                                toolbar.visibility = View.GONE
                        },300)
                    }else {
                        toolbar.alpha = 1.0f
                        toolbar.visibility = View.VISIBLE
                    }
                }
            })

            recyclerView.errorView.setOnClickListener { view->
                recyclerView.showProgress()
                onRefresh()
            }
            mAdapter.setOnItemClickListener { pos ->
                val dataId = URLEncoder.encode(mList[pos-1].dataId)
                Router.router(context, ClientUri.VIDEO_INFO + dataId)
            }
            mPresenter.getData()
        }
    }
    override fun showError(msg: String) {
        EventUtil.showToast(context, msg);
    }


    override fun showContent(videoRes: VideoRes) {
//        mAdapter.clear()
        val videoInfos: MutableCollection<VideoInfo>
        for(vidoInfo in videoRes.list){
            if(vidoInfo.title == "精彩推荐"){
                videoInfos = vidoInfo.childList
                mList.addAll(videoInfos)
                break
            }
        }
        for(vidoInfo in videoRes.list){
            if(vidoInfo.title == "免费推荐"){
                mList.addAll(vidoInfo.childList)
                break
            }
        }
        bannerList.addAll(videoRes.list[0].childList)
        bannerAdapter.notifyDataSetChanged()
        mAdapter.notifyDataSetChanged()

    }

    override fun refreshFaild(msg: String) {
        if (!TextUtils.isEmpty(msg)){
            showError(msg)
        }
        mBinding.recyclerView.showError()

    }
    override fun onRefresh() {
        mPresenter.getData()
    }


    companion object {
        fun newInstance(): RecommendFragment {
            val fragment = RecommendFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}