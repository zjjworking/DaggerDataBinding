package com.zjj.daggerdatabinding.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zjj.daggerdatabinding.App.getMainComponent
import com.zjj.daggerdatabinding.R
import com.zjj.daggerdatabinding.base.BaseBindingFragment
import com.zjj.daggerdatabinding.bean.FuckGoods
import com.zjj.daggerdatabinding.component.FuckGoodsModule
import com.zjj.daggerdatabinding.contract.FuckGoodsContract
import com.zjj.daggerdatabinding.databinding.ViewRecyclerBinding
import com.zjj.daggerdatabinding.presenter.FuckGoodsPresenter
import com.zjj.daggerdatabinding.router.ClientUri
import com.zjj.daggerdatabinding.router.Router
import com.zjj.daggerdatabinding.ui.adapter.FuckGoodsAdapter
import com.zjj.daggerdatabinding.utils.EventUtil
import kotlinx.android.synthetic.main.view_recycler.*
import java.net.URLEncoder
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by zjj on 17/10/18.
 */
class IOSFragment : BaseBindingFragment<ViewRecyclerBinding>() , FuckGoodsContract.View{


    private var mList = ArrayList<FuckGoods>()
    private lateinit var mAdapter: FuckGoodsAdapter
    private var mPage = 1
    @Inject lateinit var mPresenter: FuckGoodsPresenter

    private lateinit var  empty_view : View
    private lateinit var error_view : View

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?,
                                   savedInstanceState: Bundle?): ViewRecyclerBinding {
        return ViewRecyclerBinding.inflate(inflater,container,false)
    }

    override fun initView() {
        mAdapter = FuckGoodsAdapter(mList,R.layout.item_fuckgoods)
        context.getMainComponent().plus(FuckGoodsModule(this)).inject(this)

        with(mBinding!!) {
            tvTitle.text = "iOS干货"
            recyclerView.adapter = mAdapter
            recyclerView.setLayoutManager(LinearLayoutManager(context))
            empty_view = LayoutInflater.from(context).inflate(R.layout.view_empty,recyclerView.getParent() as ViewGroup,false)
            empty_view.setOnClickListener { view->
                recyclerView.showProgress()
                onRefresh()
            }
            error_view = LayoutInflater.from(context).inflate(R.layout.view_error,recyclerView.getParent() as ViewGroup,false)
            error_view.setOnClickListener { view->
                recyclerView.showProgress()
                onRefresh()
            }
            recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(!recyclerView?.canScrollVertically(1)!!){
                        mPresenter.getData(++mPage, IOS)
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })

            recyclerView.setRefreshListener {
                onRefresh()
            }
            mPresenter.getData(mPage,IOS)

        }

        mAdapter.setOnItemClickListener {
            pos ->
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
        val IOS = "iOS"
        fun newInstance(): IOSFragment {
            val fragment = IOSFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun showError(msg: String) {
        mAdapter.setEmptyView(error_view)
        EventUtil.showToast(mBinding.recyclerView.context, msg)
    }

    override fun refreshFaild(msg: String) {
        if(!TextUtils.isEmpty(msg)){
            showError(msg)
        }

    }
    override fun onRefresh() {
        mPresenter.getData(mPage, IOS)
    }

}