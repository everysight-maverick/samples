//Copyright (c) 2020 Everysight LTD

import Foundation
import NativeEvsKit

class GlassesControlScreen : Screen {
    private let text = Text()
    override func onCreate() {
        text
            .setText(text: "Hello World")
            .setResource(font: Font(stockFont: .medium))
            .setTextAlign(align: Align.center)
        text
            .setX(x: getWidth()/2)
            .setY(y: getHeight()/2)
            .setForegroundColor(color: EvsColor.green.rgba)
        
        add(uiElement: text)
    }
    
    override func onTouch(touchDirection: TouchDirection) -> Bool {
        text.setText(text: "Touch \(touchDirection)")
        return false
    }
}
