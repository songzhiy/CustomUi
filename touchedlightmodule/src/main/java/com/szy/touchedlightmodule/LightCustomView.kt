package com.szy.touchedlightmodule

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import kotlin.math.cos
import kotlin.math.sin

fun getTwoPointDistance(pointA: LightCustomView.Point, pointB: LightCustomView.Point): Double =
    Math.sqrt(Math.pow((pointA.x - pointB.x), 2.0) + Math.pow((pointA.y - pointB.y), 2.0))

/**
 * Created by songzhiyang on 2020-05-15.
 * @author songzhiyang
 */
class LightCustomView : View {

    private var ratio = 1f//当前的比例
    private val circleRadius = 100.0f

    private var pointA: Point = LightCustomView.Point(0.0, 0.0)
    private var pointB: Point = LightCustomView.Point(0.0, 0.0)

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
        val pointC = getPointC(k)
        //这时有了三个点，然后开始画向心弧线
        val path = Path()
        path.moveTo(pairPoint.second.x.toFloat(), pairPoint.second.y.toFloat())
        path.cubicTo(
            pairPoint.second.x.toFloat(), pairPoint.second.y.toFloat(),
            pointC.x.toFloat(), pointC.y.toFloat(),
            pairPoint.first.x.toFloat(), pairPoint.first.y.toFloat()
        )
//        val rect = RectF()
//        rect.left = (pointC.x - circleRadius).toFloat()
//        rect.top = (pointC.y - circleRadius).toFloat()
//        rect.right = (pointC.x + circleRadius).toFloat()
//        rect.bottom = (pointC.y + circleRadius).toFloat()
//        path.addArc(rect, -45f, 90f)
        canvas?.drawPath(path, paint)
    }

    private fun getPointC(k: Float): Point {
        //这里计算c点的办法有些绕 主要根据以下几点：
        //1、ab与oc垂直，那么ab的斜率 * oc的斜率 = -1
        //2、c点的横坐标 = o点横坐标 - cosa * oc
        //3、c点的纵坐标 = o点纵坐标 + sina * oc
        //4、oc = k * ab
        //5、sina平方 + cosa平方 = 1
        //目标 计算出cosa即可
        //通过上面的基础，我们可以推导出 cona = 1/Math.sqr(1+tana平方) tana = -1/ab斜率

        //先计算下ab斜率
        //这里进行了一次修正 因为android的坐标系 y是与普通坐标系相反 向下为正 且越来越大
        val kab = -((pointA.y - pointB.y) / (pointA.x - pointB.x))
        //获得oc的斜率
        val koc = -1 / kab
        //计算出ab中心点o的坐标
        val pointOx = pointB.x + (pointA.x - pointB.x) / 2
        val pointOy = pointA.y - (pointA.y - pointB.y) / 2
        //计算cosa 基于 cos平方 + sin平方 = 1；tana = koc 可推导出下面的公式
        var cosa = 1 / (Math.sqrt(1 + Math.pow(koc, 2.0)))
        //这里处理的原因是：
        //当角度a大于90度时，x坐标实际上应为 pointOx - cos(Pi - a) * k * ab = pointOx + cosa * k * ab
        if (koc > 0) cosa = cosa else cosa = -cosa
        val sina = koc * cosa
        //根据o点坐标 计算c点坐标
        val pointCx = pointOx - k * getTwoPointDistance(pointA, pointB) * cosa
        val pointCy = pointOy + k * getTwoPointDistance(pointA, pointB) * sina

        return Point(pointCx, pointCy)
    }

    private fun findTwoPoint(ratio: Float, thisViewRectangle: Rect): Pair<Point, Point> {
        pointA = getPointA(ratio, thisViewRectangle)
        pointB = getPointB(ratio, thisViewRectangle)
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