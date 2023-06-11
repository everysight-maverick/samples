//Copyright (c) 2020 Everysight LTD

import Foundation
import NativeEvsKit

class HelloWorldScreen : Screen {
    
    let text = Text()
    override func onCreate() {
        text.setText(text: "Hello World")
            .setResource(font: Font(stockFont: .medium))
            .setTextAlign(align: Align.center)
            .setForegroundColor(color: EvsColor.green.rgba)
        text.setX(x: getWidth()/2)
        text.setY(y: getHeight()/2)
        add(uiElement: text)
    }
    
}
