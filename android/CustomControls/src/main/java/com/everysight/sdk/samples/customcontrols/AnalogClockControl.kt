package com.everysight.sdk.samples.customcontrols

import UIKit.app.CanvasBuffer
import UIKit.app.data.Align
import UIKit.app.data.EvsColor
import UIKit.app.resources.Font
import UIKit.widgets.*
import android.graphics.Color
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.*

class AnalogClockControl : Frame() {

    private var padding: Float = 0f

    private var clockCenter = Ellipse("clockCenter")
    private var hours = Line("hours")
    private var minutes = Line("minutes")
    private var seconds = Line("seconds")
    private var radius = 0f
    private var hPrev = -1
    private var mPrev = -1
    private var sPrev = -1
    private var date = Text("date")

    private var centerX = 0f
    private var centerY = 0f
    private val format = SimpleDateFormat("MM-dd-YY")

    override fun onCreate() {
        super.onCreate()
        centerX = getWidth()/2
        centerY = getHeight()/2
        radius = min(centerX,centerY)
        padding = radius/20f

        addTicks()

        hours.setX(centerX).setY(centerY)
        hours.toAngle(radius- OUTER_WIDTH-7*padding,0f).setPenThickness(4).setForegroundColor(EvsColor.Green.rgba)
        add(hours)

        minutes.setX(centerX).setY(centerY)
        minutes.toAngle(radius-OUTER_WIDTH-padding,0f).setPenThickness(4).setForegroundColor(EvsColor.Green.rgba)
        add(minutes)

        seconds.setX(centerX).setY(centerY+4*padding)
        seconds.toAngle(radius-OUTER_WIDTH+3*padding,0f).setPenThickness(2).setForegroundColor(EvsColor.Blue.rgba)
        add(seconds)

        clockCenter
            .setEllipseInfo(centerX,centerY,2*padding)
            .setFillColor(EvsColor.Blue.rgba)
            .setForegroundColor(Color.BLACK)
            .setPenThickness(padding.toInt())
        add(clockCenter)
    }


    override fun onBeforeDraw(timestampMs:Long) {
        super.onBeforeDraw(timestampMs)
        updateTime()
    }

    private fun addTicks() {
        //val arr = arrayOf(1)
        val arr:Array<Int> = arrayOf(1,2,4,5,7,8,10,11)
        for(i in arr ){
            val angle = 360f*i/12f
            val rad = angle * (PI / 180f)

            val rx = centerX * sin(rad)
            val ry = centerY * cos(rad)
            val r = sqrt(rx*rx+ry*ry)
            val t = Arc("H$i")
            t
                .setArcInfo(centerX,centerY, (r-3).toFloat(), angle-1, (angle+1).toFloat())
                .setPenThickness(7)
                .setForegroundColor(EvsColor.Green.rgba)

            add(t)
        }

        val t12 = Text()
        t12.setText("12").setResource(Font.StockFont.Small).setTextAlign(
            Align.center).setX(centerX)
            .setY(0f + padding)
        add(t12)

        val t3 = Text()
        t3.setText("3").setResource(Font.StockFont.Small).setTextAlign(
            Align.right)
            .setX(centerX * 2 - padding).setY(centerY - 6)
        add(t3)

        val t6 = Text()
        t6.setText("6").setResource(Font.StockFont.Small).setTextAlign(
            Align.center).setX(centerX)
            .setY(2 * centerY - 8 - padding)
        add(t6)

        val t9 = Text()
        t9.setText("9").setResource(Font.StockFont.Small).setTextAlign(
            Align.left).setX(0f + padding)
            .setY(centerY - 6)
        add(t9)

        date.setText("").setResource(Font.StockFont.Small).setTextAlign(
            Align.center).setX(centerX)
            .setY(centerY + 5 * padding)
        add(date)

    }

    private fun updateTime() {
        val cal = Calendar.getInstance()
        cal.time = Date()
        val h = cal.get(Calendar.HOUR_OF_DAY)
        val m = cal.get(Calendar.MINUTE)
        val s = cal.get(Calendar.SECOND)


        date.setText(format.format(cal.time))

        var angle = (360f * (s+m*60+h*60*60) / (60*60*12f))
        if(angle.toInt()!=hPrev) {
            hours.rotate(angle, 0f, 0f)
            hPrev = angle.toInt()
        }

        angle = 360f * (s+m*60) / (60f*60f)
        if(angle.toInt()!=mPrev) {
            minutes.rotate(angle, 0f, 0f)
            mPrev = angle.toInt()
        }

        angle = 360f * s / (60f)
        if(angle.toInt()!=sPrev) {
            seconds.rotate(angle, 0f, -4*padding)
            sPrev = angle.toInt()
        }
    }

    companion object {
        private const val OUTER_WIDTH:Byte = 3
    }


}