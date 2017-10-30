package com.zjj.daggerdatabinding.widget

import android.graphics.drawable.Drawable
import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewTreeObserver.OnPreDrawListener
import android.widget.TextView
import android.widget.RelativeLayout
import android.view.LayoutInflater
import android.support.v4.content.ContextCompat
import android.text.TextUtils

import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.zjj.daggerdatabinding.R


/**
 * 按行数进行折叠带过渡动画的TextView
 * <br></br>custom TextView that can be expanded with a smooth transition animation
 */
class TextViewExpandableAnimation(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs), View.OnClickListener {

    /**
     * TextView
     */
    private var textView: TextView? = null

    /**
     * 收起/全部TextView
     * <br></br>shrink/expand TextView
     */
    private var tvState: TextView? = null

    /**
     * 点击进行折叠/展开的图片
     * <br></br>shrink/expand icon
     */
    private var ivExpandOrShrink: ImageView? = null

    /**
     * 底部是否折叠/收起的父类布局
     * <br></br>shrink/expand layout parent
     */
    private var rlToggleLayout: RelativeLayout? = null

    /**
     * 提示折叠的图片资源
     * <br></br>shrink drawable
     */
    var drawableShrink: Drawable? = null
    /**
     * 提示显示全部的图片资源
     * <br></br>expand drawable
     */
    var drawableExpand: Drawable? = null

    /**
     * 展开提示文本
     * <br></br>expand text
     */
    private var textExpand: String? = null
    /**
     * 收缩提示文本
     * <br></br>shrink text
     */
    private var textShrink: String? = null

    /**
     * 是否折叠显示的标示
     * <br></br>flag of shrink/expand
     */
    private var isShrink = false

    /**
     * 是否需要折叠的标示
     * <br></br>flag of expand needed
     */
    private var isExpandNeeded = false

    /**
     * 是否初始化TextView
     * <br></br>flag of TextView Initialization
     */
    private var isInitTextView = true

    /**
     * 折叠显示的行数
     * <br></br>number of lines to expand
     */
    private var expandLines: Int = 0

    /**
     * 文本的行数
     * <br></br>Original number of lines
     */
    private var textLines: Int = 0

    /**
     * 显示的文本
     * <br></br>content text
     */
    /**
     * 取得显示的文本内容
     * get content text
     *
     * @return content text
     */
    var textContent: CharSequence? = null
        private set

    /**
     * 显示的文本颜色
     * <br></br>content color
     */
    private var textContentColor: Int = 0

    /**
     * 显示的文本字体大小
     * <br></br>content text size
     */
    private var textContentSize: Float = 0.toFloat()

    /**
     * 动画线程
     * <br></br>thread
     */
    private var thread: Thread? = null

    /**
     * 动画过度间隔
     * <br></br>animation interval
     */
    var sleepTime = 22

    /**
     * handler信号
     * <br></br>handler signal
     */
    private val WHAT = 2
    /**
     * 动画结束信号
     * <br></br>animation end signal of handler
     */
    private val WHAT_ANIMATION_END = 3

    /**
     * 动画结束，只是改变图标，并不隐藏
     * <br></br>animation end and expand only,but not disappear
     */
    private val WHAT_EXPAND_ONLY = 4

    init {
        initValue(context, attrs)
        initView(context)
        initClick()
    }

    private fun initValue(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs,
                R.styleable.TextViewExpandableAnimation)

        expandLines = ta.getInteger(
                R.styleable.TextViewExpandableAnimation_tvea_expandLines, 5)

        drawableShrink = ta
                .getDrawable(R.styleable.TextViewExpandableAnimation_tvea_shrinkBitmap)
        drawableExpand = ta
                .getDrawable(R.styleable.TextViewExpandableAnimation_tvea_expandBitmap)

        textShrink = ta.getString(R.styleable.TextViewExpandableAnimation_tvea_textShrink)
        textExpand = ta.getString(R.styleable.TextViewExpandableAnimation_tvea_textExpand)

        if (null == drawableShrink) {
            drawableShrink = ContextCompat.getDrawable(context, R.mipmap.icon_green_arrow_up)
        }

        if (null == drawableExpand) {
            drawableExpand = ContextCompat.getDrawable(context, R.mipmap.icon_green_arrow_down)
        }

        if (TextUtils.isEmpty(textShrink)) {
            textShrink = context.getString(R.string.shrink)
        }

        if (TextUtils.isEmpty(textExpand)) {
            textExpand = context.getString(R.string.expand)
        }


        textContentColor = ta.getColor(R.styleable.TextViewExpandableAnimation_tvea_textContentColor, ContextCompat.getColor(context, R.color.gray_light))
        textContentSize = ta.getDimension(R.styleable.TextViewExpandableAnimation_tvea_textContentSize, 14f)

