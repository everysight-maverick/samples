//Copyright (c) 2020 Everysight LTD

import Foundation
import NativeEvsKit

class SimpleControl:Frame {
    
    override func onCreate() {
        super.onCreate()
        let rect = Rect()
        rect.setWidth(width: 50.0)
            .setHeight(height: 50.0)
            .setForegroundColor(color: EvsColor.blue.rgba)
            .setBackgroundColor(color: EvsColor.blue.rgba)
        add(uiElement: rect)
        
        let rect2 = Rect()
        rect2.setWidth(width: 50.0)
            .setHeight(height: 50.0)
            .setForegroundColor(color: EvsColor.green.rgba)
            .setBackgroundColor(color: EvsColor.green.rgba)
            .rotate(angleDegrees: 45.0)
        add(uiElement: rect2)
    }
    
}
