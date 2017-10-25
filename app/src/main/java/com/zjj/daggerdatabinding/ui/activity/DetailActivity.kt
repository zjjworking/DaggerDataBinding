package com.zjj.daggerdatabinding.ui.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.airbnb.deeplinkdispatch.DeepLink
import com.zjj.daggerdatabinding.R
import com.zjj.daggerdatabinding.base.BaseBindingActivity
import com.zjj.daggerdatabinding.databinding.ActivityDetailBinding
import com.zjj.daggerdatabinding.router.ClientUri
import kotlinx.android.synthetic.main.activity_detail.*
import java.net.URLDecoder

/**
 * Created by zjj on 17/10/19.
 */
@DeepLink("${ClientUri.DETAIL}{${ClientUri.DETAIL_PARAM_URL}}")
class DetailActivity : BaseBindingActivity<ActivityDetailBinding>(){
    var url = ""
    override fun initView() {
        if(intent.getBooleanExtra(DeepLink.IS_DEEP_LINK,false)){
            url = URLDecoder.decode(intent.extras.getString(ClientUri.DETAIL_PARAM_URL))
        }
        Log.e("DetailActivity",url)
        setupToolbar(toolbar)
        tv_title.text = "详情"
        webView.loadUrl(url)
        webView.setWebViewClient(object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        })
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityDetailBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_detail)
    }

}