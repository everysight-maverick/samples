//Copyright (c) 2020 Everysight LTD

import Foundation
import NativeEvsKit

class GlassesEventsScreen : Screen {
    
    override func onCreate() {
        let text = Text()
        text.setText(text: "Glasses Events")
            .setResource(font: Font(stockFont: .medium))
            .setTextAlign(align: Align.center)
            .setX(x: getWidth()/2)
            .setY(y: getHeight()/2)
            .setForegroundColor(color: EvsColor.green.rgba)
        add(uiElement: text)
    }
}
