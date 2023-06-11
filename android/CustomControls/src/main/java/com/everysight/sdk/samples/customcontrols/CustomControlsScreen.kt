package com.everysight.sdk.samples.customcontrols

import UIKit.app.Screen
import UIKit.app.data.Align
import UIKit.app.data.EvsColor
import UIKit.app.resources.Font
import UIKit.widgets.Text

class CustomControlsScreen:Screen() {

    private val text = Text()
    companion object{
        const val SZ_PIX = 150f
    }

    override fun onCreate() {
        add(SimpleControl().setX(30f).setY(30f))
        add(SimpleControl().setX(30f).setY(100f))

        text.setText("Custom Controls").setResource(Font.StockFont.Medium).setTextAlign(Align.center)
        text.setX(getWidth()/2).setY(getHeight()-40).setForegroundColor(EvsColor.Green.rgba)
        add(text)

        val clock = AnalogClockControl()
        clock.setX((getWidth()- SZ_PIX)/2).setY((getHeight()-SZ_PIX)/2).setWidth(SZ_PIX).setHeight(SZ_PIX)
        add(clock)
    }

}