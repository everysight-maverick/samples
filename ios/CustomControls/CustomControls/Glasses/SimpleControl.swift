//Copyright (c) 2020 Everysight LTD

import Foundation
import NativeEvsKit

class SimpleControl : Frame {
    override init() {
        super.init()
        let rect1 = Rect().setWidth(width: 50)
            .setHeight(height: 50)
            .setForegroundColor(color: EvsColor.blue.rgba)
            .setBackgroundColor(color: EvsColor.blue.rgba)
        add(uiElement: rect1)
        
        let rect2 = Rect()
            .setWidth(width: 50)
            .setHeight(height: 50)
            .setForegroundColor(color: EvsColor.green.rgba)
            .setBackgroundColor(color: EvsColor.green.rgba)
            .rotate(angleDegrees: 45)
        add(uiElement:rect2)
    }
}
