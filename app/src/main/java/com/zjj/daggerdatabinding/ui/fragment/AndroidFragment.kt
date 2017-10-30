package com.zjj.daggerdatabinding.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zjj.daggerdatabinding.App.getMainComponent
import com.zjj.daggerdatabinding.R
import com.zjj.daggerdatabinding.base.BaseBindingFragment
import com.zjj.daggerdatabinding.bean.FuckGoods
import com.zjj.daggerdatabinding.component.FuckGoodsModule
import com.zjj.daggerdatabinding.mvp.contract.FuckGoodsContract
import com.zjj.daggerdatabinding.databinding.ViewRecyclerBinding
import com.zjj.daggerdatabinding.mvp.presenter.FuckGoodsPresenter
import com.zjj.daggerdatabinding.router.ClientUri
import com.zjj.daggerdatabinding.router.Router
import com.zjj.daggerdatabinding.ui.adapter.FuckGoodsAdapter
import com.zjj.daggerdatabinding.utils.EventUtil
import java.net.URLEncoder
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by zjj on 17/10/17.
 */

class AndroidFragment : BaseBindingFragment<ViewRecyclerBinding>(), FuckGoodsContract.View {



    private var mList = ArrayList<FuckGoods>()
    private lateinit var mAdapter: FuckGoodsAdapter
    private var mPage = 1
    @Inject lateinit var mPresenter: FuckGoodsPresenter
    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?,
                                   savedInstanceState: Bundle?): ViewRecyclerBinding {
        return ViewRecyclerBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        mAdapter = FuckGoodsAdapter(mList,R.layout.item_fuckgoods)
        context.getMainComponent().plus(FuckGoodsModule(this)).inject(this)

        with(mBinding) {
            mBinding.tvTitle.text  = "android干货"
            recyclerView.adapter = mAdapter
            recyclerView.setLayoutManager(LinearLayoutManager(context))
            recyclerView.setErrorView( R.layout.view_error)
            recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView?.canScrollVertically(1)!!) {
                        mPresenter.getData(++mPage, ANDROID)
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
            recyclerView.setRefreshListener{
                onRefresh()
            }
        }

        mPresenter.getData(mPage, ANDROID)

        mAdapter.setOnItemClickListener { pos ->
            val url = URLEncoder.encode(mList[pos].url)
            Router.router(context, ClientUri.DETAIL + url)
        }


    }

    override fun setData(results: List<FuckGoods>) {
        mList.addAll(results)
        mAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unSubscribe()
    }

    companion object {
        val ANDROID = "ANDROID"
        fun newInstance(): AndroidFragment {
            val fragment = AndroidFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun refreshFaild(msg: String) {
        if(!TextUtils.isEmpty(msg)){
            showError(msg)
        }

    }

    override fun showError(msg: String) {
        EventUtil.showToast(mBinding.recyclerView.context, msg);
    }
    override fun onRefresh() {
        mPresenter.getData(mPage, ANDROID)
    }
}