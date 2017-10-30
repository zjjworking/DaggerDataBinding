package com.zjj.daggerdatabinding.widget

import android.content.Context
import android.view.MotionEvent
import android.support.v4.view.ViewConfigurationCompat
import android.view.ViewConfiguration
import android.support.v4.view.ViewPager
import android.util.AttributeSet


/**
 * Created by zjj on 17/10/26.
 * viewPager左右滑动冲突解决
 */
class SwipeViewPager : ViewPager {

    private var mTouchSlop: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        val context = context
        val configuration = ViewConfiguration.get(context)
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration)
    }

    internal var mLastX: Float = 0.toFloat()
    internal var mLastY: Float = 0.toFloat()

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentItem == 0) {
            val x = ev.x
            val y = ev.y
            when (ev.action) {
                MotionEvent.ACTION_MOVE -> {
                    val xDiff = Math.abs(x - mLastY)
                    val yDiff = Math.abs(y - mLastY)
                    //在第一页，判断到是向左边滑动，即想滑动第二页
                    if (xDiff > 0 && x - mLastX < 0 && xDiff * 0.5f > yDiff) {
                        //告诉父容器不要拦截事件
                        parent.requestDisallowInterceptTouchEvent(true)
                    } else if (yDiff > mTouchSlop && xDiff < mTouchSlop) {
                        //竖直滑动时，告诉父容器拦截事件，用于在ScrollView中可以竖直滑动
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
                MotionEvent.ACTION_UP -> parent.requestDisallowInterceptTouchEvent(false)
            }
            mLastX = x
            mLastY = y
        } else {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return super.dispatchTouchEvent(ev)
    }
}