package com.szy.touchedlightmodule

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.RequiresApi

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

    //todo 天气view的直径 通过framelayout 自定义属性设置 处理给自定义view
    private var lightViewRadius = dp2px(getContext(), 50f)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0, 0)

    constructor(context: Context) : this(context, null, 0, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

}