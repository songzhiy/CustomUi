package com.szy.touchedlightmodule

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.RequiresApi

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

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0, 0)

    constructor(context: Context) : this(context, null, 0, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

}