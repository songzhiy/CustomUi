package com.szy.touchedlightmodule

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

fun dp2px(context: Context?, dp: Float): Int {
    var scale = 0f
    if (context == null) {
        scale = Resources.getSystem().displayMetrics.density
    } else {
        scale = context.resources.displayMetrics.density
    }
    return (scale * dp + 0.5).toInt()
}

/**
 * Created by songzhiyang on 2020-05-12.
 * @author songzhiyang
 *
 * 底层托底view 用来进行touch事件的处理与计算
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class TouchedLightFrameLayout(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    var lightCustomView: LightCustomView? = null

    var xLimitDistance = 50//手指横向移动的最大距离 该距离内视为有效
    val smoothMaxDistance = 500 //手指滑动的最大距离
    var lastTouchDownY = -1f
    var lastTouchDownX = -1f

    //todo 天气view的直径 通过framelayout 自定义属性设置 处理给自定义view
    private var lightViewRadius = dp2px(getContext(), 50f)

    //todo 天气view的背景padding 相当于中间宽度 + padding = 整个宽度
    private var lightViewBgPadding = dp2px(getContext(), 100f)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0, 0)

    constructor(context: Context) : this(context, null, 0, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchDownX = event.x
                lastTouchDownY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
//                if (abs(firstTouchDownX - event.x) > xLimitDistance) {
//                    //当x的触发距离大于最小触发限定值时，认为无效
//                    return super.onTouchEvent(event)
//                }
                //构建自定义亮度view
                checkAndInitView()
                lightCustomView!!.visibility = View.VISIBLE
                //对自定义亮度view进行设置
                lightCustomView!!.updateRatio((event.y - lastTouchDownY) / smoothMaxDistance)
                lastTouchDownX = event.x
                lastTouchDownY = event.y
            }
            MotionEvent.ACTION_UP -> {
                lastTouchDownY = -1f
                lastTouchDownX = -1f
                lightCustomView!!.visibility = View.GONE
            }
            MotionEvent.ACTION_CANCEL -> {
                lastTouchDownX = -1f
                lastTouchDownY = -1f
                lightCustomView?.visibility = View.GONE
            }
        }
        return true
    }

    private fun checkAndInitView() {
        if (lightCustomView == null) {
            //构建一个自定义view出来
            lightCustomView = LightCustomView(this.context)
            val params = FrameLayout.LayoutParams(
                lightViewBgPadding + lightViewRadius,
                lightViewBgPadding + lightViewRadius
            )
            params.gravity = Gravity.CENTER
            this.addView(lightCustomView, params)
            lightCustomView!!.setCircleRadius(this.lightViewRadius.toFloat())
            lightCustomView!!.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.custom_light_view_bg
                )
            )
        }
    }
}