package com.everysight.sdk.samples.customcontrols

import UIKit.app.data.EvsColor
import UIKit.widgets.Frame
import UIKit.widgets.Rect

class SimpleControl:Frame() {

    override fun onCreate() {
        super.onCreate()

        add(Rect()
            .setWidth(50f)
            .setHeight(50f)
            .setForegroundColor(EvsColor.Blue.rgba)
            .setBackgroundColor(EvsColor.Blue.rgba))

        add(Rect().setWidth(50f)
            .setHeight(50f)
            .setForegroundColor(EvsColor.Green.rgba)
            .setBackgroundColor(EvsColor.Green.rgba)
            .rotate(45f))
    }

}
