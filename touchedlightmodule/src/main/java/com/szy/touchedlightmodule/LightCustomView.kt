package com.szy.touchedlightmodule

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import kotlin.math.cos
import kotlin.math.sin

/**
 * Created by songzhiyang on 2020-05-15.
 * @author songzhiyang
 */
class LightCustomView : View {

    private var ratio = 0.5f//当前的比例
    private val circleRadius = 100.0f

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

    override fun onDraw(canvas: Canvas?) {
        val paint = Paint()
        val thisViewRectangle = Rect()
        this.getDrawingRect(thisViewRectangle)
        //1、画圆
        drawCircle(canvas, thisViewRectangle, paint)
        //2、寻找到两个圆上的点
        val pairPoint = findTwoPoint(ratio, thisViewRectangle)
        //3、画两个点的向心贝塞尔曲线 这个0.43是 oc = k * ab，其中 o为a,b中点，oc为ab垂线
        drawInnerArc(pairPoint, 0.43f, canvas, paint)
        //4、画两个点的离心弧线
        //5、将圆与向心贝塞尔曲线、离心弧的区域进行裁剪，剩余的便是月亮
    }

    private fun drawInnerArc(
        pairPoint: Pair<Point, Point>,
        k: Float,
        canvas: Canvas?,
        paint: Paint
    ) {
        //todo 这里应该先通过k求出 两点中垂线上的点 这里先假设是0，0点
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_OUT))
        val pointC = getPointC(pairPoint, k)
        //这时有了三个点，然后开始画向心弧线
        val path = Path()
        path.moveTo(pairPoint.second.x.toFloat(), pairPoint.second.y.toFloat())
        path.cubicTo(
            pairPoint.second.x.toFloat(), pairPoint.second.y.toFloat(),
            pointC.x.toFloat(), pointC.y.toFloat(),
            pairPoint.first.x.toFloat(), pairPoint.first.y.toFloat()
        )
        val rect = RectF()
        rect.left = (pointC.x - circleRadius).toFloat()
        rect.top = (pointC.y - circleRadius).toFloat()
        rect.right = (pointC.x + circleRadius).toFloat()
        rect.bottom = (pointC.y + circleRadius).toFloat()
        path.addArc(rect, -45f, 90f)
        canvas?.drawPath(path, paint)
    }

    private fun getPointC(pairPoint: Pair<Point, Point>, k: Float): Point {
        val rect = Rect()
        this.getDrawingRect(rect)
        return Point(rect.centerX().toDouble(), rect.centerY().toDouble())
    }

    private fun findTwoPoint(ratio: Float, thisViewRectangle: Rect): Pair<Point, Point> {
        val pointA = getPointA(ratio, thisViewRectangle)
        val pointB = getPointB(ratio, thisViewRectangle)
        return Pair(pointA, pointB)
    }

    private fun getPointA(ratio: Float, thisViewRectangle: Rect): Point {
        //pointA 应该从 -45度 -> 45度 横跨90度 即月亮的上角 始终在 一、四象限
        val originAngle = -Math.PI / 4
        val transferAngle = Math.PI / 2 * ratio
        val endAngle = originAngle + transferAngle
        val pointAX = thisViewRectangle.centerX() + cos(endAngle) * circleRadius
        val pointAY = thisViewRectangle.centerY() - sin(endAngle) * circleRadius
        return Point(pointAX, pointAY)
    }

    private fun getPointB(ratio: Float, thisViewRectangle: Rect): Point {
        //pointB 应该从 -45度 -> -135度 横跨了270度 即月亮的下角 可以经历 一、二、三、四象限
        val startAngle = -Math.PI / 4
        val transferAngle = Math.PI * 1.5 * ratio
        val endAngle = startAngle + transferAngle
        val pointBX = thisViewRectangle.centerX() + cos(endAngle) * circleRadius
        val pointBY = thisViewRectangle.centerY() - sin(endAngle) * circleRadius
        return Point(pointBX, pointBY)
    }

    private fun drawCircle(
        canvas: Canvas?,
        thisViewRectangle: Rect,
        paint: Paint
    ) {
        paint.color = Color.WHITE
        val centerX = thisViewRectangle.centerX()
        val centerY = thisViewRectangle.centerY()
        canvas?.drawCircle(centerX.toFloat(), centerY.toFloat(), circleRadius, paint)
    }

    data class Point(val x: Double, val y: Double)
}