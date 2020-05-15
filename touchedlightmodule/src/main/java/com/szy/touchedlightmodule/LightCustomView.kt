package com.szy.touchedlightmodule

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi

/**
 * Created by songzhiyang on 2020-05-15.
 * @author songzhiyang
 */
class LightCustomView : View {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

}