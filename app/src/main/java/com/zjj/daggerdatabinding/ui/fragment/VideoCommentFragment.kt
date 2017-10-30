package com.zjj.daggerdatabinding.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jude.easyrecyclerview.decoration.SpaceDecoration
import com.zjj.daggerdatabinding.App.getMainComponent
import com.zjj.daggerdatabinding.R
import com.zjj.daggerdatabinding.base.BaseBindingFragment
import com.zjj.daggerdatabinding.bean.VideoType
import com.zjj.daggerdatabinding.component.CommentModule
import com.zjj.daggerdatabinding.databinding.FragmentVideoIntroBinding
import com.zjj.daggerdatabinding.mvp.contract.CommentContract
import com.zjj.daggerdatabinding.mvp.presenter.CommentPresenter
import com.zjj.daggerdatabinding.mvp.presenter.VideoInfoPresenter
import com.zjj.daggerdatabinding.ui.adapter.CommentAdapter
import com.zjj.daggerdatabinding.utils.EventUtil
import com.zjj.daggerdatabinding.utils.ScreenUtil
import com.zjj.daggerdatabinding.widget.CustomLoadMoreView
import org.simple.eventbus.Subscriber
import javax.inject.Inject


/**
 * Created by zjj on 17/10/26.
 */
class VideoCommentFragment : BaseBindingFragment<FragmentVideoIntroBinding>(),CommentContract.View {


    private lateinit var adapter : CommentAdapter
    private var mList = ArrayList<VideoType>()

    private lateinit var textView : TextView
    @Inject lateinit var mPresenter: CommentPresenter
    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentVideoIntroBinding {
        return FragmentVideoIntroBinding.inflate(inflater,container,false)
    }

    override fun initView() {
        context.getMainComponent().plus(CommentModule(this)).inject(this)
        with(mBinding){
            adapter = CommentAdapter(mList, R.layout.item_comment)
            //设置加载空间
//            adapter.setLoadMoreView(CustomLoadMoreView())
            //加载更多
//            adapter.setOnLoadMoreListener {
//                mPresenter.loadMore()
//            }
            //动画加载更多动画
            adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
            recyclerView.setErrorView(R.layout.view_error)
            recyclerView.adapter = adapter
            recyclerView.setLayoutManager(LinearLayoutManager(context))
            val itemDecoration =  SpaceDecoration(ScreenUtil.dip2px(context,8f))
            itemDecoration.setPaddingEdgeSide(true);
            itemDecoration.setPaddingStart(true);
            itemDecoration.setPaddingHeaderFooter(false);
            recyclerView.addItemDecoration(itemDecoration);
            textView = recyclerView.emptyView as TextView
            textView.text = "暂无评论"

            recyclerView.setRefreshListener {
                mPresenter.onRefresh()
            }
            recyclerView.errorView.setOnClickListener {
                recyclerView.showProgress()
                mPresenter.onRefresh()
            }
        }
    }
    override fun showError(msg: String) {
        EventUtil.showToast(mBinding.recyclerView.context, msg)
    }

    override fun refreshFaild(msg: String) {
        if(!TextUtils.isEmpty(msg)){
            showError(msg)
        }
    }
    @Subscriber(tag = VideoInfoPresenter.Put_DataId)
    fun setData(dataId : String){
        mPresenter.setMediaId(dataId)
        mPresenter.onRefresh()
    }
    override fun showContent(list: List<VideoType>) {
        adapter.setNewData(list)
        adapter.notifyDataSetChanged()
    }

    override fun showMoreContent(list: List<VideoType>) {
        adapter.addData(list)
        adapter.notifyDataSetChanged()
    }

    override fun lazyFetchData() {
        mPresenter.onRefresh()
    }
}