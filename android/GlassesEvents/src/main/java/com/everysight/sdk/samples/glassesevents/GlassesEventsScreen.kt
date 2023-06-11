package com.everysight.sdk.samples.glassesevents

import UIKit.app.Screen
import UIKit.app.data.Align
import UIKit.app.data.EvsColor
import UIKit.app.resources.Font
import UIKit.widgets.Text


class GlassesEventsScreen:Screen() {
    internal val text = Text()
    override fun onCreate() {

        text.setText("Glasses Events").setResource(Font.StockFont.Medium).setTextAlign(Align.center)
        text.setX(getWidth()/2).setY(getHeight()/2).setForegroundColor(EvsColor.Green.rgba)
        add(text)
    }
}