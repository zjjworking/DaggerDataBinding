package com.zjj.daggerdatabinding.ui.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.zjj.daggerdatabinding.R
import com.zjj.daggerdatabinding.base.BaseBindingActivity
import com.zjj.daggerdatabinding.base.BaseBindingFragment
import com.zjj.daggerdatabinding.databinding.ActivityImageBinding
import com.zjj.daggerdatabinding.router.ClientUri

/**
 * Created by zjj on 17/10/19.
 */
class ImageActivity : BaseBindingActivity<ActivityImageBinding>(){
    override fun createDataBinding(savedInstanceState: Bundle?): ActivityImageBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_image)
    }

    override fun initView() {
        mBinding.url = intent.getStringExtra(ClientUri.IMG)
        Log.e("DetailActivity",intent.getStringExtra(ClientUri.IMG))
        mBinding.root.setOnClickListener {
            supportFinishAfterTransition()
        }
    }

}