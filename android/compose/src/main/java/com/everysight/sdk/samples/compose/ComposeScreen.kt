@file:OptIn(ExperimentalUnsignedTypes::class)

package com.everysight.sdk.samples.compose

import UIKit.app.Screen
import UIKit.app.data.Align
import UIKit.app.data.AlignV
import UIKit.widgets.Text
import UIKit.widgets.layout.Column
import UIKit.widgets.layout.Row
import UIKit.widgets.layout.modifier

class ComposeScreen: Screen() {
    override fun onCreate() {
        super.onCreate()
        Column(verticalArrangement = AlignV.center, horizontalAlignment = Align.center, spacedBy = 5f).add(
            Text().setText("Compose Screen"),
            Row(horizontalArrangement = Align.center).add(
                Text().apply {
                    setText("Hello")
                    modifier().padding(right = 15f)
                },
                Text().apply {
                    setText("World")
                    modifier().padding(left = 15f)
                }
            ).setWidth(this@ComposeScreen.getWidth())
        )
        .setWidthHeight(getWidth(), getHeight())
        .addTo(this)
    }
}