        ta.recycle()
    }

    private fun initView(context: Context) {

        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_textview_expand_animation, this)

        rlToggleLayout = findViewById(R.id.rl_expand_text_view_animation_toggle_layout) as RelativeLayout

        textView = findViewById(R.id.tv_expand_text_view_animation) as TextView
        textView!!.setTextColor(textContentColor)
        textView!!.paint.textSize = textContentSize

        ivExpandOrShrink = findViewById(R.id.iv_expand_text_view_animation_toggle) as ImageView

        tvState = findViewById(R.id.tv_expand_text_view_animation_hint) as TextView

    }

    private fun initClick() {
        textView!!.setOnClickListener(this)
        rlToggleLayout!!.setOnClickListener(this)
    }

    fun setText(charSequence: CharSequence) {

        textContent = charSequence

        textView!!.text = charSequence.toString()

        val viewTreeObserver = textView!!.viewTreeObserver
        viewTreeObserver.addOnPreDrawListener(OnPreDrawListener {
            if (!isInitTextView) {
                return@OnPreDrawListener true
            }
            textLines = textView!!.lineCount
            isExpandNeeded = textLines > expandLines
            isInitTextView = false
            if (isExpandNeeded) {
                isShrink = true
                doAnimation(textLines, expandLines, WHAT_ANIMATION_END)
            } else {
                isShrink = false
                doNotExpand()
            }
            true
        })

    }

    @SuppressLint("HandlerLeak")
    private val handler = object : Handler() {

        override fun handleMessage(msg: Message) {
            if (WHAT == msg.what) {
                textView!!.maxLines = msg.arg1
                textView!!.invalidate()
            } else if (WHAT_ANIMATION_END == msg.what) {
                setExpandState(msg.arg1)
            } else if (WHAT_EXPAND_ONLY == msg.what) {
                changeExpandState(msg.arg1)
            }
            super.handleMessage(msg)
        }

    }

    /**
     * @param startIndex 开始动画的起点行数 <br></br> start index of animation
     * @param endIndex   结束动画的终点行数 <br></br> end index of animation
     * @param what       动画结束后的handler信号标示 <br></br> signal of animation end
     */
    private fun doAnimation(startIndex: Int, endIndex: Int,
                            what: Int) {

        thread = Thread(Runnable {
            if (startIndex < endIndex) {
                // 如果起止行数小于结束行数，那么往下展开至结束行数
                // if start index smaller than end index ,do expand action
                var count = startIndex
                while (count++ < endIndex) {
                    val msg = handler.obtainMessage(WHAT, count, 0)

                    try {
                        Thread.sleep(sleepTime.toLong())
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                    handler.sendMessage(msg)
                }
            } else if (startIndex > endIndex) {
                // 如果起止行数大于结束行数，那么往上折叠至结束行数
                // if start index bigger than end index ,do shrink action
                var count = startIndex
                while (count-- > endIndex) {
                    val msg = handler.obtainMessage(WHAT, count, 0)
                    try {
                        Thread.sleep(sleepTime.toLong())
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                    handler.sendMessage(msg)
                }
            }

            // 动画结束后发送结束的信号
            // animation end,send signal
            val msg = handler.obtainMessage(what, endIndex, 0)
            handler.sendMessage(msg)
        })

        thread!!.start()

    }

    /**
     * 改变折叠状态（仅仅改变折叠与展开状态，不会隐藏折叠/展开图片布局）
     * change shrink/expand state(only change state,but not hide shrink/expand icon)
     *
     * @param endIndex
     */
    private fun changeExpandState(endIndex: Int) {
        rlToggleLayout!!.visibility = View.VISIBLE
        if (endIndex < textLines) {
            ivExpandOrShrink!!.setBackgroundDrawable(drawableExpand)
            tvState!!.text = textExpand
        } else {
            ivExpandOrShrink!!.setBackgroundDrawable(drawableShrink)
            tvState!!.text = textShrink
        }

    }

    /**
     * 设置折叠状态（如果折叠行数设定大于文本行数，那么折叠/展开图片布局将会隐藏,文本将一直处于展开状态）
     * change shrink/expand state(if number of expand lines bigger than original text lines,hide shrink/expand icon,and TextView will always be at expand state)
     *
     * @param endIndex
     */
    private fun setExpandState(endIndex: Int) {

        if (endIndex < textLines) {
            isShrink = true
            rlToggleLayout!!.visibility = View.VISIBLE
            ivExpandOrShrink!!.setBackgroundDrawable(drawableExpand)
            textView!!.setOnClickListener(this)
            tvState!!.text = textExpand
        } else {
            isShrink = false
            rlToggleLayout!!.visibility = View.GONE
            ivExpandOrShrink!!.setBackgroundDrawable(drawableShrink)
            textView!!.setOnClickListener(null)
            tvState!!.text = textShrink
        }

    }

    /**
     * 无需折叠
     * do not expand
     */
    private fun doNotExpand() {
        textView!!.maxLines = expandLines
        rlToggleLayout!!.visibility = View.GONE
        textView!!.setOnClickListener(null)
    }

    override fun onClick(v: View) {
        if (v.getId() === R.id.rl_expand_text_view_animation_toggle_layout || v.getId() === R.id.tv_expand_text_view_animation) {
            clickImageToggle()
        }

    }

    private fun clickImageToggle() {
        if (isShrink) {
            // 如果是已经折叠，那么进行非折叠处理
            // do shrink action
            doAnimation(expandLines, textLines, WHAT_EXPAND_ONLY)
        } else {
            // 如果是非折叠，那么进行折叠处理
            // do expand action
            doAnimation(textLines, expandLines, WHAT_EXPAND_ONLY)
        }

        // 切换状态
        // set flag
        isShrink = !isShrink
    }

    fun getExpandLines(): Int {
        return expandLines
    }

    fun setExpandLines(newExpandLines: Int) {
        val start = if (isShrink) this.expandLines else textLines
        val end = if (textLines < newExpandLines) textLines else newExpandLines
        doAnimation(start, end, WHAT_ANIMATION_END)
        this.expandLines = newExpandLines
    }

}
