package com.zjj.daggerdatabinding.ui.fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jude.easyrecyclerview.EasyRecyclerView
import com.zjj.daggerdatabinding.App.getMainComponent
import com.zjj.daggerdatabinding.R
import com.zjj.daggerdatabinding.base.BaseBindingFragment
import com.zjj.daggerdatabinding.bean.FuckGoods
import com.zjj.daggerdatabinding.component.FuckGoodsModule
import com.zjj.daggerdatabinding.mvp.contract.FuckGoodsContract
import com.zjj.daggerdatabinding.databinding.ViewRecyclerBinding
import com.zjj.daggerdatabinding.mvp.presenter.FuckGoodsPresenter
import com.zjj.daggerdatabinding.ui.adapter.GirlAdapter
import com.zjj.daggerdatabinding.utils.EventUtil
import java.util.ArrayList
import javax.inject.Inject


class GirlFragment : BaseBindingFragment<ViewRecyclerBinding>(), FuckGoodsContract.View {

    private lateinit var mRecyclerView : EasyRecyclerView
    private var mList = ArrayList<FuckGoods>()
    private lateinit var mAdapter: GirlAdapter
    private var mPage = 1
    @Inject lateinit var mPresenter: FuckGoodsPresenter



    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?,
                                   savedInstanceState: Bundle?): ViewRecyclerBinding {
        return ViewRecyclerBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        mAdapter = GirlAdapter(mList,R.layout.item_girl)
        context.getMainComponent().plus(FuckGoodsModule(this)).inject(this)

        with(mBinding) {
            tvTitle.text  = "妹子"
            mRecyclerView = recyclerView
            recyclerView.adapter = mAdapter
            recyclerView.setLayoutManager(GridLayoutManager(context,2))
            recyclerView.setErrorView(R.layout.view_error)
            recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView?.canScrollVertically(1)!!) {
                        mPresenter.getData(++mPage, GIRL)
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
            recyclerView.errorView.setOnClickListener { view ->
                recyclerView.showProgress()
                onRefresh()
            }
            recyclerView.setRefreshListener {
                onRefresh()
            }
            mPresenter.getData(mPage, GIRL)

        }

//        mAdapter.setOnItemClickListener {
//            pos->
//
//            val imageView = recyclerView.findViewHolderForAdapterPosition(pos)?.itemView?.findViewById(R.id.iv_girl) as ImageView
//
//            JumpUtil.startIMGActivity(context,imageView,mList[pos].url)
//        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unSubscribe()
    }

    override fun setData(results: List<FuckGoods>) {

        mList.addAll(results)
        mAdapter.notifyDataSetChanged()
    }

    companion object {
        val GIRL = "GIRL"
        fun newInstance(): GirlFragment {
            val fragment = GirlFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun showError(msg: String) {
        EventUtil.showToast(mBinding.recyclerView.context, msg);
    }

    override fun refreshFaild(msg: String) {
        if(!TextUtils.isEmpty(msg)){
            showError(msg)
        }
    }

    override fun onRefresh() {
        mPresenter.getData(mPage, GIRL)
    }


}