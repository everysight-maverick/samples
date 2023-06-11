package com.everysight.sdk.samples.inertialsensors

import UIKit.app.data.CalibrationStatus
import UIKit.app.data.EvsColor
import UIKit.app.resources.Font
import UIKit.widgets.*
import kotlin.math.round

class Compass:UIElementsGroup() {

    private var lineEW = Line("WE")
    private var lineCN = Line("North")
    private var lineCS = Line("South")
    private val northArrow = Polygon("NorthArrow")
    private val directionText = Text()

    private var circles = ArrayList<Ellipse>()
    private val arrowsGroup = UIElementsGroup()

    init {
        setWidth(RADAR_SZ_ON_CREATE)
        setHeight(RADAR_SZ_ON_CREATE)
        arrowsGroup.setWidth(getWidth())
        arrowsGroup.setHeight(getHeight())
    }

    override fun onCreate() {
        super.onCreate()

        //build circles
        val radX = getWidth()/2
        val radY = getHeight()/2
        val radXSteps = radX/CIRCLES_COUNT
        val radYSteps = radY/CIRCLES_COUNT
        for ( i in 1..CIRCLES_COUNT){
            val e = Ellipse("r$i")
            e
                .setEllipseInfo(radX,radY,radXSteps*i,radYSteps*i)
                .setForegroundColor(EvsColor.Orange).setPenThickness(PEN_WIDTH)
            circles.add(e)
            add(e)
        }
        circles.last().setForegroundColor(EvsColor.White).setPenThickness(1)

        //build lines
        lineEW
            .toCoord(getWidth()-2*radXSteps,0f)
            .setForegroundColor(EvsColor.Orange)
            .setX(radXSteps)
            .setY(radY)
        arrowsGroup.add(lineEW)

        lineCS
            .toCoord(0f,radY-radYSteps)
            .setPenThickness(PEN_WIDTH)
            .setForegroundColor(EvsColor.Blue.rgba)
            .setX(radX)
            .setY(radY)
        arrowsGroup.add(lineCS)

        lineCN
            .toCoord(0f,-radY+radYSteps)
            .setX(radX).setY(radY)
            .setPenThickness(PEN_WIDTH)
            .setForegroundColor(EvsColor.Red.rgba)
        arrowsGroup.add(lineCN)


        val xp = floatArrayOf(radX - ARROW_WIDTH, radX + ARROW_WIDTH , radX)
        val yp = floatArrayOf( radYSteps,radYSteps, radYSteps - ARROW_HEIGHT)
        northArrow
            .addMany(xp,yp,xp.size)
            .setFillColor(EvsColor.Red.rgba)
            .setForegroundColor(EvsColor.Red.rgba)
            .setPenThickness(PEN_WIDTH)
        arrowsGroup.add(northArrow)

        addLetters(radX,radYSteps)
        add(arrowsGroup)

        //add direction text
        val font = Font(Font.StockFont.Small)
        directionText
            .setResource(font)
            .setY(getHeight()-font.getMeasuredContentHeight(" "))
            .setForegroundColor(EvsColor.White)

        add(directionText)


    }

    private fun addLetters(radX: Float, radYSteps: Float) {
        val N = Path()
            .addPoint(radX - ARROW_WIDTH,radYSteps - 2* ARROW_HEIGHT+2*ARROW_WIDTH)
            .addPoint(radX - ARROW_WIDTH,radYSteps - 3* ARROW_HEIGHT+2*ARROW_WIDTH)
            .addPoint(radX + ARROW_WIDTH,radYSteps - 2* ARROW_HEIGHT+2*ARROW_WIDTH)
            .addPoint(radX + ARROW_WIDTH,radYSteps - 3* ARROW_HEIGHT+2*ARROW_WIDTH)
            .setPenThickness(PEN_WIDTH)
            .setForegroundColor(EvsColor.Red.rgba)
        arrowsGroup.add(N)

        val a1 = Arc().setArcInfo(radX,getHeight()-radYSteps+2* ARROW_WIDTH,
            ARROW_WIDTH *2/3f,180f,90f)
            .setForegroundColor(EvsColor.Blue.rgba)
            .setPenThickness(PEN_WIDTH)
        val a2 = Arc().setArcInfo(radX,getHeight()-radYSteps+3* ARROW_WIDTH,
            ARROW_WIDTH *2/3f,0f,270f)
            .setForegroundColor(EvsColor.Blue.rgba)
            .setPenThickness(PEN_WIDTH)
        arrowsGroup.add(a1).add(a2)

    }

    fun setWorldAngle(angle: Float, calibrationStatus: CalibrationStatus){
        val deg = if (angle <= -0.5f) {
            round(angle + 360f).toInt()
        } else {
            round(angle).toInt()
        }

        directionText.setText("$deg'")
        val color = when(calibrationStatus){
            CalibrationStatus.Calibrated -> EvsColor.Green
            CalibrationStatus.InProgress -> EvsColor.White
            CalibrationStatus.Required -> EvsColor.Orange
        }
        directionText.setForegroundColor(color)
        arrowsGroup.rotate(-angle)
    }


    companion object{
        private const val PEN_WIDTH = 2
        private const val ARROW_WIDTH = 6
        private const val CIRCLES_COUNT = 3
        private const val ARROW_HEIGHT = 12
        const val RADAR_SZ_ON_CREATE = 200f

    }

}