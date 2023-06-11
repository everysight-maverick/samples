package com.everysight.sdk.samples.uielements

import UIKit.app.Screen
import UIKit.app.data.Align
import UIKit.app.data.AlignV
import UIKit.app.data.Direction
import UIKit.app.data.EvsColor
import UIKit.app.resources.Font
import UIKit.controls.popup.PopupMessage
import UIKit.widgets.*


class UIElementsScreen:Screen() {

    private var lastPopupTime = 0L
    override fun onCreate() {


        val text = Text()
        text.setText("UI Elements Screen").setResource(Font.StockFont.Medium).setTextAlign(Align.center)
        text.setX(getWidth()/2).setY(10f).setForegroundColor(EvsColor.Green.rgba)
        add(text)

        val textLeft = Text()
        textLeft.setText("Small Left Text").setResource(Font.StockFont.Small).setTextAlign(Align.left)
        textLeft.setX(10f).setY(100f)
        add(textLeft)

        val textRight = Text()
        textRight.setText("Large Right Text").setResource(Font.StockFont.Large).setTextAlign(Align.right)
        textRight.setX(getWidth()-10f).setY(getHeight()-textRight.getHeight()-50f)
        add(textRight)

        val line = Line()
        line.setX(20f).setY(getHeight()/2)
        line.toAngle(getWidth()-20f,90f)
            .setPenThickness(4)
            .setForegroundColor(EvsColor.Green.rgba)
        add(line)

        val rect = Rect()
        rect.setX(30f).setY(50f).setWidth(50f).setHeight(30f).setBackgroundColor(EvsColor.Blue.rgba)
        add(rect)

        val polygon = Polygon()
        polygon
            .addPoint(10f,80f)
            .addPoint(50f,100f)
            .addPoint(30f,110f)
            .addPoint(20f,60f)
        polygon.setX(110f)
        add(polygon)


        val path = Path()
        path
            .moveTo(200f,55f)
            .lineTo(200f,70f)
            .lineTo(210f,65f)
            .moveTo(230f,55f)
            .lineTo(230f,70f)
            .lineTo(240f,65f)
        add(path)

        val ellipse = Ellipse()
        ellipse.setEllipseInfo(215f,80f,40f)
        add(ellipse)

        val arc = Arc()
        arc.setArcInfo(215f,80f,20f,150f,210f)
        arc.setPenThickness(4).setForegroundColor(EvsColor.Red.rgba)
        add(arc)

        val arrow = Arrow()
        arrow
            .setDirection(Direction.down)
            .setArrowHeadInfo(20f)
            .setArrowBodyInfo(44f,100f)
            .setFillColor(EvsColor.Green)
            .setForegroundColor(EvsColor.Yellow)
            .setPenThickness(3)
            .setHeight(70f).setWidth(50f)
            .setX(200f).setY(200f)
        add(arrow)

    }

    override fun onUpdateUI(timestampMs: Long) {
        super.onUpdateUI(timestampMs)
        if(timestampMs - lastPopupTime > 10000) {
            lastPopupTime = timestampMs
            val t = Text()
                .setResource(Font.StockFont.Small)
                .setText("A popup message")
                .setForegroundColor(EvsColor.Blue.rgba)
            showPopup(PopupMessage(t, AlignV.bottom, 5000))
        }

    }
}