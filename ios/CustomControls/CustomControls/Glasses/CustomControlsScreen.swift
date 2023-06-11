//Copyright (c) 2020 Everysight LTD

import Foundation
import NativeEvsKit


class CustomControlsScreen : Screen {
    
    private var text = Text()
   
    override func onCreate() {
        add(uiElement: SimpleControl().setX(x: 30).setY(y: 30))
        add(uiElement: SimpleControl().setX(x: 30).setY(y: 100))

        text
            .setText(text: "Custom Controls")
            .setResource(font: Font(stockFont: .medium))
            .setTextAlign(align: Align.center)
        text
            .setX(x: getWidth()/2).setY(y: getHeight()-40)
            .setForegroundColor(color: EvsColor.green.rgba)
        add(uiElement: text)

        let clock = AnalogClockControl()
        clock
            .setX(x: getWidth()/2-50.0)
            .setY(y: 30.0)
            .setWidth(width: 150.0)
            .setHeight(height: 150.0)
        add(uiElement: clock)
        
    }
}
