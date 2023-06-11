//Copyright (c) 2020 Everysight LTD

import Foundation
import NativeEvsKit

class OtaHandlingScreen : Screen {
    private let text = Text()
    override func onCreate() {
        super.onCreate()
        text.setText(text: "OTA Handling")
            .setResource(font: Font(stockFont: .medium))
            .setTextAlign(align: Align.center)
        text.setX(x: getWidth()/2)
            .setY(y: getHeight()/2)
            .setForegroundColor(color: EvsColor.green.rgba)
        add(uiElement: text)
    }
}
