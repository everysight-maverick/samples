package com.everysight.sdk.samples.glassescontrol

import UIKit.app.Screen
import UIKit.app.data.Align
import UIKit.app.data.EvsColor
import UIKit.app.data.TouchDirection
import UIKit.app.resources.Font
import UIKit.widgets.Text


class GlassesControlScreen:Screen() {
    internal val text = Text()
    override fun onCreate() {

        text.setText("Hello World").setResource(Font.StockFont.Medium).setTextAlign(Align.center)
        text.setX(getWidth()/2).setY(getHeight()/2).setForegroundColor(EvsColor.Green.rgba)
        add(text)
    }

    override fun onTouch(touch: TouchDirection): Boolean {
        super.onTouch(touch)
        text.setText("Touch: $touch")
        return false
    }
}