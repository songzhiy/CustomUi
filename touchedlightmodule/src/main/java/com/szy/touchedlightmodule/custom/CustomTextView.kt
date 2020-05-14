package com.szy.touchedlightmodule.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.szy.touchedlightmodule.R

/**
 * Created by songzhiyang on 2020-05-14.
 * @author songzhiyang
 */
class CustomTextView : TextView {

    constructor(context: Context) : this(context, null) {
        println("init 1")
    }

    constructor(context: Context, attributeSet: AttributeSet?) : this(
        context,
        attributeSet,
        R.attr.customStyleInTheme
    ) {
        println("init 2")
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attributeSet,
        defStyleAttr,
        R.style.CustomViewInDefStyleRes
    ) {
        println("init 3")
    }

    constructor(
        context: Context,
        attributeSet: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attributeSet, defStyleAttr, defStyleRes) {
        initView(context, attributeSet, defStyleAttr, defStyleRes)
    }

    private fun initView(
        context: Context,
        attributeSet: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        val typedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.CustomTextView,
            defStyleAttr,
            defStyleRes
        )
        val text1 = typedArray.getString(R.styleable.CustomTextView_customText1)
        val text2 = typedArray.getString(R.styleable.CustomTextView_customText2)
        val text3 = typedArray.getString(R.styleable.CustomTextView_customText3)
        val text4 = typedArray.getString(R.styleable.CustomTextView_customText4)
        val text5 = typedArray.getString(R.styleable.CustomTextView_customText5)
        typedArray.recycle()

        println("text1 --- $text1")
        println("text2 --- $text2")
        println("text3 --- $text3")
        println("text4 --- $text4")
        println("text5 --- $text5")
    }
}